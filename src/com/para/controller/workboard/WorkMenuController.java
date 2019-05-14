package com.para.controller.workboard;


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

import com.para.object.workboard.WorkMenu;
import com.para.service.workboard.WorkMenuService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.common.handler.HomepageInfoHandler;
import com.paraframework.common.handler.TempJspHandler;
import com.paraframework.object.Menu;

@Controller
@RequestMapping(value=ControllerCommonMethod.admin_page_path + "/workboard/category")
public class WorkMenuController extends ControllerCommonMethod {
	@Autowired
	private WorkMenuService service;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String menu(HttpServletRequest request) {
		request.setAttribute("menu", "업무보고 메뉴 관리");
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/bytree", method=RequestMethod.GET)
	public @ResponseBody AjaxResponse getAllMenuByTree() {
		AjaxResponse res = new AjaxResponse();
		List<WorkMenu> menu_list = new ArrayList<WorkMenu>();
		
		WorkMenu menu = new WorkMenu();
		
		menu.setGroup_idx(-1);
		menu.setMenu_idx(0);
		menu.setMenu_name("최상위");
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
		
		res.setObject(menu);
		return res;
	}
	
	@RequestMapping(value="/moveMenu", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse moveMenu(HttpServletRequest request, Menu menu) {
		AjaxResponse res = new AjaxResponse();
		
		//이동을 요청한 메뉴
		WorkMenu move_menu = service.getMenuById(menu.getMenu_idx());
		
		//최상위 메뉴로 이동된 경우
		if (menu.getParent_menu_idx() == 0) {
			move_menu.setParent_menu_idx(0);
			move_menu.setGroup_idx(move_menu.getMenu_idx());
		} else {
			//이동의 타깃이되는 메뉴
			WorkMenu target_menu = service.getMenuById(menu.getParent_menu_idx());
			//요청 메뉴의 그룹을 부모의 그룹과 동일하게 변경
			move_menu.setGroup_idx(target_menu.getGroup_idx());
			//요청 메뉴의 부모아이디를 타깃의 아이디로 변경
			move_menu.setParent_menu_idx(target_menu.getMenu_idx());
		}
		
		//요청 메뉴 업데이트
		service.moveMenu(move_menu);
		

		
		//이동 요청된 메뉴의 자식들
		List<WorkMenu> child_menu = service.getMenuByParentId(move_menu.getMenu_idx());
		
		//자식들의 그룹을 요청 메뉴의 그룹으로 변경 및 업데이트
		for (WorkMenu child : child_menu) {
			child.setGroup_idx(move_menu.getGroup_idx());
			service.moveMenu(child);
		}
		
		res.setMessage("메뉴 이동이 완료되었습니다.");
		res.setNext_url(ControllerCommonMethod.admin_page_path + "/workboard/category/index");
		

		//context에 홈페이지 정보 갱신
		HomepageInfoHandler handler = new HomepageInfoHandler();
		handler.UploadCustomerMenu(request.getServletContext());
		
		return res;
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse insertMenu(HttpServletRequest request, @Valid WorkMenu menu, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		String next_url = ControllerCommonMethod.admin_page_path + "/workboard/category/index";
		String success_message = "메뉴가 추가 되었습니다.";
		
		WorkMenu parent_menu = service.getMenuById(menu.getParent_menu_idx());
		
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
					
					//context에 홈페이지 정보 갱신
					HomepageInfoHandler handler = new HomepageInfoHandler();
					handler.UploadCustomerMenu(request.getServletContext());
			} else {
				res.setMessage("잘못된 접근입니다.");
			}
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse editMenu(HttpServletRequest request, @Valid WorkMenu menu, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = ControllerCommonMethod.admin_page_path + "/workboard/category/index";
		String success_message = "메뉴가 수정 되었습니다.";
		
		WorkMenu parent_menu = service.getMenuById(menu.getParent_menu_idx());
		WorkMenu target_menu = service.getMenuById(menu.getMenu_idx());

		if (!res.validation_data(result, next_url, success_message, res)) {
			if ((menu.getParent_menu_idx() == 0 && menu.getGroup_idx() == target_menu.getGroup_idx()) ||
					(menu.getParent_menu_idx() != 0 && parent_menu != null && menu.getGroup_idx() == parent_menu.getGroup_idx())) {
					service.updateMenu(menu);
					
					//context에 홈페이지 정보 갱신
					HomepageInfoHandler handler = new HomepageInfoHandler();
					handler.UploadCustomerMenu(request.getServletContext());
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
			res.setNext_url(ControllerCommonMethod.admin_page_path + "/workboard/category/index");
			res.setProcessing_result(true);
			
			return res;
		} else if (service.getMenuById(menu_idx) == null) {
			res.setMessage("존재하지않거나 삭제된 메뉴입니다.");
			res.setNext_url(ControllerCommonMethod.admin_page_path + "/workboard/category/index");
			res.setProcessing_result(true);
			
			return res;
		} else {
			
			//자식 메뉴들 먼저 삭제
			List<WorkMenu> childs = service.getMenuByParentId(menu_idx);
			
			for (WorkMenu c : childs) {
				service.deleteMenu(c.getMenu_idx());
			}
			
			//메뉴 삭제
			service.deleteMenu(menu_idx);
			
			TempJspHandler handler = new TempJspHandler();
			handler.RemoveTempJsp(request.getServletContext(), menu_idx);
			
			res.setMessage("메뉴가 삭제되었습니다.");
			res.setNext_url(ControllerCommonMethod.admin_page_path + "/workboard/category/index");
			res.setProcessing_result(true);
			

			//context에 홈페이지 정보 갱신
			HomepageInfoHandler infoHandler = new HomepageInfoHandler();
			infoHandler.UploadCustomerMenu(request.getServletContext());
		}
		
		
		return res;
	}
}
