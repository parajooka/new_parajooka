package com.paraframework.object;

public class MenuHtml {
	private int id;
	private int menu_idx;
	private String menu_html;
	private String mobile_menu_html = "";
	
	public String getMobile_menu_html() {
		return mobile_menu_html;
	}
	public void setMobile_menu_html(String mobile_menu_html) {
		this.mobile_menu_html = mobile_menu_html;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMenu_idx() {
		return menu_idx;
	}
	public void setMenu_idx(int menu_idx) {
		this.menu_idx = menu_idx;
	}
	public String getMenu_html() {
		return menu_html;
	}
	public void setMenu_html(String menu_html) {
		this.menu_html = menu_html;
	}
	
	
	
}
