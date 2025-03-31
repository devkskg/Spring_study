package com.gn.todo.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gn.todo.Dto.AttachDto;
import com.gn.todo.Entity.Attach;
import com.gn.todo.service.AttachService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AttachController {
	
	private final AttachService attachService;

	// 1. url : /attach/create
	// 2. 응답 : JSON(Map<String, String>)
	// 3. 메소드명 : createAttachApi
	// 4. 매개변수 : List<MultipartFile>
	@PostMapping("/attach/create")
	@ResponseBody
	@Transactional
	public Map<String, String> createAttachApi(@RequestParam("files") List<MultipartFile> files){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "파일이 등록에 실패했습니다.");
		System.out.println(files.isEmpty());
		System.out.println(files.size());
		try {
			for(MultipartFile mf : files) {
				// System.out.println(mf.getOriginalFilename());
				// 1. 파일 자체 업로드
				AttachDto dto = attachService.uploadFile(mf);
				// 2. 파일 데이터베이스 저장
				if(dto != null) {
					attachService.createAttach(dto);
				}
				
			}
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "파일이 등록에 성공했습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Object> fileDownload(@PathVariable("id") Long id){
		try {
			Attach fileData = attachService.selectAttachOne(id);
			if(fileData == null) {
				// 너가 보내준 id 조회해보니까 없더라~ = notFound
				return ResponseEntity.notFound().build();
			}
			// 파일 정보를 꺼내오고 resource에 담아줘서 보낼 거다~
			Path filePath = Paths.get(fileData.getAttachPath());
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			// 사용자는 바뀐 이름이 아니라 원래 이름을 가지고 오게한다.
			String oriFileName = fileData.getOriName();
			String encodedName = URLEncoder.encode(oriFileName,StandardCharsets.UTF_8);
			// 브라우저에게 알려주기
			HttpHeaders headers = new HttpHeaders();
			// 파일 다운로드 받을 것이다~ 이런이름으로~
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+encodedName);
			
			return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
}
