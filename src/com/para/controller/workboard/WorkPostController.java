package com.para.controller.workboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
		request.setAttribute("menu", "업무 관련 글 관리");
		
		  //검색 조건 부여 -- 시작 
			//검색 옵션 지정 
			Map<String, String> search_items = new LinkedHashMap<String, String>();
			search_items.put("제목", "post_title");
			search_items.put("내용", "post_contents");
			search_items.put("작성자", "writter");
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
			  request.setAttribute("post_count", SearchCount); 
		  } 
		});
		  
		  request.setAttribute("TreeMenu", getTreeNode());
		  
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
	
	@RequestMapping(value="/insert", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse Insert(HttpServletRequest request, HttpServletResponse resposne, @Valid WorkPost post, BindingResult result) {
		AjaxResponse res = new AjaxResponse();

		if (!res.validation_data(result, ControllerCommonMethod.admin_page_path +"/workboard/board/index?menu_idx="+ post.getMenu_idx(), "게시물이 등록 되었습니다.", res)) {
			if (post.getPost_password() != null && post.getPost_password().length() > 0) {
				Pattern p = Pattern.compile("(^[0-9]*$)");

				if (post.getPost_password().length() < 4 || post.getPost_password().length() > 8) {
					return res.returnResponse("암호는 최소 4글자에서 최대 8자까지 입력 가능합니다.", null);
				} else if (!p.matcher(post.getPost_password()).find()) {
					return res.returnResponse("암호는 숫자만 입력 가능합니다.", null);
				}
			}
			
			WorkMenu menu = menu_service.getMenuById(post.getMenu_idx());
			
			if (menu == null || menu.getParent_menu_idx() == 0) {
				return res.returnResponse("존재하지 않거나 사용 할 수 없는 메뉴입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
			}
			
			service.InsertPost(post);
			post.setPost_contents(ManageSmartEditorImg(post.getPost_contents(), "C:/res/img/admin/workpost/" + post.getPost_id()));
			service.UpdatePost(post);
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value = "/getpost", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse ValidPassword(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		
		String post_id = request.getParameter("post_id");
		String menu_idx = request.getParameter("menu_idx");
		
		if (post_id == null || menu_idx == null || post_id.length() == 0 || menu_idx.length() == 0) {
			return res.returnResponse("허용되지 않은 접근 방법입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		}
		
		int pid = 0;
		int mid = 0;
		
		try {
			pid = Integer.parseInt(post_id);
			mid = Integer.parseInt(menu_idx);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return res.returnResponse("잘못된 값이 입력되었습니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		}
		
		WorkPost post = service.getPostById(pid);
		WorkMenu menu = menu_service.getMenuById(mid);
		
		if (post == null || menu == null) {
			return res.returnResponse("존재하지 않거나 삭제된 게시글입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		} else if (menu.getUse_yn() == 1) {
			return res.returnResponse("해당 게시물이 속한 게시판은 접근이 제한되었습니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		} else if (post.getMenu_idx() != mid) {
			return res.returnResponse("잘못된 값이 입력되었습니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		}
		
		//암호가 걸려있는 게시물을 경우 내용을 모두 숨기고 local에 암호가 걸려있는걸 알리기위한 셋팅
		if (post.getPost_password() != null && post.getPost_password().length() > 0) {
			int orign_menu_id = post.getMenu_idx();
			int orign_post_id = post.getPost_id();
			
			post = new WorkPost();
			post.setPost_id(orign_post_id);
			post.setMenu_idx(orign_menu_id);
			post.setPost_password("tempPassword");
		}
		
		res.setObject(post);
		
		return res;
	}
	
	@RequestMapping(value = "/validPW", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse AccessPassword(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		
		String pw = request.getParameter("post_password");
		
		if (pw == null || pw.length() == 0) {
			return res.returnResponse("게시물의 비밀번호를 입력 해주세요.", null);
		} 
		
		String post_id = request.getParameter("post_id");
		
		if (post_id == null || post_id.length() == 0) {
			return res.returnResponse("조회하려는 게시글이 존재하지 않습니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		}
		
		int pid = 0;
		
		try {
			pid = Integer.parseInt(post_id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return res.returnResponse("잘못된 게시글 정보입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		}
		
		WorkPost post = service.getPostById(pid);
		
		if (post == null) {
			return res.returnResponse("존재하지 않거나 삭제된 게시글입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		} else if (!post.getPost_password().equals(pw)) {
			return res.returnResponse("비밀번호가 일치하지 않습니다.", null);
		}
		
		res.setObject(post);
		
		return res;
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse DeletePost(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		String[] target_array = request.getParameterValues("target_array[]");
		
		if (target_array == null || target_array.length == 0) {
			return res.returnResponse("삭제하려는 타겟이 존재하지 않습니다.", null);
		}
		
		for (String key : target_array) {
			if (!validNumber(key, request, response)) {
				return res.returnResponse("잘못된 게시글 키값입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
			}
		}
		
		List<Integer> target_list = new ArrayList<Integer>();

		try {
			for (String key : target_array) {
				target_list.add(Integer.parseInt(key));
				removeDirectory("C:\\res\\img\\admin\\workpost\\" + Integer.parseInt(key));
			}
			
			service.DeletePost(target_list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("게시글 삭제도중 오류 발생");
			return res.returnResponse("잘못된 타겟값입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		}
		
		return res.returnResponse("선택한 게시물이 삭제 되었습니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
	}
	
	@RequestMapping(value="/update", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse UpdatePost(HttpServletRequest request, HttpServletResponse response, @Valid WorkPost post, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		if (!res.validation_data(result, ControllerCommonMethod.admin_page_path + "/workboard/board/index", "게시글이 수정 되었습니다.", res)) {
			WorkPost orign_post = service.getPostById(post.getPost_id());
			
			if (
				((orign_post.getPost_password() == null || orign_post.getPost_password().length() == 0) && post.getPost_password() != null && post.getPost_password().length() > 0) ||
				(orign_post.getPost_password() != null && orign_post.getPost_password().length() > 0 && (post.getPost_password() == null || post.getPost_password().length() == 0)) ||
				(!orign_post.getPost_password().equals(post.getPost_password()))
				) {
				
				return res.returnResponse("암호가 일치하지 않습니다.", null);
			}
			
			WorkMenu menu = menu_service.getMenuById(post.getMenu_idx());
			
			if (menu == null || menu.getMenu_idx() == 0 || menu.getUse_yn() == 1) {
				return res.returnResponse("잘못된 접근 방법입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
			}
			
			post.setPost_contents(ManageSmartEditorImg(post.getPost_contents(), "C:/res/img/admin/workpost/" + post.getPost_id()));
			
			service.UpdatePost(post);
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/moveMenu", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse MoveMenu(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		
		String[] target_array = request.getParameterValues("target_array[]");
		String menu_idx = request.getParameter("menu_idx");
		
		if (menu_idx == null || menu_idx.length() == 0) {
			return res.returnResponse("이동 할 메뉴가 선택되지 않았습니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
		}
		
		WorkMenu menu = null;
		
		try {
			menu = menu_service.getMenuById(Integer.parseInt(menu_idx));
			
			if (menu == null || menu.getUse_yn() == 1) {
				return res.returnResponse("존재하지 않거나 사용 할 수 없는 메뉴입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("업무보고 게시글 메뉴 이동 도중 잘못된 메뉴 아이디값 입력됨");
		}
		
		if (target_array == null || target_array.length == 0) {
			return res.returnResponse("이동하려는 타겟이 존재하지 않습니다.", null);
		}
		
		List<Integer> move_target_list = new ArrayList<Integer>();
		
		for (String key : target_array) {
			if (!validNumber(key, request, response)) {
				return res.returnResponse("잘못된 게시글 키값입니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index");
			}
			
			move_target_list.add(Integer.parseInt(key));
		}
		
		service.movePost(move_target_list, Integer.parseInt(menu_idx));
		
		return res.returnResponse("게시글이 '"+ menu.getMenu_name() +"' 게시판으로 이동 되었습니다.", ControllerCommonMethod.admin_page_path + "/workboard/board/index?menu_idx="+ menu.getMenu_idx());
	}
	
	/**
	 * 글 작성 게시판을 트리 형태로 정렬 해주는 메서드
	 * @return
	 */
	public List<WorkMenu> getTreeNode() {
		List<WorkMenu> AllNode = menu_service.getViewMenu();
		List<WorkMenu> ParentNode = menu_service.getParentMenu();
		
		List<WorkMenu> tree = new ArrayList<WorkMenu>();
				
		for (WorkMenu m : ParentNode) {
			tree.addAll(SortNode(m, AllNode));
		}
		
		int parent_id = 0;
		int depth = 0;
		
		for (int i = 0; i < tree.size(); i++) {
			WorkMenu m = tree.get(i);
			
			if (i != 0) {
				WorkMenu before_menu = tree.get(i - 1);
				
				if (m.getParent_menu_idx() == 0) {
					depth = 0;
					parent_id = 0;
					continue;
				} else if (m.getParent_menu_idx() == before_menu.getMenu_idx() || m.getParent_menu_idx() == parent_id) {
					if (m.getParent_menu_idx() == before_menu.getMenu_idx()) {
						depth ++;
						parent_id = before_menu.getMenu_idx();
					}
					
					String dashed = "｜";
					
					for (int x = 0; x < depth; x++) {
						if (depth > 2 && x > 1) {
							dashed += "｜";
						}
						
						dashed += "-----";
					}
					
					if (depth > 1) {
						dashed += "｜---";
					}
					
					m.setMenu_name(dashed + " " + m.getMenu_name());
				} else if (m.getParent_menu_idx() != before_menu.getMenu_idx() && m.getParent_menu_idx() != parent_id) {
					depth --;
					parent_id = m.getMenu_idx();
					
					String dashed = "｜";
					
					for (int x = 0; x < depth; x++) {
						if (depth > 2 && x > 1) {
							dashed += "｜";
						}
						
						dashed += "-----";
					}
					
					if (depth > 1) {
						dashed += "｜---";
					}

					m.setMenu_name(dashed + " " + m.getMenu_name());
				}
			}
		}
		return tree;
	}
	
	public List<WorkMenu> SortNode(WorkMenu ParentNode, List<WorkMenu> AllNode) {
		List<WorkMenu> nodes = new ArrayList<WorkMenu>();
		
		for (WorkMenu m : AllNode) {
			if (m.getMenu_idx() == ParentNode.getMenu_idx() && ParentNode.getParent_menu_idx() == 0) {
				nodes.add(m);
			} else if (m.getParent_menu_idx() == ParentNode.getMenu_idx()) {
				nodes.add(m);
				nodes.addAll(SortNode(m, AllNode));
			}
		}
		
		return nodes;
	}
}
