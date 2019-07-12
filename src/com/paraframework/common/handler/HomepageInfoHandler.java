	package com.paraframework.common.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.paraframework.object.Category;
import com.paraframework.object.Homepage;
import com.paraframework.service.CategoryService;
import com.paraframework.service.HomepageService;
import com.paraframework.service.MenuService;

public class HomepageInfoHandler {
	@Autowired
	private CategoryService category_service;
	@Autowired
	private HomepageService homepage_service;
	@Autowired
	private MenuService menu_service;
	
	public HomepageInfoHandler() {
		//해당 클래스의 객체가 생성될때 Autowired를 채워준다.
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	public void UploadCustomerMenu(ServletContext request) {
		//context에 메뉴정보 업로드
		if (request.getAttribute("menu_list") != null) {
			request.removeAttribute("menu_list");
		}
		
		request.setAttribute("menu_list", menu_service.getViewMenu());
	}
	
	public Homepage UploadHomepageObject(ServletContext request) {
		if (request.getAttribute("homepage") != null) {
			request.removeAttribute("homepage");
		}
		
		//홈페이지 정보 호출
		Homepage homepage = homepage_service.getHomepage();
		
		if (homepage != null) {
			request.setAttribute("homepage", homepage);
		}
		
		return homepage;
	}
	
	public void UploadAdminMenu(ServletContext request) {
		List<Category> category_list = category_service.getViewCategory();
		Map<Integer, List<Category>> category_tree = new LinkedHashMap<>();
		
		for (Category c : category_list) {
			List<Category> categories = category_tree.get(c.getParent_category_idx()); 
			if (categories != null) {
				categories.add(c);
			} else {
				categories = new ArrayList<>();
				categories.add(c);
				category_tree.put(c.getParent_category_idx(), categories);
			}
		}
		
		if (request.getAttribute("admin_Category_list") != null) {
			request.removeAttribute("admin_Category_list");
		}

		request.setAttribute("admin_Category_list", category_tree);
	}
}
