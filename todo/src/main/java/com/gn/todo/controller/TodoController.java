package com.gn.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.Dto.TodoDto;
import com.gn.todo.Entity.Todo;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class TodoController {
	private final TodoService service;

//	할일 추가
	@PostMapping("/todo01")
	@ResponseBody
	public Map<String, String> addContentApi(TodoDto dto) {
//		System.out.println("addContentApi 도착");
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할일 추가중 오류가 발생했습니다.");
		Todo result = service.addTodo(dto);
		
//		System.out.println("할일추가 결과 : " + result);
		
		if(result != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "정상적으로 할일 추가 되었습니다.");
		}
		
		return resultMap;
	}
	
//	할일 체크, 체크해제
	@PostMapping("/todo/{id}/update/{checkFlag}")
	@ResponseBody
	public Map<String, String> updateTodoApi(@PathVariable("id") Long id, @PathVariable("checkFlag") String checkFlag, @RequestBody(required = false) Map<String, Object> body){
//	public Map<String, String> updateTodoApi(@PathVariable("id") Long id, @PathVariable("checkFlag") String checkFlag){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "업데이트에 실패했습니다.");
		
		Todo result = service.updateTodo(id, checkFlag);
		System.out.println("제이슨으로 넘어온 데이터 : " + body);
		
		if(result != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "업데이트에 성공했습니다.");
		}
		
		return resultMap;
	}
	
//	할일 삭제
	@DeleteMapping("/todo/{id}")
	@ResponseBody
	public Map<String, String> deleteTodoApi(@PathVariable("id") Long id){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "삭제 실패했습니다.");
		
		int result = service.deleteTodo(id);
		if(result == 1) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "삭제 성공했습니다.");
		}
		return resultMap;
	}
	
}
