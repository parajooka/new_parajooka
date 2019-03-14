package com.paraframework.common;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {
	private int page = 1;
	private int limit = 5;
	private int limit_count;
	private int total_page;
	private List<Integer> view_page = new ArrayList<Integer>(); // view에서 보여줄 페이지 리스트
	private int total_item_count; // 등록된 아이템 갯수
	private String paging_url;
	private int numberic = 1;
	
	public int getNumberic() {
		return numberic;
	}

	public void setNumberic() {
		this.numberic = (this.page * this.limit) - this.limit + 1;
	}

	public PageUtil() {}
	
	public String getPaging_url() {
		return paging_url;
	}

	public void setPaging_url(String paging_url) {
		this.paging_url = paging_url;
	}

	public List<Integer> getView_page() {
		return view_page;
	}

	public void setView_page(List<Integer> view_page) {
		this.view_page = view_page;
	}

	public int getTotal_item_count() {
		return total_item_count;
	}

	public void setTotal_item_count(int total_item_count) {
		this.total_item_count = total_item_count;
	}

	public int getTotal_page() {
		return total_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public int getLimit_count() {
		return limit_count;
	}

	public void setLimit_count(int limit_count) {
		this.limit_count = limit_count;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		refresh_view_paging();
	}
	
	public int getLimits() {
		return page * limit - limit;
	}
	
	/**
	 * 몇페이지까지 있는가, 가져와야할 아이템의 시작번호/끝번호 지정, 뷰페이지에서 보여줄 페이징 넘버 지정
	 * @param count 총 처리해야할 아이템의 갯수
	 */
	public void setPaging() {
		int page = 0;
		int count = this.total_item_count;
		
		if (count/getLimit() <= 1 && (count%getLimit() == 0) || count/getLimit() < 1) {
			page = 1;
		} else {
			page = (count%getLimit() > 0) ? (int)count/getLimit() + 1 : (int)count/getLimit();
		}
		
		setTotal_page(page);//총 몇페이지까지 있는지.
	}
	
	/**
	 * 페이징 중에서 사용자에게 보여질 페이지만을 골라내는 메서드
	 */
	public void refresh_view_paging() { 
		int begin = 0;
		int end = 0;
		
		if (this.page <	 7) { //7페이지 이전까지는 1 ~ 10페이지까지 보여준다.
			begin = 1;
			end = 10;
		} else if (total_page - page < 4) { 
			begin = total_page - 9; // 남은 페이지가 4개 미만일때 시작 : 마지막 페이지에서 9를 뺀 페이지
			end = total_page; // 남은 페이지가 4개 미만일때 종료 : 마지막 페이지
		} else { // 현재 페이지가 7페이지 이상이고 남은 페이지가 4개 이상일 경우
			begin = page - 5; // 시작 : 현재 페이지 - 5
			end = page + 4; // 종료 : 현재 페이지 + 4
		}
		
		this.view_page.clear(); // List초기화
		
		//시작번호가 1보다 작을경우 1로 고정
		begin = (begin < 1) ? 1 : begin;
		for (int i = begin; i <= end; i++) { // 페이징을 담기 시작
			if (i > total_page) {
				break;
			}
			this.view_page.add(i);
		}
	}

	@Override
	public String toString() {
		return "PageUtil [page=" + page + ", limit=" + limit + ", limit_count=" + limit_count + ", total_page="
				+ total_page + ", view_page=" + view_page + ", total_item_count=" + total_item_count + ", paging_url="
				+ paging_url + ", numberic=" + numberic + "]";
	}
	
	
	
	
	
}
