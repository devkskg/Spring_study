package com.gn.mvc.dto;

import java.time.LocalDateTime;

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
public class MemberDto {
	private Long member_no;
	private String member_id;
	private String member_pw;
	private String member_name;
	private LocalDateTime reg_date;
	private LocalDateTime mod_date;
	
//	MemberDto -> Member(Entity)
	public Member toEntity() {
		return Member.builder()
				.memberId(member_id)
				.memberPw(member_pw)
				.memberName(member_name)
				.memberNo(member_no)
				.build();
	}
//	 Member(Entity) -> MemberDto
	public MemberDto toDto(Member member) {
		return MemberDto.builder()
				.member_id(member.getMemberId())
				.member_pw(member.getMemberPw())
				.member_name(member.getMemberName())
				.member_no(member.getMemberNo())
				.reg_date(member.getRegDate())
				.mod_date(member.getModDate())
				.build();
	}
	

	
}
