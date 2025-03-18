package com.gn.mvc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gn.mvc.entity.Board;

import lombok.RequiredArgsConstructor;

@SpringBootTest
class BoardServiceTest {
	
	@Autowired
	private BoardService service;
	
	@Test
	void selectBoardOne_success() {
		// 1. 예상 데이터
		Long id = 4L;
		Board expected = Board.builder().boardTitle("제목1").build();
		// 2. 실제 데이터
		Board real = service.selectBoardOne(id);
		// 3. 비교 및 검증
		assertEquals(expected.getBoardTitle(), real.getBoardTitle());
	}
	
	// 실패 테스트
	// 존재하지 않는 PK기준으로 조회 요청
	@Test
	void selectBoardOne_fail() {
		// 1. 실패 예상 PK 데이터
		Long id = 100000000000000L;
		// 2. 실패 예상 PK를 사용한 데이터 조회
		Board expected = service.selectBoardOne(id);
		// 3. 비교 및 검증
		assertEquals(expected, null);
		
		// 밑에는 강사님 방법
//		Long id = 100000000000000L;
//		Board expected = null;
//		Board real = service.selectBoardOne(id);
//		assertEquals(expected, real);

	}
	

}
