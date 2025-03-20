package com.gn.mvc.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링이 읽는 환경 파일입니다~ 라는 뜻
@EnableWebSecurity // 스프링 시큐리티 쓸거에요~ 라는 뜻
public class WebSecurityConfig {

	// 1-1. 요청중에 정적인 리소스가 있는 경우 -> Security 비활성화
	@Bean // security가 언제든 읽힐 수 있는 상태로 만든다. @Bean 쓰면 자동으로 public 처리 된다.
	WebSecurityCustomizer configure() {
		// 람다식 이라고 부른다. 화살표 함수랑 똑같다.
		// ignoring 무시할거다! 무엇을? 뒤의 url들을
		// requestMatchers는 특정 url을 정해주는 것이다.
		return web -> web.ignoring()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	// 1. 특정 요청이 들어왔을 때 어떻게 처리할 것인가
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService customUserDetailsService) throws Exception {
		// http 방식으로 /login, /signup, /logout은 일단 허락. 나머지 부분으로 갈때는 인증 받은 분(authenticated)만 받아주세요. 로그인은 로그인 페이지로, 성공하면 /board로. 로그아웃할때는 인증정보 지워주고, 로그아웃 성공하면 로그인쪽으로, 로그아웃 성공하면 세션 정보를 싹 날려주세요.
		http.userDetailsService(customUserDetailsService)
		.authorizeHttpRequests(requests -> requests
//				.requestMatchers("/login", "/signup", "/logout", "/", "/**").permitAll()
//				.anyRequest().authenticated())
				.anyRequest().permitAll()) // 모든 권한 준다~ 라는 뜻
		.formLogin(login -> login.loginPage("/login")
								.successHandler(new MyLoginSuccessHandler())
								.failureHandler(new MyLoginFailureHandler()))
		.logout(logout -> logout.clearAuthentication(true)
							.logoutSuccessUrl("/login")
							.invalidateHttpSession(true));
		return http.build();
	}
	
	// 2. 비밀번호 암호화에 사용될 Bean 등록
	@Bean
	PasswordEncoder passwordEncoder() {
		// 스프링시큐리티가 가지고 있는 인코더
		return new BCryptPasswordEncoder();
	}
	
	// 1-2. AuthenticationManager(인증 관리) << 얘한테 1.을 매개변수로 인식 시켜줘야한다.
	@Bean
	AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
	
}
