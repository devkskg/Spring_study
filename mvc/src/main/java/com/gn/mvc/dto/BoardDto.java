package com.gn.mvc.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gn.mvc.entity.Board;
import com.gn.mvc.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BoardDto {
	private Long board_no;
	private String board_title;
	private String board_content;
	private LocalDateTime reg_date;
	private LocalDateTime mod_date;
//	화면 단에서 받을 수 있도록! create.html에서 hidden으로 설정한 value가 넘어온다.
	private Long board_writer;
//	스프링에서 쓰이는 	
	private List<MultipartFile> files;
	
	// 1. BoardDto -> Board(Entity)
	public Board toEntity() {
		return Board.builder()
				.boardTitle(board_title)
				.boardContent(board_content)
				.boardNo(board_no)
//				board_writer와 memberNo가 매칭 + Member
				.member(Member.builder().memberNo(board_writer).build())
				.build();
	}
	
	
	// 2. Board(Entity) -> BoardDto
	public BoardDto toDto(Board board) {
		return BoardDto.builder()
				.board_title(board.getBoardTitle())
				.board_content(board.getBoardContent())
				.board_no(board.getBoardNo())
				.reg_date(board.getRegDate())
				.mod_date(board.getModDate())
				.build();
	}
}
