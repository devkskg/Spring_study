package com.gn.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gn.demo.vo.Member;


@Controller
public class HongController {
	@GetMapping("/bye")
	public String selsectMemberList(Model model) {
		Member member = new Member("홍길동", 111);
		model.addAttribute("member", member);
		return "bye";
	}
}
