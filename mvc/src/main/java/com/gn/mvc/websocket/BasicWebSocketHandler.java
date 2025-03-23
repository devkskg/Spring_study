package com.gn.mvc.websocket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class BasicWebSocketHandler extends TextWebSocketHandler {
	
//	연결된 사용자들의 정보를 담을 수 있는 공간
	private static final List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 새로운 WebSocket이 연결(open)된 순간 동작하는 메소드
		// System.out.println("서버 : 연결");
		sessionList.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 클라이언트 -> 서버. 클라이언트가 서버에게 메세지를 보내는(send) 순간
		String payload = message.getPayload();
		// System.out.println("서버 : 메시지 받음 : " + payload);
		
		for(WebSocketSession temp : sessionList) {
			temp.sendMessage(new TextMessage(payload));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// WebSocket 연결 끊겼을 때
		// System.out.println("서버 : 연결 끊김");
		
		// 서버 나가면 세션 정보 지우기
		sessionList.remove(session);
	}

	
	
}
