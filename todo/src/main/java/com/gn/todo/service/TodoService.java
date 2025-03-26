package com.gn.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.todo.Dto.PageDto;
import com.gn.todo.Dto.SearchDto;
import com.gn.todo.Dto.TodoDto;
import com.gn.todo.Entity.Todo;
import com.gn.todo.repository.TodoRepository;
import com.gn.todo.specification.TodoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
	
	private final TodoRepository repository;

//	조회, 페이징, 검색
	public Page<Todo> selectTodoAll(SearchDto searchDto, PageDto pageDto) {
		
		Pageable pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("no").ascending());
//		System.out.println(pageDto.getNowPage() + " : 현재 nowPage"); nowpage = 1 잘 나옴
		Specification<Todo> spec = (root, query, criteriaBuilder) -> null;
		spec = spec.and(TodoSpecification.contentContains(searchDto.getSearch_text()));
		Page<Todo> list = repository.findAll(spec, pageable);
//		System.out.println(list);
//		System.out.println(list.isEmpty()); // 사이즈가 있는데 true..?
//		System.out.println(list.getSize()); // 사이즈가 5..?
		
		return list;
	}
	
//	할일 추가
	public Todo addTodo(TodoDto dto) {
		dto.setFlag("N");
		Todo entity = dto.toEntity();
		Todo saved = repository.save(entity);
		return saved;
	}

//	할일 체크
	public Todo updateTodo(Long id,String checkFlag) {
		Todo result = null;
		TodoDto dto = null;
		try {
			Todo todo = repository.findById(id).orElse(null);
			if(todo == null) {
				return null;
			}
			todo.setFlag(checkFlag);
			
			dto = TodoDto.builder()
							.no(todo.getNo())
							.content(todo.getContent())
							.flag(checkFlag)
							.build();
			
			
			result = repository.save(dto.toEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

//	할일 삭제
	public int deleteTodo(Long id) {
		int result = 0;
		try {
			Todo todo = repository.findById(id).orElse(null);
			if(todo != null) {
				repository.deleteById(id);
			}
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
}
