package com.gn.mvc.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gn.mvc.entity.Member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//UserDetails(스프링이 사용하는 사용자 정보 객체)를 구현한 구현체
@RequiredArgsConstructor
@Getter
public class MemberDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	// 멤버 Entity를 가져와서 쓴다?
	private final Member member;
	
	public Member getMember() {
		return member;
	}

	// 사용자 권한 설정
	// Collection 여러개의 권한을 가질 수 있으니깐.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("user"));
	}

	// 사용자의 비밀번호 반환
	@Override
	public String getPassword() {
		return member.getMemberPw();
	}

	// 사용자 이름 반환
	@Override
	public String getUsername() {
		// 아이디로서 쓸 수 있는 정보
		return member.getMemberId();
	}
	
//	이 아래는 추가로 넣을 수 있는 것들
//	계정 상태 관리
//	is~ 로 시작하는 메소드. boolean 타입으로 반환
	
	// 계정 만료 여부 반환 메소드
	// 임시 계정, 비활성화된 계정(퇴사 상태 등)
	@Override
	public boolean isAccountNonExpired() {
//		이런식으로 재직 여부 판단하기도 가능!
//		if(member.getExpired().equals("Y")) {
//			return false;
//		} else {
//			return true;
//		}
//		이런식으로 재직 여부 판단하기도 가능!
		return true;
	}
	
	// 계정 잠금 여부
	// 비밀번호 5번 틀리면 -> 10분간 로그인 금지
	@Override
	public boolean isAccountNonLocked() {
		// 쓸 수 있는 계정인가? 언제부터 몇 번 틀렸는가?
//		if(member.getLockedDate() + 10 > 현재시간 -> isAfter 써도 될듯) {
//			return false;
//		} else {
//			return true;
//		}
		// 쓸 수 있는 계정인가? 언제부터 몇 번 틀렸는가?
		return true;
	}
	
	// 패스워드 만료 여부
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	// 계정 사용 가능 여부
	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
