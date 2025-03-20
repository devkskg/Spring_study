package com.gn.mvc.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository repository;
	private final PasswordEncoder passwordEncoder;
	
	public MemberDto createMember(MemberDto dto) {
//		String oriPw = dto.getMember_pw();
//		String newPw = passwordEncoder.encode(oriPw);
//		dto.setMember_pw(newPw);
//		위의 3줄을 아래 1줄로 줄여 쓴 것.
//		이거 코드 짧아서 놓치기 쉬움!
		dto.setMember_pw(passwordEncoder.encode(dto.getMember_pw()));
		Member param = dto.toEntity();
		Member result = repository.save(param);
//		System.out.println(param);
//		System.out.println(result);
		return new MemberDto().toDto(result);
	}
}
