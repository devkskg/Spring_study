package com.gn.mvc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.BoardSpecification;

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
	public List<Board> selectBoardAll(SearchDto searchDto){
//		1,2 방법
//		List<Board> list = new ArrayList<Board>();
//		if(searchDto.getSearch_type() == 1) {
//			// 제목 기준 검색
//			list = repository.findByTitleLike(searchDto.getSearch_text());
//		} else if(searchDto.getSearch_type() == 2) {
//			// 내용 기준 검색
//			list = repository.findByContentLike(searchDto.getSearch_text());
//			
//		} else if(searchDto.getSearch_type() == 3) {
//			// 제목+내용 기준 검색
//			list = repository.findByTitleOrContentLike(searchDto.getSearch_text(),searchDto.getSearch_text());
//			
//		} else {
//			// WHERE절 없이 검색(처음 진힙 했을 때)
//			list = repository.findAll();
//		}
//		return list;
		
		
//		3 방법
		Specification<Board> spec = (root, query, criteriaBuilder) -> null;
		if(searchDto.getSearch_type() == 1) {
			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
		} else if(searchDto.getSearch_type() == 2) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		} else if(searchDto.getSearch_type() == 3) {
			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()))
					.or(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		}
		List<Board> list = repository.findAll(spec);
		return list;
		
	}
}
