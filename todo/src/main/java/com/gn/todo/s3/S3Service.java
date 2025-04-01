package com.gn.todo.s3;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.gn.todo.Dto.AttachDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	
	private final AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	// S3 서비스와 연결하기
	public S3Object getS3Object(String fileName) {
		// aws에서 제공하는 파일 객체 정보를 가져오는 것
		S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, fileName));
		return s3Object;
	}
	
	public AttachDto uploadFile(MultipartFile file) {
		AttachDto dto = new AttachDto();
		// 파일 있는지 확인
		try(InputStream is = file.getInputStream()) {
			if(file == null || file.isEmpty()) {
				throw new Exception("존재하지 않는 파일입니다.");
			}
			// 파일 용량 체크
			long fileSize = file.getSize();
			if(fileSize >= 1048576) {
				throw new Exception("허용 용량을 초과한 파일입니다.");
			}
			
			// 파일 최초 이름
			String oriName = file.getOriginalFilename();
			dto.setOri_name(oriName);
			
			// 파일 확장자
			String fileExt = oriName.substring(oriName.lastIndexOf("."));
			
			// 새로운 파일 이름 만들기
			UUID uuid = UUID.randomUUID();
			String uniqueName = uuid.toString().replaceAll("-", "");
			String newName = uniqueName + fileExt;
			dto.setNew_name(newName);
			
			// S3 사용해서 넣기. 객체 형태로 세팅
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(file.getContentType());
			meta.setContentLength(file.getSize());
			
			// S3가 만들어준 객체의 도움을 받아서
			// 버킷정보, 새로운 이름, InputStream, 메타데이터
			// withCannedAcl(CannedAccessControlList.PublicRead) 바깥쪽에서 접근할수있게 하겠다? 바깥쪽 = 자바
			amazonS3.putObject(new PutObjectRequest(bucket, newName, is, meta)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			
			// AttachPath 지정
			String region = amazonS3.getRegionName();
			// file이름이 저장될때 url 형태로 저장이 된다. ex)"https://kgn-bucket.s3.ap-northeast-2.amazonaws.com/파일명" < 이거 전체가 파일 url 이다.
			String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + newName; 
			dto.setAttach_path(fileUrl);
			
		} catch (Exception e) {
			dto = null;
			e.printStackTrace();
		}
		return dto;
	}
	
}
