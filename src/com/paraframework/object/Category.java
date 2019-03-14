package com.paraframework.object;

import java.sql.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Category {
	private int category_idx; //카테고리 고유 아이디
	private Date add_date = new Date(new java.util.Date().getTime());
	
	@NotNull(message="그룹아이디를 입력해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	private int group_idx; //카테고리 최상위 아이디
	
	@NotNull(message="카테고리명을 입력해주세요.")
	@Size(min=2, max=20, message="카테고리명은 최소 2글자에서 최대 20글자입니다.")
	private String category_name; //카테고리명
	
	private Date modify_date  = new Date(new java.util.Date().getTime());
	
	@NotNull(message="상위 카테고리의 아이디를 입력해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	private int parent_category_idx; //부모의 아이디
	
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
	
	@NotNull(message="해당 카테고리의 url을 입력해주세요.")
	@Size(min=1, message="정확한 url을 입력해주세요.")
	private String category_url;

	public int getCategory_idx() {
		return category_idx;
	}

	public void setCategory_idx(int category_idx) {
		this.category_idx = category_idx;
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

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}

	public int getParent_category_idx() {
		return parent_category_idx;
	}

	public void setParent_category_idx(int parent_category_idx) {
		this.parent_category_idx = parent_category_idx;
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
	

	public String getCategory_url() {
		return category_url;
	}

	public void setCategory_url(String category_url) {
		this.category_url = category_url;
	}

	@Override
	public String toString() {
		return "Category [category_idx=" + category_idx + ", add_date=" + add_date + ", group_idx=" + group_idx
				+ ", category_name=" + category_name + ", modify_date=" + modify_date + ", parent_category_idx="
				+ parent_category_idx + ", print_seq=" + print_seq + ", view_yn=" + view_yn + ", use_yn=" + use_yn
				+ ", category_url=" + category_url + "]";
	}
	
	
	
	
	
}
