package com.gn.mvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MvcApplication implements WebMvcConfigurer {
	
//	이렇게 쓰면 모든 프로젝트가 같은 경로를 보게 된다.
	@Value("${ffupload.location}")
//	아래처럼 써도 되지만 똑같은 외부파일 경로를 읽어올 수 있도록 application.properties에서 선언하고 가져와서 쓰는 것이다.
//	private String fileDir = "C:/upload/";
	private String fileDir;
	
//	WebMvcConfigurer 얘가 가지고 있는 메소드 오버라이드할거다.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		바깥쪽 경로는 어디이고, 읽어올 때 내부에서 뭐라고 부르면서 쓸 것인가? << 를 설정
		
//		안쪽에서 뭐라고 부를 것인가?. uploads라고 시작하는 경로를 쓰게된다면 > file:///라고 대체돼서 쓰게 돼고. 바깥쪽에서 불러온 파일이다!
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:///" + fileDir);
	}

	public static void main(String[] args) {
		SpringApplication.run(MvcApplication.class, args);
	}

}
