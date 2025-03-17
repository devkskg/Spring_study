package com.gn.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
//	setter는 우리가 커스텀하는 부분이 필요해서 Lombok 안 썼다.
public class PageDto {
	
//	한 페이지당 보이는 데이터 개수
	private int numPerPage = 2;
//	현재 페이지, 전에는 1로 세팅 했었나?
	private int nowPage;
	
//	페이징바
	private int pageBarSize = 3;
	private int pageBarStart;
	private int pageBarEnd;
	
//	이전 , 다음 여부
	private boolean prev = true;
	private boolean next = true;
	
//	nowPage 전달 받을 거다
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	
	private int totalPage;
	
//	totalPage 전달 받을 거다
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		calcPaging();
	}
	
	
//	우리가 로직 써야하는 부분
	private void calcPaging() {
//		페이징바 시작 번호 계산
//		내가 있는 페이지가 6이면 6 - 1 = 5 -> 5 /5 = 1 -> ??
		pageBarStart = ((nowPage - 1) / pageBarSize) * pageBarSize + 1;
//		페이징바 끝 번호 계산
		pageBarEnd = pageBarStart + pageBarSize - 1;
		if(pageBarEnd > totalPage) {
			pageBarEnd = totalPage;
		}
//		이전, 이후 버튼이 보이는지 여부
		if(pageBarStart == 1) {
			prev = false;
		}
		if(pageBarEnd >= totalPage) {
			next = false;
		}
		
	}
	
	
}
