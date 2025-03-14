package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService service;
	
	@GetMapping("/member/create")
	public String createMemberView() {
		System.out.println("페이지 이동");
		return "/member/create";
	}

	@PostMapping("/member")
	@ResponseBody // 응답을 JSON형태로 받는다.
	public Map<String, String> createMemberApi(
			MemberDto dto
			){
		System.out.println("준비완료");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원가입 중 오류가 발생했습니다.");
		MemberDto memberDto = service.createMember(dto); // 매개변수는 화면단에서 받은 정보 dto, 메소드 결과는 처리되어서 반환된 결과값 dto
		if(memberDto != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원가입이 완료되었습니다.");
		}
		return resultMap;
		
	}
}
