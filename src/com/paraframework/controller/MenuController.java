package com.paraframework.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.common.handler.TempJspHandler;
import com.paraframework.object.Menu;
import com.paraframework.object.MenuHtml;
import com.paraframework.service.MenuHtmlService;
import com.paraframework.service.MenuService;

@Controller
@RequestMapping(value=ControllerCommonMethod.admin_page_path + "/menu")
public class MenuController extends ControllerCommonMethod {
	@Autowired
	private MenuService service;
	
	@Autowired
	private MenuHtmlService html_service;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String menu(HttpServletRequest request) {
		request.setAttribute("menu", "사용자 메뉴 관리");
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/bytree", method=RequestMethod.GET)
	public @ResponseBody AjaxResponse getAllMenuByTree() {
		AjaxResponse res = new AjaxResponse();
		List<Menu> menu_list = new ArrayList<Menu>();
		
		Menu menu = new Menu();
		
		menu.setGroup_idx(-1);
		menu.setMenu_idx(0);
		menu.setMenu_name("최상위");
		menu.setMenu_type(0);
		menu.setMenu_url("/");
		menu.setParent_menu_idx(-1);
		menu.setPrint_seq(0);
		menu.setView_yn(0);
		
		
		menu_list.add(menu);
		menu_list.addAll(service.getAllMenu());
		
		res.setObject(menu_list);
		return res;
	}
	
	@RequestMapping(value="/getMenu", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getMenuById(HttpServletRequest request, int menu_idx) {
		
		AjaxResponse res = new AjaxResponse();
		
		List<Object> menu = new ArrayList<Object>();
		
		menu.add(service.getMenuById(menu_idx));
		menu.add(html_service.getHTMLByMenuId(menu_idx));
		
		res.setObject(menu);
		return res;
	}
	
	@RequestMapping(value="/moveMenu", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse moveMenu(HttpServletRequest request, Menu menu) {
		AjaxResponse res = new AjaxResponse();
		
		//이동을 요청한 메뉴
		Menu move_menu = service.getMenuById(menu.getMenu_idx());
		
		//최상위 메뉴로 이동된 경우
		if (menu.getParent_menu_idx() == 0) {
			move_menu.setParent_menu_idx(0);
			move_menu.setGroup_idx(move_menu.getMenu_idx());
		} else {
			//이동의 타깃이되는 메뉴
			Menu target_menu = service.getMenuById(menu.getParent_menu_idx());
			//요청 메뉴의 그룹을 부모의 그룹과 동일하게 변경
			move_menu.setGroup_idx(target_menu.getGroup_idx());
			//요청 메뉴의 부모아이디를 타깃의 아이디로 변경
			move_menu.setParent_menu_idx(target_menu.getMenu_idx());
		}
		
		//요청 메뉴 업데이트
		service.moveMenu(move_menu);
		

		
		//이동 요청된 메뉴의 자식들
		List<Menu> child_menu = service.getMenuByParentId(move_menu.getMenu_idx());
		
		//자식들의 그룹을 요청 메뉴의 그룹으로 변경 및 업데이트
		for (Menu child : child_menu) {
			child.setGroup_idx(move_menu.getGroup_idx());
			service.moveMenu(child);
		}
		
		res.setMessage("메뉴 이동이 완료되었습니다.");
		res.setNext_url(ControllerCommonMethod.admin_page_path + "/menu/index");
		

		if (request.getServletContext().getAttribute("menu_list") != null) {
			request.getServletContext().removeAttribute("menu_list");
			request.getServletContext().setAttribute("menu_list", service.getAllMenu());
		}
		
		return res;
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse insertMenu(HttpServletRequest request, @Valid Menu menu, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		String next_url = ControllerCommonMethod.admin_page_path + "/menu/index";
		String success_message = "메뉴가 추가 되었습니다.";
		
		Menu parent_menu = service.getMenuById(menu.getParent_menu_idx());
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			if ((menu.getParent_menu_idx() == 0 && menu.getGroup_idx() == 0) ||
				(menu.getParent_menu_idx() != 0 && parent_menu != null && menu.getGroup_idx() == parent_menu.getGroup_idx())) {
					//추가하려는 메뉴가 최상위가 아닐경우 부모의 그룹아이디를 따라간다.
					service.insertMenu(menu);
					
					//추가하려는 메뉴가 최상위일경우 자신의 아이디를 그룹아이디에 집어넣는다.
					//자신의 아이디는 위에서 이미 insert를 한상태이기 때문에 자동으로 객체에 들어가있다.
					if (menu.getParent_menu_idx() == 0) {
						menu.setGroup_idx(menu.getMenu_idx());
						service.moveMenu(menu);
					}
					
					MenuHtml html = new MenuHtml();
					
					html.setMenu_html("");
					html.setMenu_idx(menu.getMenu_idx());
					html_service.insertHTML(html);
					
					if (menu.getMenu_type() == 0) {
						TempJspHandler handler = new TempJspHandler();
						handler.ReCreateTempJsp(request.getServletContext(), menu);
					}
					
					if (request.getServletContext().getAttribute("menu_list") != null) {
						request.getServletContext().removeAttribute("menu_list");
						request.getServletContext().setAttribute("menu_list", service.getViewMenu());
					}
			} else {
				res.setMessage("잘못된 접근입니다.");
			}
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse editMenu(HttpServletRequest request, @Valid Menu menu, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = ControllerCommonMethod.admin_page_path + "/menu/index";
		String success_message = "메뉴가 수정 되었습니다.";
		
		Menu parent_menu = service.getMenuById(menu.getParent_menu_idx());
		Menu target_menu = service.getMenuById(menu.getMenu_idx());

		if (!res.validation_data(result, next_url, success_message, res)) {
			if ((menu.getParent_menu_idx() == 0 && menu.getGroup_idx() == target_menu.getGroup_idx()) ||
					(menu.getParent_menu_idx() != 0 && parent_menu != null && menu.getGroup_idx() == parent_menu.getGroup_idx())) {
					service.updateMenu(menu);
					
					if (menu.getMenu_type() == 0) {
						TempJspHandler handler = new TempJspHandler();
						handler.ReCreateTempJsp(request.getServletContext(), menu);
					}
		
					if (request.getServletContext().getAttribute("menu_list") != null) {
						request.getServletContext().removeAttribute("menu_list");
						request.getServletContext().setAttribute("menu_list", service.getViewMenu());
					}
			} else {
				res.setMessage("잘못된 접근입니다.");
			}
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteMenu(HttpServletRequest request, int menu_idx) {
		AjaxResponse res = new AjaxResponse();
		
		if (menu_idx <= 0) {
			res.setMessage("잘못된 접근입니다.");
			res.setNext_url(ControllerCommonMethod.admin_page_path + "/menu/index");
			res.setProcessing_result(true);
			
			return res;
		} else if (service.getMenuById(menu_idx) == null) {
			res.setMessage("존재하지않거나 삭제된 메뉴입니다.");
			res.setNext_url(ControllerCommonMethod.admin_page_path + "/menu/index");
			res.setProcessing_result(true);
			
			return res;
		} else {
			
			//자식 메뉴들 먼저 삭제
			List<Menu> childs = service.getMenuByParentId(menu_idx);
			
			for (Menu c : childs) {
				service.deleteMenu(c.getMenu_idx());
			}
			
			//메뉴 삭제
			service.deleteMenu(menu_idx);
			
			TempJspHandler handler = new TempJspHandler();
			handler.RemoveTempJsp(request.getServletContext(), menu_idx);
			
			res.setMessage("메뉴가 삭제되었습니다.");
			res.setNext_url(ControllerCommonMethod.admin_page_path + "/menu/index");
			res.setProcessing_result(true);
			

			if (request.getServletContext().getAttribute("menu_list") != null) {
				request.getServletContext().removeAttribute("menu_list");
				request.getServletContext().setAttribute("menu_list", service.getViewMenu());
			}
		}
		
		
		return res;
	}
	
	@RequestMapping(value="/html/edit", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse EditHTML(HttpServletRequest request, MenuHtml menu_html) {
		AjaxResponse res = new AjaxResponse();
		
		Menu menu = service.getMenuById(menu_html.getMenu_idx());
		if (menu != null) {
			html_service.updateHTML(menu_html);
			res.setMessage("해당메뉴의 html이 성공적으로 수정되었습니다.");
			if (menu.getMenu_type() == 0) {
				TempJspHandler handler = new TempJspHandler();
				handler.CustomazingTemoJsp(request.getServletContext(), "menu_"+ menu.getMenu_idx() +".jsp", menu_html.getMenu_html());
			}
		} else {
			res.setMessage("잘못된 접근방법 입니다.");
		}
		
		res.setNext_url(ControllerCommonMethod.admin_page_path + "/menu/index");
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/mobile_html/edit", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse EditMoileHTML(HttpServletRequest request, MenuHtml menu_html) {
		AjaxResponse res = new AjaxResponse();

		Menu menu = service.getMenuById(menu_html.getMenu_idx());
		if (menu != null) {
			html_service.updateMobileHTML(menu_html);
			res.setMessage("해당메뉴의 모바일버전 html이 성공적으로 수정되었습니다.");
			if (menu.getMenu_type() == 0) {
				TempJspHandler handler = new TempJspHandler();
				handler.CustomazingTemoJsp(request.getServletContext(), "mobile_menu_"+ menu.getMenu_idx() +".jsp", menu_html.getMobile_menu_html());
			}
		} else {
			res.setMessage("잘못된 접근방법 입니다.");
		}
		
		res.setNext_url(ControllerCommonMethod.admin_page_path + "/menu/index");
		res.setProcessing_result(true);
		
		return res;
	}

}
