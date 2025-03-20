package com.gn.mvc.service;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	// 
	private final DataSource dataSource;
	//
	private final UserDetailsService userDetailsService;
	
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

	public Member selectMemberOne(Long id) {
		Member member = repository.findById(id).orElse(null);
		return member;
	}
	
	
	// 1. 반환형 : int
	// 2. 메소드명 : updateMember
	// 3. 매개변수 : MemberDto
	// 4. 역할
	public int updateMember(MemberDto param) {
		int result = 0;
		try {
			// (1) 데이터베이스 회원 정보 수정
			param.setMember_pw(passwordEncoder.encode(param.getMember_pw()));
			Member updated = repository.save(param.toEntity());
			if(updated != null) {
				// (2) remember-me(DB, cookie 두 곳에 있다.)가 있다면 무효화
				// DB는 서비스에서! cookie는 컨트롤러에서! 하자
				// Springboot가 JdbcTemplate 방식을 쓰기 때문에 방식을 존중하는 것
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "DELETE FROM persistent_logins WHERE username = ?";
				jdbcTemplate.update(sql, param.getMember_id());
				// (3) 변경된 회원 정보 Security Context에 즉시 반영
				UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(param.getMember_id());
				Authentication newAuth = new UsernamePasswordAuthenticationToken(
						updatedUserDetails, updatedUserDetails.getPassword(), updatedUserDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
				result = 1;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int deleteMember(Long id) {
		int result = 0;
		try {
			Member deleteMember = repository.findById(id).orElse(null);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			String sql = "DELETE FROM persistent_logins WHERE username = ?";
			jdbcTemplate.update(sql, deleteMember.getMemberId());
			SecurityContextHolder.getContext().setAuthentication(null);
			
			repository.deleteById(id);
			
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
