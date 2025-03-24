package com.gn.mvc.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.entity.Member;

public class ChatRoomSpecification{

	public static Specification<ChatRoom> fromMemberEquals(Member member){
		// where from_member = (select * from member where = 회원번호)
		return(root, query, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("fromMember"), member);
	}
	
	// where to_member = (select * from member where = 회원번호)
	public static Specification<ChatRoom> toMemberEquals(Member member){
		return(root, query, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("toMember"), member);
	}
	

	
}
