package com.paraframework.dao;


import com.paraframework.object.MenuHtml;

public interface MenuHtmlDao {
	public int insertHTML(MenuHtml menu_html);
	
	public int updateHTML(MenuHtml menu_html);
	
	public int updateMobileHTML(MenuHtml menu_html);
	
	public MenuHtml getHTMLById(int id);
	
	public MenuHtml getHTMLByMenuId(int id);
}
