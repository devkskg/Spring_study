package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;


@Controller

//이만큼을 Lombok이 대신 만들어줌!!! 아래 보자!!
@RequiredArgsConstructor
//이만큼을 Lombok이 대신 만들어줌!!! 아래 보자!!
public class BoardController {
//	로그 레벨 확인해보자!
	private Logger logger = LoggerFactory.getLogger(BoardController.class);
//	로그 레벨 확인해보자!
	
	
	
	
//	1. 필드 주입 -> 순환 참조
//	@Autowired
//	BoardService service;
	
//	2. 메소드(Setter) 주입 -> 불변성 보장X
//	private BoardService service;
//	@Autowired
//	public void setBoardService(BoardService service) {
//		this.service = service;
//	}
	
//	3. 생성자 주입 + final -> 순환 참조X, 불변성 보장O 이게 제일 좋다!
	private final BoardService service;
	
//	이만큼을 Lombok이 대신 만들어줌!!! 위에 보자!!
//	@Autowired
//	public BoardController(BoardService service) {
//		this.service = service;
//	}
//	이만큼을 Lombok이 대신 만들어줌!!! 위에 보자!!
	
	@GetMapping("/board/create")
	// 화면 전환이니 view라고 명명
	public String createBoardView() {
		return "/board/create";
	}
	
	
	// JS -제이슨-RequestBoty-Map-Board.>
	// 두 태그가 같이 있어야 좋다
	@PostMapping("/board")
	@ResponseBody // 요청의 상단부에 사용하면 응답 데이터가 JSON 형태로 반환되어 전달. 필쑤!
	public Map<String, String> createBoardApi(
			// 스프링에서 제이슨을 주고 받을 때 return을 제이슨이라고 하지 않는다.
			// form 데이터 받을 때 3가지 방법. 
			// 1. @RequestParam 하나의 데이터당 하나의 변수로 잔달
//			@RequestParam("board_title") String boardTitle,
//			@RequestParam("board_content") String boardContent
			// 2. @RequestParamMap 형태로 전달
//			@RequestParam Map<String, String> param
			// 3. 클래스 생성하여 객체 형태로 정보를 전달.
			// BoardDto dto
			// 위의 1,2,3 이 모두 같은 결과다.
			
			BoardDto dto
			
			) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 등록중 오류가 발생하였습니다.");
		// 매개변수 dto -> entity
//		Board parm = Board.builder()
//				.boardTitle(dto.getBoard_title())
//				.boardContent(dto.getBoard_content())
//				.build();
		
				

		// Service가 가지고 있는 createBoard 호출
		// bean 스캐닝 해서 가져와서 쓸거다.
		BoardDto result = service.createBoard(dto);
		
		logger.debug("1 : " + result.toString());
		logger.info("2 : " + result.toString());
		logger.warn("3 : " + result.toString());
		logger.error("4 : " + result.toString());
		
		
		if(result != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글 등록이 완료되었습니다.");
		}
		return resultMap;
		
	}
	
//	게시판 들어갈 때 + 검색
	@GetMapping("/board")
//	public String selectBoardAll(Model model, SearchDto searchDto) {
//		페이징 관련 파라미터 추가!	
//		public String selectBoardAll(Model model, SearchDto searchDto, @RequestParam(name="nowPage", defaultValue="1") int nowPage) {
//		PageDto사용하자!
		public String selectBoardAll(Model model, SearchDto searchDto, PageDto pageDto) {
		
//		PageDto 추가!
		if(pageDto.getNowPage() == 0) {
			pageDto.setNowPage(1);
		}
		
		// 1. DB에서 목록 SELECT
//		List<Board> resultList = service.selectBoardAll(searchDto);
//		페이징 사용!
//		Page<Board> resultList = service.selectBoardAll(searchDto);
//		페이징 파라미터 추가!
		Page<Board> resultList = service.selectBoardAll(searchDto, pageDto);
//		와 아래 부분 코드 생각 해봐야함.
		pageDto.setTotalPage(resultList.getTotalPages());
//		PageDto 추가했을때 추가로 보내줘야함
		model.addAttribute("pageDto", pageDto);
		
		
		// 2. 목록 Model에 등록
		model.addAttribute("boardList", resultList);
		model.addAttribute("searchDto", searchDto);
		
		
		// 3. list.html에 데이터 셋팅
		return "board/list";
	}
	
	
	
}
