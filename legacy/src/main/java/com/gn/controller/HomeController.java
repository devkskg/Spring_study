package com.gn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//	@Controller 클라이언트의 요청을 받을 수 있는 클래스로 만들어줌.
//	전문용어 : 빈 스캐닝
@Controller
public class HomeController {
	
//	요청 메소드. 클라이언트가 요청한 url과 연결된 메소드
//	@RequestMapping(value="/", method=RequestMethod.GET)
//	위의 아래 두개의 mapping이 같은 것을 의미한다. 
//	@GetMapping("/")
//	여러개의 url을 적용하고싶다면? 배열 형태는 아래처럼!
	@GetMapping({"/",""})
	public String home() {
//		servlet-context.xml에 어디다 보내는지 써놨다.
//		/WEB-INF/views/home.jsp
		return "home";
	}
	
}
