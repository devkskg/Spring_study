package com.gn.mvc.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.AttachRepository;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.AttachSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachService {
	
	private final BoardRepository boardRepository;
	private final AttachRepository attachRepository;
	
	
	
	@Value("${ffupload.location}")
	private String fileDir;
	
	public AttachDto uploadFile(MultipartFile file) {
		AttachDto dto = new AttachDto();
		try {
			// 1. 정상 파일 여부 확인(없는 파일인지 아닌지)
			if(file == null || file.isEmpty()) {
				throw new Exception("존재하지 않는 파일입니다.");
			}
			// 2. 파일 최대 용량 체크
			// Spring 허용 파일 최대 용량 1MB(1048576byte)
			// byte -> KB -> MB
			long fileSize = file.getSize();
			if(fileSize >= 1048576) {
				throw new Exception("허용 용량을 초과하는 파일입니다.");
			}
			// 3. 파일 최초 이름 읽어오기
			String oriName = file.getOriginalFilename();
			dto.setOri_name(oriName);
			
			// 4. 파일 확장자 자르기
			String fileExt = oriName.substring(oriName.lastIndexOf("."));
//			String fileExt = oriName.substring(oriName.lastIndexOf("."), oriName.length());
			// 5. 파일 명칭 바꾸기
			UUID uuid = UUID.randomUUID();
			// 6. uuid의 8자리마다 반복되는 하이픈 제거
			String uniqueName = uuid.toString().replaceAll("-", "");
			// 7. 새로운 파일명 생성
			String newName = uniqueName + fileExt;
			dto.setNew_name(newName);
			
			// 8. 파일 저장 경로 설정. C:/upload/board/newName
			String downDir = fileDir + "board/" + newName;
			dto.setAttach_path(downDir);
			
			// 9. 파일 껍데기 생성
			File saveFile = new File(downDir);
			// 10. 경로 존재 유무 확인
			if(saveFile.exists() == false) {
				saveFile.mkdirs();
			}
			// 11. 껍데기에 파일 정보 복제(file이 알맹이?)
			file.transferTo(saveFile);
			
		} catch (Exception e) {
			// throw를 만나면 dto가 null로 초기화 된다.
			dto = null;
			e.printStackTrace();
		}
		
		return dto;
	}
	
	public List<Attach> selectAttachList(Long boardNo){
		// Attach Entity가 아닌 Board Entity를 기준으로 조회 해야한다.
		// Attach Entity가 아닌 Board Entity를 기준으로 조회 해야한다.
		// Attach Entity가 아닌 Board Entity를 기준으로 조회 해야한다.
		// 1. boardNo 기준 Board Entity 조회
		Board board = boardRepository.findById(boardNo).orElse(null);
		// 2. Specification 생성(Attach)
		Specification<Attach> spec = (root, query, criteriaBuilder) -> null;
		spec = spec.and(AttachSpecification.boardEquals(board));
		// 3. findAll 메소드에 spec 전달.
		return attachRepository.findAll(spec);
	}
	
	public Attach selectAttachOne(Long attachNo) {
		Attach attach = attachRepository.findById(attachNo).orElse(null);
		return attach;
	}
	
	
	
	// 파일 메타 데이터 삭제
	public int deleteMetaData(Long attach_no) {
		int result = 0;
		try {
			// 지금까지 삭제할때 엔티티를 조회하고 있으면 삭제하고~ 했다.
			// 이유 1. 이미 삭제된 애를 또 삭제하는 것을 방지하기 위해서
			Attach target = attachRepository.findById(attach_no).orElse(null);
			if(target != null) {
				// 여기다 원래 삭제하는 코드를 넣었는 데~ attachRepository.deleteById~~ 이런식으로!
				// 이유 2. Entity를 기준으로 삭제하는 방법이 JPA가 원하는 방향이다!
				attachRepository.delete(target);
			}
			
			
			
			
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 파일 자체 메모리에서 삭제
	public int deleteFileData(Long attach_no) {
		int result = 0;
		try {
			// 있으면 Attach Entity, 없으면 null
			Attach attach = attachRepository.findById(attach_no).orElse(null);
			if(attach != null) {
				// 해당하는 경로의 파일의 객체 생성
				File file = new File(attach.getAttachPath());
				// 파일이 존재하면 삭제
				if(file.exists()) {
					file.delete();
				}
				
			}
			
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
