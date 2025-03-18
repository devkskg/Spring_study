package com.gn.mvc.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;

public class AttachSpecification {

	public static Specification<Attach> boardEquals(Board board){
		return (root, query, criteriaBuilder) -> 
			criteriaBuilder.equal(root.get("board"), board);
//			엔티티 안의 (board)라는 필드를 기준으로 걔는 매개변수 board입니다~ ??
//			같은 애가 있으면 반환해주는 방식
	}
		
}
