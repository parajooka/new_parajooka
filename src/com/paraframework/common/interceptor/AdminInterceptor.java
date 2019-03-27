 package com.paraframework.common.interceptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.object.AccessIp;
import com.paraframework.object.Category;
import com.paraframework.object.Homepage;
import com.paraframework.service.AccessIpService;
import com.paraframework.service.CategoryService;
import com.paraframework.service.HomepageService;
import com.paraframework.service.MenuService;

public class AdminInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private AccessIpService service;
	@Autowired
	private CategoryService category_service;
	@Autowired
	private HomepageService homepage_service;
	@Autowired
	private MenuService menu_service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		/* 로직처리 */ 
		String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        } 
        
		List<AccessIp> access = service.getAccessIp();
		
		if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
			initAdminCategory(request);
			return true;
		}
		
		for (AccessIp a : access) {
			if (ip.equals(a.getAccess_ip()) || ip.indexOf(a.getAccess_ip()) >= 0) {
				if (!ControllerCommonMethod.isMobile(request)) {
					initAdminCategory(request);
					return true;
				} else {
					return ControllerCommonMethod.alertMessage("모바일에서는 관리자 페이지 접근이 불가능합니다.", request, response);
				}
			}
		}
		
		//홈페이지 메뉴 로딩
		if (!ControllerCommonMethod.MenuUpload) {
			ControllerCommonMethod.MenuUpload = true;
			request.getServletContext().setAttribute("menu_list", menu_service.getViewMenu());
		}
		
		//홈페이지 정보 호출
		if (!ControllerCommonMethod.HomePageUpload) {
			Homepage homepage = homepage_service.getHomepage();
			
			if (homepage != null) {
				ControllerCommonMethod.HomePageUpload = true;
				request.getServletContext().setAttribute("homepage", homepage);
			}
		}
		
		return ControllerCommonMethod.alertMessage("허용되지않은 접근입니다.", request, response);
	}
	
	public void initAdminCategory(HttpServletRequest request) {
		if (!ControllerCommonMethod.AdminCategoryUpload) {
			request.getServletContext().removeAttribute("admin_Category_list");
			
			ControllerCommonMethod.AdminCategoryUpload = true;
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
			

			request.getServletContext().setAttribute("admin_Category_list", category_tree);
		}
	}
}
