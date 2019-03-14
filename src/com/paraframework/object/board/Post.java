package com.paraframework.object.board;

import java.sql.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Post {
	private int post_id;
	private int menu_id;
	
	@NotNull
	@Size(min=1, max=100, message="게시글 제목은 최소 1글자에서 최대 100글자까지 가능합니다.")
	private String title;
	
	@NotNull
	@Size(min=1, message="게시글 내용을 최소 1글자이상 입력해주세요.")
	private String contents;
	
	@NotNull
	@Size(min=1, message="모바일 게시글 내용을 최소 1글자이상 입력해주세요.")
	private String mobile_contents;
	
	@NotNull
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=1, message="잘못된 접근입니다.")
	private int comment_acc;
	private int view_count = 0;
	private String main_img;
	private Date write_date = new Date(new java.util.Date().getTime());
	private Date modify_date = new Date(new java.util.Date().getTime());
	private String explanation;
	
	
	public String getMobile_contents() {
		return mobile_contents;
	}
	public void setMobile_contents(String mobile_contents) {
		this.mobile_contents = mobile_contents;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getMain_img() {
		return main_img;
	}
	public void setMain_img(String main_img) {
		this.main_img = main_img;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getComment_acc() {
		return comment_acc;
	}
	public void setComment_acc(int comment_acc) {
		this.comment_acc = comment_acc;
	}
	public int getView_count() {
		return view_count;
	}
	public void setView_count(int view_count) {
		this.view_count = view_count;
	}
	public Date getWrite_date() {
		return write_date;
	}
	public void setWrite_date(Date write_date) {
		this.write_date = write_date;
	}
	public Date getModify_date() {
		return modify_date;
	}
	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
	
	
}
