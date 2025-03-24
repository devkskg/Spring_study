package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.entity.ChatMsg;
import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.repository.ChatMsgRepository;
import com.gn.mvc.repository.ChatRoomRepository;
import com.gn.mvc.specification.ChatMsgSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMsgService {
	
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMsgRepository chatMsgRepository;

	// 1. 기능 : 채팅 메시지 목록 조회
	// 2. 조건 : 채팅방 번호
	// 3. 결과 : 채팅 메시지 목록
	public List<ChatMsg> selectChatMsgAll(Long id){
		
		// 조건 : 채팅방 번호
		// (1) 전달받은 id를 기준으로 ChatRoom Entity 조회
		ChatRoom chatRoom = chatRoomRepository.findById(id).orElse(null);
		Specification<ChatMsg> spec = (root, query, criteriaBuilder) -> null;
		// (2) ChatRoom Entity 기준으로 Specification 생성
		spec = spec.and(ChatMsgSpecification.chatRoomNoEquals(chatRoom));
		// (3) spec을 매개변수로 전달하여 findAll 반환
//		System.out.println("스펙이야 : " + spec);
		
//		List<ChatRoom> chatRoomlist = chatRoomRepository.findAll(spec);
		List<ChatMsg> list = chatMsgRepository.findAll(spec);
//		System.out.println("리스트야 : " + list);
		return list;
		
		
	}
}
