package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.security.MemberDetails;
import com.gn.mvc.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService service;
	
	@GetMapping("/signup")
	public String createMemberView() {
//		System.out.println("페이지 이동");
		return "member/create";
	}

	@PostMapping("/signup")
	@ResponseBody // 응답을 JSON형태로 받는다.
	public Map<String, String> createMemberApi(
			MemberDto dto
			){
//		System.out.println("준비완료");
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
	
	@GetMapping("/login")
	public String loginView(
			// required=false 없어도 되지만, 있으면 받아줄게~ 라는 뜻 / 에러메세지가 있든 없든 허락하겠다.
			// MyLoginFailureHandler 에서 error 관련 세팅 해줬다.
			@RequestParam(value="error", required=false) String error, 
			@RequestParam(value="errorMsg", required=false) String errorMsg,
			Model model) {
		model.addAttribute("error", error);
		model.addAttribute("errorMsg", errorMsg);
		
		return "member/login";
	}
	
	@GetMapping("/member/{id}/update")
	public String updateView(@PathVariable("id") Long id, Model model) {
		Member member = service.selectMemberOne(id);
		model.addAttribute("member", member);
		return "member/update";
	}
	
	@PostMapping("/member/{id}/update")
	@ResponseBody
	public Map<String,String> memberUpdateApi(MemberDto param, HttpServletResponse response) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 수정중 오류가 발생하였습니다.");
		
		int result = service.updateMember(param);
		if(result > 0) {
			// 쿠키(remember-me) 무효화
			Cookie rememberMe = new Cookie("remember-me", null);
			rememberMe.setMaxAge(0); // 쿠키 수명 0초 만들기
			rememberMe.setPath("/"); // 모든 경로의 쿠키 삭제
			response.addCookie(rememberMe); // 브라우저의 쿠키 정보 무효화
			
			
			
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원이 수정되었습니다.");
		}
		
		return resultMap;
	}
	
	@DeleteMapping("/member/{id}")
	@ResponseBody
	public Map<String, String> memberDeleteApi(@PathVariable("id") Long id, HttpServletResponse response){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 탈퇴중 오류가 발생하였습니다.");
		
		int result = service.deleteMember(id);
		if(result > 0) {
			// 쿠키(remember-me) 무효화
			Cookie rememberMe = new Cookie("remember-me", null);
			rememberMe.setMaxAge(0); // 쿠키 수명 0초 만들기
			rememberMe.setPath("/"); // 모든 경로의 쿠키 삭제
			response.addCookie(rememberMe); // 브라우저의 쿠키 정보 무효화
			
			
			
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원 탈퇴 완료.");
		}
		return resultMap;
	}
}
