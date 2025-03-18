package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.AttachRepository;
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
	private final AttachRepository attachRepository;
	
	// 어노테이션 뿐만 아니라 JpaRepository를 상속받은 Interface들은 Bean 스캐닝 없이 사용 가능
	
	
	
//	파일 수업!!!
//	파일 수업!!!
//	파일 수업!!!
//	public BoardDto createBoard(BoardDto dto) {
//		// 1. 매개변수 dto -> entity
//		Board param = dto.toEntity();
//		// 2. Repository의 save() 메소드 호출(insert 쿼리 실행)
//		Board result = repository.save(param);
//		// 3. 결과 entity -> dto로 바꿔서 return
//		return new BoardDto().toDto(result);
//		
//	}
	@Transactional(rollbackFor = Exception.class)
	public int createBoard(BoardDto dto, List<AttachDto> attachList) {
		int result = 0;
		try {
			// 1. Board 엔티티 insert
			Board entity = dto.toEntity();
			// 2. insert 결과로 반환받은 pk를 알아야한다.
			Board saved = repository.save(entity);
//			아래처럼 쓰면 save가 두 번 읽혀서 잘못된 방법이 될 수 있다.
//			Board saved = repository.save(entity).getBoardNo();
			Long boardNo = saved.getBoardNo();
			// 3. attachList에 데이터가 있는 경우
			if(attachList.size() != 0) {
				// 4. Attach 엔티티 insert
				for(AttachDto attachDto : attachList) {
					attachDto.setBoard_no(boardNo);
					Attach attachEntity = attachDto.toEntity(attachDto);
					Attach attachSaved = attachRepository.save(attachEntity);
				}
			}
			
			
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
//	파일 수업!!!
//	파일 수업!!!
//	파일 수업!!!
	
	
	
	
//	public List<Board> selectBoardAll(SearchDto searchDto){
//		public Page<Board> selectBoardAll(SearchDto searchDto, ){
//		페이징 파라미터 추가 작업!
//		public Page<Board> selectBoardAll(SearchDto searchDto, int nowPage){
//		PageDto 추가!
		public Page<Board> selectBoardAll(SearchDto searchDto, PageDto pageDto){
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
		
		
//		정렬 관련 코드, Pageable을 사용하기 위해 주석한다.
//		Sort sort = Sort.by("regDate").descending();
//		if(searchDto.getOrder_type() == 2) {
//			sort = Sort.by("regDate").ascending();
//		}
		
//		Pageable!!(시작하는 데이터 번호, 한 페이지에 몇개씩 보여줄 것인가, Sorting 관련 코드)
//		nowPage - 1 하는 이유. JPA Paging은 0번으로 시작한다고 생각 함. 하지만 사용자 입장에서는 1번이 시작이다. 그 차이를 메꾸는 작업
		Pageable pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").descending());
		if(searchDto.getOrder_type() == 2) {
			pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").ascending());
		}
		
		
//		3 방법 + 페이징시 추가 import 받아야한다.
		Specification<Board> spec = (root, query, criteriaBuilder) -> null;
		if(searchDto.getSearch_type() == 1) {
			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
		} else if(searchDto.getSearch_type() == 2) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		} else if(searchDto.getSearch_type() == 3) {
			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()))
					.or(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		}
//		List<Board> list = repository.findAll(spec, sort);
//		페이징 추가!
		Page<Board> list = repository.findAll(spec, pageable);
		return list;
		
	}
		public Board selectBoardOne(Long idBoardNo01) {
//			Board 형태로 가지고 올건데 가져올 게 없다면 null로 반환해라. 라는 뜻이다.
			return repository.findById(idBoardNo01).orElse(null);
		}
		
		public Board updateBoard(BoardDto param) {
			Board result = null;
//			1. @Id를 쓴 필드를 기준으로 타겟 조회
			Board target = repository.findById(param.getBoard_no()).orElse(null);
//			2. 타겟이 존재하는 경우 업데이트
			if(target != null) {
				result = repository.save(param.toEntity());
			}
			return result;
		}
		
		public int deleteBoard(Long id) {
//			repository.deleteById(id);
//			return repository.findById(id).orElse(null);
			
//			위는 내가 짠 코드인데 누군가가 이미 삭제했다면 오류 뜰 가능성이 높음
//			아래처럼 해야 예외 상황 막고 효율적인 코드가 된다.
			int result = 0;
			try {
				Board target = repository.findById(id).orElse(null);
				if(target != null) {
					repository.deleteById(id);
				}
				result = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
}
