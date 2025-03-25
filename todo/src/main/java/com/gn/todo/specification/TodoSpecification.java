package com.gn.todo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.todo.Entity.Todo;

public class TodoSpecification {

	public static Specification<Todo> contentContains(String keyword){
		return (root, query, criteriaBuilder) -> 
			criteriaBuilder.like(root.get("content"), "%"+keyword+"%");
	}
	
}
