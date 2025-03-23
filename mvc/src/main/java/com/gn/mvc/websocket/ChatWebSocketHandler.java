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

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
	
	// new 연산자 있기 때문에 Bean이 아니어서 requiredConstructor 쓸 필요 없다. 그냥 전역 변수일 뿐이다.
	private static final Map<Long, WebSocketSession> userSessions = new HashMap<Long, WebSocketSession>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 쿼리 스트링 떼와서 쓰는 법!
		// 1. ws://localhost:8080/ws/chat?senderNo=3
		// 2. senderNo=3
		// 3. 0번 인덱스 -> senderNo, 1번 인덱스 -> 3
		String senderNo = session.getUri().getQuery().split("=")[1];
		userSessions.put(Long.parseLong(senderNo), session);
		
		
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		// 현제 message 안에 JSON 형태로 되어있다. 파싱 해보자!
		ObjectMapper objectMapper = new ObjectMapper();
		ChatMsgDto dto = objectMapper.readValue(message.getPayload(), ChatMsgDto.class);
		
		// 내가 썼지만 상대방도, 나도 기록이 남아야하니까~
		WebSocketSession receiverSession = userSessions.get(dto.getReceiver_no());
		if(receiverSession != null && receiverSession.isOpen()) {
			receiverSession.sendMessage(new TextMessage(dto.getMsg_content()));
		}
		WebSocketSession senderSession = userSessions.get(dto.getSender_no());
		if(senderSession != null && senderSession.isOpen()) {
			senderSession.sendMessage(new TextMessage(dto.getMsg_content()));
		}
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String userNo = session.getUri().getQuery().split("=")[1];
		userSessions.remove(Long.parseLong(userNo));
	}
	
}
