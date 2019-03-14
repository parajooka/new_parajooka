package com.paraframework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraframework.dao.MenuHtmlDao;
import com.paraframework.object.MenuHtml;

@Service
public class MenuHtmlService {
	@Autowired
	MenuHtmlDao dao;

	public int insertHTML(MenuHtml menu_html) {
		// TODO Auto-generated method stub
		return dao.insertHTML(menu_html);
	}

	public int updateHTML(MenuHtml menu_html) {
		// TODO Auto-generated method stub
		return dao.updateHTML(menu_html);
	}

	public int updateMobileHTML(MenuHtml menu_html) {
		// TODO Auto-generated method stub
		return dao.updateMobileHTML(menu_html);
	}

	public MenuHtml getHTMLById(int id) {
		// TODO Auto-generated method stub
		return dao.getHTMLById(id);
	}

	public MenuHtml getHTMLByMenuId(int id) {
		// TODO Auto-generated method stub
		return dao.getHTMLByMenuId(id);
	}

}
