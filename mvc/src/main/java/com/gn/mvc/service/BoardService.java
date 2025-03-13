package com.gn.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
//	롬복이 대신 해줌! @RequiredArgsConstructor
//	@Autowired
//	BoardRepository repository;
//	롬복이 대신 해줌! @RequiredArgsConstructor
	private final BoardRepository repository;
	
	// 어노테이션 뿐만 아니라 JpaRepository를 상속받은 Interface들은 Bean 스캐닝 없이 사용 가능
	
	
	public BoardDto createBoard(BoardDto dto) {
		// 1. 매개변수 dto -> entity
		Board param = dto.toEntity();
		// 2. Repository의 save() 메소드 호출(insert 쿼리 실행)
		Board result = repository.save(param);
		// 3. 결과 entity -> dto로 바꿔서 return
		return new BoardDto().toDto(result);
		
	}
	
}
