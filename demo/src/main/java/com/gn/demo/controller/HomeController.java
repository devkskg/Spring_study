package com.gn.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gn.demo.vo.Member;

@Controller
public class HomeController {

//	@GetMapping({"/",""})
//	public String home() {
////		실질적으로 읽는 위치는 아래와 같다. 환경 설정이 알아서 되어 있어서 되는 거다.
////		src/main/resources/templates/home.html
//		return "home";
//	}
	
//	ModelAndView를 통해서 데이터와 함께 어디로 갈지 정해준다.
	@GetMapping({"/",""})
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home");
		mav.addObject("age", 1);
		return mav;
	}
	
	@GetMapping({"/test1"}) // a태그의 href와 일치시키기, 인터넷 주소창에 나오는 주소가 된다.
	public String testMethod01(Model model01) {
//		request.setAttribute("name", "김철수");
		model01.addAttribute("name", "김철수");
		
		
//		src/main/resources/templates/test
		return "test"; // templates 아래에서 해당문자열"test"와 일치하는 html을 조회한다. 
	}
	
//	@GetMapping("/bye")
//	public String bye01(Model model) {
//		Member member = new Member("홍길동", 50);
//		model.addAttribute("member", member);
//		return "bye";
//		
//	}
	
	
}
