package com.para.object.workboard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class WorkPost {
	private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
	
	private int post_id;
	@Min(value = 1, message = "게시글을 작성할 메뉴를 선택 해주세요.")
	private int menu_idx;
	
	private String post_password;
	
	@NotNull (message = "제목을 입력 해주세요.")
	@Size(min = 1, max = 50, message = "제목은 최소 1글자에서 최대 50글자까지 입력 가능합니다.")
	private String post_title;
	
	@NotNull (message = "내용을 입력 해주세요.")
	@Size(min = 10, message = "글 내용을 최소 10글자 이상 입력 해주세요.")
	private String post_contents;
	
	@NotNull (message = "작성자를 입력 해주세요.")
	@Pattern(regexp = "^[가-힣]*$", message = "이름에는 한글만 입력 가능합니다.")
	@Size(min = 2, message = "이름은 최소 2글자 이상입니다.")
	private String writter;
	
	private String write_date = formatTime.format(new Date());
	private String modify_date = formatTime.format(new Date());
	
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public String getPost_password() {
		return post_password;
	}
	public void setPost_password(String post_password) {
		this.post_password = post_password;
	}
	public String getPost_title() {
		return post_title;
	}
	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}
	public String getPost_contents() {
		return post_contents;
	}
	public void setPost_contents(String post_contents) {
		this.post_contents = post_contents;
	}
	public String getWritter() {
		return writter;
	}
	public void setWritter(String writter) {
		this.writter = writter;
	}
	public String getModify_date() {
		return modify_date;
	}
	public void setModify_date(String modify_date) {
		this.modify_date = modify_date;
	}
	public int getMenu_idx() {
		return menu_idx;
	}
	public void setMenu_idx(int menu_idx) {
		this.menu_idx = menu_idx;
	}
	public String getWrite_date() {
		return write_date;
	}
	public void setWrite_date(String write_date) {
		this.write_date = write_date;
	}
}
