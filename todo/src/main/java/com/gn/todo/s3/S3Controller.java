package com.gn.todo.s3;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.gn.todo.Dto.AttachDto;
import com.gn.todo.Entity.Attach;
import com.gn.todo.service.AttachService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {
	
	private final S3Service s3Service;
	private final AttachService attachService;

	@PostMapping("/upload")
	@ResponseBody
	public Map<String, String> uploadFile(@RequestParam("files") List<MultipartFile> files){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "파일이 등록에 실패했습니다.");
		
		try {
			for(MultipartFile mf : files) {
				// 1. 업로드
				AttachDto dto = s3Service.uploadFile(mf);
				// 2. 메타 데이터 insert
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
	public ResponseEntity<Object> downloadFile(@PathVariable("id") Long id){
		try {
			// 1. 파일 정보 조회
			Attach fileData = attachService.selectAttachOne(id);
			if(fileData == null) {
				return ResponseEntity.notFound().build();
			}
			
			// 2. S3 서비스와 연결하기
			// S3가 알고있는 이름을 알려줘야하기 때문에 uuid를 보내줘야한다.
			S3Object s3Object = s3Service.getS3Object(fileData.getNewName());
			
			// 3. S3 서비스에서 컨텐츠 정보 가져오기 - 현재 정보는 java가 이해 못하는 형태 그걸 변환시켜주는 것
			InputStream inputStream = s3Object.getObjectContent();
			
			// 4. 파일 데이터를 바이트 배열로 변환 - S3에 있는 정보를 Java로 가져오는 중이다.
			byte[] fileBytes = inputStream.readAllBytes();
			
			// 5. 기존 파일 명칭 세팅을 해줘야한다.
			String oriFileName = fileData.getOriName();
			String encodedName = URLEncoder.encode(oriFileName, StandardCharsets.UTF_8);
			
			// 6. 브라우저한테 보내주기(브라우저한테 보내줄때는 HttpHeader로 보내준다.)
			HttpHeaders headers = new HttpHeaders();
			// 이미지, 파일, 엑셀 등 전부를 넣을 수 있기 때문에 현재 S3파일 오브젝트의 파일 종류를 브라우저에게 알려줘야한다.
			// (s3Object.getObjectMetadata().getContentType() 컨텐트 타입을 미디어에게 알려주면~
			// 미디어는 브라우저에게? 자바에게? 알려준다..?
//			전에는 이런 형태로 했었는데 똑같은 거다! 형태만 다른 것!
//			전에는 이런 형태로 했었는데 똑같은 거다! 형태만 다른 것!
//			전에는 이런 형태로 했었는데 똑같은 거다! 형태만 다른 것!
//			HttpHeaders headers = new HttpHeaders();
//			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+encodedName);
//			전에는 이런 형태로 했었는데 똑같은 거다! 형태만 다른 것!
//			전에는 이런 형태로 했었는데 똑같은 거다! 형태만 다른 것!
//			전에는 이런 형태로 했었는데 똑같은 거다! 형태만 다른 것!
			headers.setContentType(MediaType.parseMediaType(s3Object.getObjectMetadata().getContentType()));
			headers.setContentDispositionFormData("attachment", encodedName);
			headers.setContentLength(fileBytes.length);
			
			// 7. ResponseEntity에 파일 데이터 반환
			return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
}
