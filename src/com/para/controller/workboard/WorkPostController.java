package com.para.controller.workboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.para.object.workboard.WorkMenu;
import com.para.object.workboard.WorkPost;
import com.para.service.workboard.WorkMenuService;
import com.para.service.workboard.WorkPostService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;

@Controller
@RequestMapping(value=ControllerCommonMethod.admin_page_path + "/workboard/board")
public class WorkPostController extends ControllerCommonMethod {
	@Autowired
	private WorkPostService service;
	@Autowired
	private WorkMenuService menu_service;

	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("menu", "업무 보고 관리");
		
		  //검색 조건 부여 -- 시작 
			//검색 옵션 지정 
			Map<String, String> search_items = new LinkedHashMap<String, String>();
			search_items.put("제목", "post_title");
			search_items.put("내용", "post_contents"); search_items.put("작성자", "writter");
			//검색 조건 부여 -- 종료
		  
		  LocationSearchCheck(request, response, "workpost", "post_id", search_items, new LocationSearchResultFunction() {
		  
			  @Override
			  public void SearchTrue(int result, List<Integer> column_value) { 
			  //TODO Auto-generated method stub
			  
			  if (column_value != null && !column_value.isEmpty()) {
				  request.setAttribute("acc_ip_list", service.getPostByIds(column_value)); 
			  } else {
				  List<WorkPost> list = new ArrayList<WorkPost>();
				  request.setAttribute("acc_ip_list", list);
			  }
			  
			  request.setAttribute("acc_ip_count", result);
			  
			  }
		  
			  @Override
			  public void SearchFalse() { 
				  String menu_idx = request.getParameter("menu_idx");
				  int SearchCount = 0; List<WorkPost> post_list = null;
			  
				  if (menu_idx == null || menu_idx.length() == 0) {
					  SearchCount = service.CountPost();
					  post_list = service.getPostByPaging(AutoPaging(request, response, SearchCount)); 
				  } else { 
					  try {
						  request.setAttribute("before_menu_idx", Integer.parseInt(menu_idx));
						  
						  SearchCount = service.CountPostByMenu(Integer.parseInt(menu_idx));
						  post_list = service.getPostByPagingAndMenu(Integer.parseInt(menu_idx), AutoPaging(request, response, SearchCount));
					   } catch (Exception e) { 
						   // TODO: handle exception
						   e.printStackTrace();
						   post_list = new ArrayList<WorkPost>();
						   request.setAttribute("post_list", post_list);
						   request.setAttribute("post_count", 0); 
					   } 
				  }
			  request.setAttribute("post_list", post_list);
			  request.setAttribute("post_count", SearchCount); } });
		 
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
		menu_list.addAll(menu_service.getViewMenu());
		
		res.setObject(menu_list);
		return res;
	}
}
