package com.gn.todo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gn.todo.Dto.AttachDto;
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
	
}
