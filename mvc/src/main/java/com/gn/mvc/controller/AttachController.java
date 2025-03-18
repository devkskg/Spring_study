package com.gn.mvc.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gn.mvc.entity.Attach;
import com.gn.mvc.service.AttachService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AttachController {

	// final 까먹지 않게 조심하기! 매개변수 생성자와 같이 쓰기!
	private final AttachService service;
	
	// 파일 다운로드
	@GetMapping("/download/{id}")
	public ResponseEntity<Object> fileDownload(@PathVariable("id") Long id) {
		// 파일 다룰 때는 예외 상황이 많이 발생하므로 전체적으로 try~catch 감싸준다.
		try {
			// 1. id 기준으로 파일 메타 데이터 조회
			Attach fileData = service.selectAttachOne(id);
			// 2. 해당 파일이 없다면 404 에러(not found exception)
			if(fileData == null) {
				return ResponseEntity.notFound().build();
			}
			// 3. 파일 경로 가서 자바 안으로 파일 읽기(InputStream)
			Path filePath = Paths.get(fileData.getAttachPath());
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			// 4. 한글 파일명 인코딩
			String oriFileName = fileData.getOriName();
			// 한글로 되어있어도 내가 읽을게! 라는 뜻 - 브라우저별로 대응 방법이 다른다. 크롬,엣지에서 동작하는 코드를 썼다. 교안 확인.
			String encodedName = URLEncoder.encode(oriFileName, StandardCharsets.UTF_8);
			// 5. 브라우저에게 파일 정보 전달
			HttpHeaders headers = new HttpHeaders();
			// 파일 다운로드 받는다~, 이런 이름이로~ 라는 뜻
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+encodedName);
			
			// 파일의 정보?,다운로드정보?, 200상태라는 뜻
			return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			// 400 에러 발생
			return ResponseEntity.badRequest().build();
		}
	}
}
