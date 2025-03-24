package com.gn.mvc.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.mvc.entity.ChatRoom;

public class ChatMsgSpecification {

	public static Specification<ChatRoom> chatRoomNoEquals(ChatRoom chatRoom){
		return(root, query, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("chatRoom"), chatRoom);
	}
	
}
