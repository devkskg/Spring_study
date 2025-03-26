package com.gn.todo.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gn.todo.Dto.PageDto;
import com.gn.todo.Dto.SearchDto;
import com.gn.todo.Entity.Todo;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final TodoService service;
	

//	메인화면 진입시 조회 검색 페이징
	@GetMapping({"","/"})
	public String home(Model model, SearchDto searchDto, PageDto pageDto) {
		// 초기 화면 진입시 nowPage 세팅
		if(pageDto.getNowPage() == 0 || pageDto == null) {
			pageDto.setNowPage(1);
		}
		// 초기 화면 진입시 search_text 세팅
		if(searchDto.getSearch_text() == null) {
			searchDto.setSearch_text("");
		}
		Page<Todo> resultList = service.selectTodoAll(searchDto, pageDto);
		
		pageDto.setTotalPage(resultList.getTotalPages());
		// 방법 1 - null로 보내는 경우 html에서도 null 판단이 필요해서 코드가 복잡해진다.
//		if(resultList.isEmpty()) {
//			resultList = null;
//		}
		
		// 방법2 - Paging 정보를 제외한 list 정보만 넘기고 싶을 경우 쓴다
		model.addAttribute("todoList", resultList.getContent());
		
//		model.addAttribute("todoList", resultList);
		// 초기 화면 진입시 total page가 0으로 잡혀서 오류 발생 -> total page 세팅해서 오류 해결
		if(resultList.isEmpty()) {
			pageDto.setTotalPage(1);
		}
		model.addAttribute("pageDto", pageDto);
		model.addAttribute("searchDto", searchDto);
//		System.out.println(resultList);
//		System.out.println(resultList.isEmpty());
//		
//		System.out.println(pageDto);
//		System.out.println(searchDto);
		
		
		
		return "home";
	}
	
}
