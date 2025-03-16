package com.gn.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gn.mvc.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board> {
	
//	3. Specification 사용 / 오버로딩 했다~ 이걸 서비스의 selectBoardAll 에서 썼다.
	List<Board> findAll(Specification<Board> spec);
	
	// 1. 메소드 네이밍. findBy + 필드명 + 키워드 형태
//	List<Board> findByBoardTitleContaining(String keyword);
//	List<Board> findByBoardContentContaining(String keyword);
//	List<Board> findByBoardTitleContainingOrBoardContentContaining(String titleKeyword, String contentkeyword);
	
	// 2. JPQL 이용
	// 엔티티를 기준으로 하는 거라서 * 대신 별칭 정한 b라고 한다.
	@Query(value="SELECT b FROM Board b WHERE b.boardTitle LIKE CONCAT('%',?1,'%')") // :을 쓰는 경우 매개변수 이름 맞춰줘야한다. 
//	@Query(value="SELECT b FROM Board b WHERE b.boardTitle LIKE CONCAT('%',?1,'%')")
	List<Board> findByTitleLike(String keyword);
	
	@Query(value="SELECT b FROM Board b WHERE b.boardContent LIKE CONCAT('%',?1,'%')")
	List<Board> findByContentLike(String keyword01);
	
	@Query(value="SELECT b FROM Board b WHERE b.boardTitle LIKE CONCAT('%',?1,'%') OR b.boardContent LIKE CONCAT('%',?2,'%')")
	List<Board> findByTitleOrContentLike(String title, String content);
	
	
	
	
	
	
	
}
