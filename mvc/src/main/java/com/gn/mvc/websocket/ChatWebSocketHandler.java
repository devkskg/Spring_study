package com.gn.mvc.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gn.mvc.dto.ChatMsgDto;
import com.gn.mvc.entity.ChatMsg;
import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.ChatMsgRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
	
	// new 연산자 있기 때문에 Bean이 아니어서 requiredConstructor 쓸 필요 없다. 그냥 전역 변수일 뿐이다.
	private static final Map<Long, WebSocketSession> userSessions = new HashMap<Long, WebSocketSession>();
	// roomNo을 쌓는 애를 만들자
	private static final Map<Long, Long> userRooms = new HashMap<Long, Long>();
	// DB에 저장하기 위해 ChatMsgRepository + Lombok의 도움을 받자
	private final ChatMsgRepository chatMsgRepository;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 쿼리 스트링 떼와서 쓰는 법!
		// 1. ws://localhost:8080/ws/chat?senderNo=3
		// 2. senderNo=3
		// 3. 0번 인덱스 -> senderNo, 1번 인덱스 -> 3
		//String senderNo = session.getUri().getQuery().split("=")[1];
		
		// 이제는 두가지 정보 필요!(userNo, roomNo)
		String userNo = getQueryParam(session, "senderNo");
		String roomNo = getQueryParam(session, "roomNo");
		userSessions.put(Long.parseLong(userNo), session);
		// 위 아래의 차이를 잘 알아보자
		// 연결된 사람이 누구고 몇번 채팅방을 사용하느냐!
		userRooms.put(Long.parseLong(userNo), Long.parseLong(roomNo));
		
		
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		// 현제 message 안에 JSON 형태로 되어있다. 파싱 해보자!
		ObjectMapper objectMapper = new ObjectMapper();
		// message.getPayload() 제이슨 형태로 받아오는?? 그래서 아래처럼 파싱한다.
		ChatMsgDto dto = objectMapper.readValue(message.getPayload(), ChatMsgDto.class);
		
		
		// 이 사람이 연결되어 있는 상황이라면~ 뜻!
		if(userSessions.containsKey(dto.getSender_no())) {
			// 화면에 응답해주기 전에 DB에 채팅 메시지 등록
			// 1. dto -> entity
			Member member = Member.builder()
					.memberNo(dto.getSender_no())
					.build();
			ChatRoom chatRoom = ChatRoom.builder()
					.roomNo(dto.getRoom_no())
					.build();
			ChatMsg entity = ChatMsg.builder()
					.sendMember(member)
					.chatRoom(chatRoom)
					.msgContent(dto.getMsg_content())
					.build();
			// 2. entity save
			chatMsgRepository.save(entity);
		}
		
		
		
		
		// 내가 썼지만 상대방도, 나도 기록이 남아야하니까~
		WebSocketSession receiverSession = userSessions.get(dto.getReceiver_no());
		
		//  && receiverRoom == dto.getRoom_no() 받는 사람이 채팅방 연결이 어디에 되어있는가? 확인하는 코드 필요. 방의 정보도 받아와야한다.
		// messagePayload와 맞는지 판단한다. 
		// dto.getReceiver_no() 받는 사람의 정보(PK)
		Long receiverRoom = userRooms.get(dto.getReceiver_no());
		
		if(receiverSession != null && receiverSession.isOpen() && receiverRoom == dto.getRoom_no()) {
			// receiverSession.sendMessage(new TextMessage(dto.getMsg_content()));
			// 메시지 JSON 데이터 전달
			receiverSession.sendMessage(new TextMessage(message.getPayload()));
		}
		WebSocketSession senderSession = userSessions.get(dto.getSender_no());
		Long senderRoom = userRooms.get(dto.getSender_no());
		// 보낸 사람이 똑같은 채팅방 위치에 있는가?
		if(senderSession != null && senderSession.isOpen() && senderRoom == dto.getRoom_no()) {
			senderSession.sendMessage(new TextMessage(message.getPayload()));
		}
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// String userNo = session.getUri().getQuery().split("=")[1];
		String userNo = getQueryParam(session, "senderNo");
		userSessions.remove(Long.parseLong(userNo));
		userRooms.remove(Long.parseLong(userNo));
	}
	
	// 기능 : WebSocketSession의 URL 파싱
	// 파라미터 : url, key값
	// 반환값 : value값
	/*
	 * author : 김가남
	 * history : 2025-03-24
	 * param : url, key data
	 * return : value data
	 * role : WebsocketSession url parsing
	 * 이런식으로 쓰면 친절하기는 한데 너무 오래 걸릴 것 같다.
	 */
	// 메소드 호출, 사용방법!
	// getQueryParam(session, "senderNo"); -> 3
	// getQueryParam(session, "roomNo"); -> 1
	private String getQueryParam(WebSocketSession session, String key) {
		// 쿼리스트링 가져온 것
		// senderNo=3&roomNo=1
		// 만약 더 있다면? 숫자만 조금 바꿔서 쓰기 좋음
		String query = session.getUri().getQuery();
		if(query != null) {
			String[] arr = query.split("&");
			// 0번 인덱스 : senderNo=3
			// 1번 인덱스 : roomNo=1
			for(String target : arr) {
				String[] keyArr = target.split("=");
				if(keyArr.length == 2 && keyArr[0].equals(key)) {
					return keyArr[1];
				}
			}
		}
		return null;
	}
	
}
