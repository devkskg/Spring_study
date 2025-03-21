package com.gn.mvc.security;

import javax.sql.DataSource;

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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.RequiredArgsConstructor;

@Configuration // 스프링이 읽는 환경 파일입니다~ 라는 뜻
@EnableWebSecurity // 스프링 시큐리티 쓸거에요~ 라는 뜻
@RequiredArgsConstructor
public class WebSecurityConfig {
	
//	Application properties의 정보에 접근해서 알아서 정보 찾는 소스..??
	private final DataSource dataSource;

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
				.requestMatchers("/login", "/signup", "/logout", "/").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()) // ~외의 요청은 권한이 있는 사용자만 가능합니다~ 라는 뜻
//				.anyRequest().permitAll()) // 모든 권한 준다~ 라는 뜻
		.formLogin(login -> login.loginPage("/login")
								.successHandler(new MyLoginSuccessHandler())
								.failureHandler(new MyLoginFailureHandler()))
		.logout(logout -> logout.logoutUrl("/logout")
							.clearAuthentication(true)
							.logoutSuccessUrl("/login")
							.invalidateHttpSession(true)
							.deleteCookies("remember-me"))
		.rememberMe(rememberMe -> rememberMe.rememberMeParameter("remember-me")
							.tokenValiditySeconds(60*60*24*30)
							.alwaysRemember(false)
							 .tokenRepository(tokenRepository()));
		// alwaysRemember는 항상! 기억한다~ 라는 뜻
		// tokenRepository는 DB에도 쿠키에도 토큰 정보 일부 저장해서 DB 정보 기준으로 쿠키랑 비교해서~~
		return http.build();
	}
	
	// 데이터베이스 접근 Bean 등록(JDBC방식으로 접근할 수 있도록 Bean만 만들자.) 테이블 자체가 remember-me와 약속된 포맷이다.
	@Bean
	PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		return jdbcTokenRepository;
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
