package com.paraframework.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paraframework.common.BaseController;
import com.paraframework.object.Homepage;
import com.paraframework.object.Menu;
import com.paraframework.service.MenuHtmlService;
import com.paraframework.service.MenuService;

@Controller
public class CustomerMenuController extends BaseController {
	@Autowired
	private MenuService service;
	@Autowired
	private MenuHtmlService html_service;
	
	@Override
	public String Index(HttpServletRequest request) {
		return null;
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String main(HttpServletRequest request) throws Exception {
		Homepage homepage = (Homepage) request.getServletContext().getAttribute("homepage");
		request.getSession().removeAttribute("target_menu");
		request.getSession().setAttribute("menu_move_cmd", true);
		
		//홈페이지 기본설정이 설정되지 않은경우
		if (homepage == null) {
			//설정 하라는알림이있는 페이지로 이동시킨다.
			String new_page = "/index";
			return new_page.substring(1, new_page.length()).replaceAll(".do", "");
		//접속이 모바일 버전이고 홈페이지가 모바일 사용 상태일경우
		//모바일 페이지로 이동 시킨다.
		} else if (isMobile(request) && homepage.getMobile_use() == 1) {
			return getMobileRequestURI(homepage.getMain_url());
		} else {
			request.setAttribute("about_idx", service.getMenuByName("About").getMenu_idx());
			if (isMobile(request)) {
				return getMobileRequestURI(homepage.getMain_url());
			} else {
				return getMobileRequestURI(homepage.getMain_url());
				//return homepage.getMain_url().substring(1, homepage.getMain_url().length()).replaceAll(".do", "");
			}
		}
	}
	
	@RequestMapping(value="/custom/menu/index", method=RequestMethod.GET)
	public String user_menu(HttpServletRequest request, int menu_idx, HttpServletResponse response) {
		Homepage homepage = (Homepage) request.getServletContext().getAttribute("homepage");
		Menu menu = service.getMenuById(menu_idx);
		
		Menu target_menu = (Menu) request.getSession().getAttribute("target_menu");
		
		request.getSession().setAttribute("target_menu", menu);
		if (target_menu == null) {
			request.getSession().setAttribute("menu_move_cmd", true);
		} else {
			request.getSession().setAttribute("menu_move_cmd", false);
		}
		
		//해당 메뉴가 존재하지 않거나 사용여부가 미사용으로 설정해둔 경우
		if (menu == null || menu.getUse_yn() == 1) {
			return BaseController.alertMessageByString("현재 페이지는 접근이 불가능한 페이지입니다.", "/custom/ready", request, response);
		//메뉴가 내부 url 이동인 경우
		} else if (menu.getMenu_type() == 1) {
			return RedirectPage(request, menu.getMenu_url());
		//메뉴 Type이 html 작성인 경우다
		} else if (menu.getMenu_type() > 1 && menu.getMenu_type() <= 4 || menu.getMenu_type() == 0) {
			switch (menu.getMenu_type()) {
				case 0: //html 작성
					//모바일일 경우 -> mobile_html을 호출하여 보여준다.
					if (isMobile(request) && homepage.getMobile_use() == 1) {
						request.setAttribute("html", html_service.getHTMLByMenuId(menu_idx).getMobile_menu_html());
					//PC일 경우 or mobile유저 이지만 홈페이지에서 따로 모바일버전을 사용하지않을경우 -> 일반 html을 호출하여 보여준다.
					} else {
						request.setAttribute("html", html_service.getHTMLByMenuId(menu_idx).getMenu_html());
					}
					return request.getRequestURI().substring(1, request.getRequestURI().length()).replaceAll(".do", "");
				default:
					return RedirectPage(request, "/custom/board/index?type="+ menu.getMenu_type() +"&menu_idx="+ menu.getMenu_idx());
			}
			
		} else {
			alertMessage("알수없는 오류, 관리자에게 문의바랍니다.", request, response);
		}
		
		return null;
	}
	
	@RequestMapping(value="/custom/ready", method=RequestMethod.GET)
	public String readyPage(HttpServletRequest request) {
		return getRequestURI(request);
	}
}
