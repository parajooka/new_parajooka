package com.para.object.workboard;

import java.sql.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class WorkMenu {
	private int menu_idx; //메뉴 고유 아이디
	private Date add_date = new Date(new java.util.Date().getTime());
	
	@NotNull(message="그룹아이디를 입력해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	private int group_idx; //메뉴 최상위 아이디
	
	@NotNull(message="메뉴명을 입력해주세요.")
	@Size(min=2, max=20, message="메뉴명은 최소 2글자에서 최대 20글자입니다.")
	private String menu_name; //메뉴명
	
	private Date modify_date  = new Date(new java.util.Date().getTime());
	
	@NotNull(message="상위 메뉴의 아이디를 입력해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	private int parent_menu_idx; //부모의 아이디
	
	@NotNull
	@Min(value=0, message="출력 순서는 최소 0입니다.")
	private int print_seq = 0; //출력순서
	
	@NotNull(message="출력 여부를 선택 해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=1, message="잘못된 접근입니다.")
	private int view_yn; //출력여부
	
	@NotNull(message="사용 여부를 선택 해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=1, message="잘못된 접근입니다.")
	private int use_yn;

	public int getMenu_idx() {
		return menu_idx;
	}

	public void setMenu_idx(int menu_idx) {
		this.menu_idx = menu_idx;
	}

	public Date getAdd_date() {
		return add_date;
	}

	public void setAdd_date(Date add_date) {
		this.add_date = add_date;
	}

	public int getGroup_idx() {
		return group_idx;
	}

	public void setGroup_idx(int group_idx) {
		this.group_idx = group_idx;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}

	public int getParent_menu_idx() {
		return parent_menu_idx;
	}

	public void setParent_menu_idx(int parent_menu_idx) {
		this.parent_menu_idx = parent_menu_idx;
	}

	public int getPrint_seq() {
		return print_seq;
	}

	public void setPrint_seq(int print_seq) {
		this.print_seq = print_seq;
	}

	public int getView_yn() {
		return view_yn;
	}

	public void setView_yn(int view_yn) {
		this.view_yn = view_yn;
	}

	public int getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(int use_yn) {
		this.use_yn = use_yn;
	}
}
