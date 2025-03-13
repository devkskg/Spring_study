package com.gn.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	// localhost:8080/ || localhost:8080 이런 두 형태로 올 수 있기 때문
	@GetMapping({"","/"})
	public String home() {
		// src/main/resources/templates/home.html
		return "home";
	}
}
