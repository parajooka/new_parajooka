package com.paraframework.controller.customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.object.Menu;
import com.paraframework.object.board.Post;
import com.paraframework.object.board.PostComment;
import com.paraframework.object.board.PostFile;
import com.paraframework.service.MenuService;
import com.paraframework.service.board.PostCommentService;
import com.paraframework.service.board.PostFileService;
import com.paraframework.service.board.PostService;

@Controller
@RequestMapping(value=ControllerCommonMethod.customer_page_path + "/board")
public class CutomerBoardController extends ControllerCommonMethod {
	@Autowired 
	private PostCommentService service;
	@Autowired
	private PostService post_service;
	@Autowired
	private PostFileService file_service;
	@Autowired
	private MenuService menu_service;
	
	private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy", Locale.KOREAN);
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String Test(HttpServletRequest request, HttpServletResponse response) {
		String menu_idx = request.getParameter("menu_idx");
		String type = request.getParameter("type");
		
		int menu_id = 0;
		int menu_type = 0;
		
		try {
			menu_type = Integer.parseInt(type);
			menu_id = Integer.parseInt(menu_idx);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			alertMessage("잘못된 타입의 메뉴입니다.", request, response);
			
			return null;
		}
		
		Menu menu = menu_service.getMenuById(menu_id);
		
		if (menu == null || menu.getMenu_type() != menu_type) {
			alertMessage("일치하는 메뉴가 존재하지않거나 잘못된 타입의 메뉴입니다.", request, response);
			return null;
		}
		
		switch (menu.getMenu_type()) {
			//게시판 형태일때는 검색 및 페이징 레이아웃 적용
			case 2:
				//검색 조건 부여 -- 시작
				//검색 옵션 지정
					Map<String, String> search_items = new LinkedHashMap<String, String>();
					search_items.put("글 제목", "title");
				//검색 조건 부여 -- 종료
				
				LocationSearchCheck(request, response, "post", "post_id", search_items, new LocationSearchResultFunction() {
					
					@Override
					public void SearchTrue(int result, List<Integer> column_value) {
						// TODO Auto-generated method stub
						List<Post> post_list = new ArrayList<>();
						
						for (int i : column_value) {
							post_list.add(post_service.getPostById(i));
						}
						
						request.setAttribute("post_list", post_list);
						request.setAttribute("post_count", result);
					}
					
					@Override
					public void SearchFalse() {
						// TODO Auto-generated method stub
						int SearchCount = post_service.CountPostByMenuId(Integer.parseInt(menu_idx));
						List<Post> post_list = post_service.getPostByPaging(AutoPaging(request, response, SearchCount), Integer.parseInt(menu_idx));
						
						request.setAttribute("post_list", post_list);
						request.setAttribute("post_count", SearchCount);
					}
				});
				break;
			//갤러리 레이아웃일 경우
			case 3:
				String year = request.getParameter("post_year");
				
				List<String> date = post_service.getTerm(menu.getMenu_idx() + "");
				Map<String, String> dates = new LinkedHashMap<>();
				
				if (date != null && date.size() > 0 && date.get(0) != null) {
					for (String s : date) {
						dates.put(s.substring(0, s.indexOf("-")), "year");
					}
					
					request.setAttribute("post_year", dates.keySet());
					request.setAttribute("post_list", post_service.getPostByYear(date.get(0) + "-01-01", formatTime.format(new Date()) + "-12-31", menu.getMenu_idx() + ""));
				}
				
				if (year == null || year.length() == 0 || year.equals("all")) {
					request.setAttribute("post_year", dates.keySet());
					request.setAttribute("post_list", post_service.getPostByYear(date.get(0) + "-01-01", formatTime.format(new Date()) + "-12-31", menu.getMenu_idx() + ""));
				} else {
					request.setAttribute("post_list", post_service.getPostByYear(year + "-01-01", year + "-12-31", menu.getMenu_idx() + ""));
				}
				break;
		default:
			break;
		}
		
		request.setAttribute("menu_idx", menu_idx);
		request.setAttribute("menu_type", type);
		
		setMenu(request, menu);
		
		String menu_url = menu.getMenu_url();
			   menu_url = menu_url == null || menu_url.length() == 0 ? getRequestURI(request) : menu_url.substring(1, menu_url.length());
		return menu_url + "/index";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String WritePost(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("menu_idx");
		int menu_id = 0;
		
		if (validNumber(id, request, response)) {
			menu_id = Integer.parseInt(id);
		} else {
			return null;
		}
		
		Menu menu = menu_service.getMenuById(menu_id);
		
		
		if (menu == null || menu.getMenu_type() < 2) {
			alertMessage("해당 메뉴는 게시글을 작성 할 수 없는 메뉴입니다.", request, response);
			return null;
		}
		
		request.setAttribute("thisMenu", menu);
		
		String post_idx = request.getParameter("post_id");
		if (post_idx != null && post_idx.length() > 0) {
			int post_id = 0;
			
			if (validNumber(post_idx, request, response)) {
				post_id = Integer.parseInt(post_idx);
			} else {
				return null;
			}
			
			Post post = post_service.getPostById(post_id);
			
			if (post == null) {
				alertMessage("존재하지않거나 삭제된 포스터입니다.", request, response);
				return null;
			}
			
			request.setAttribute("thisPost", post);
			request.setAttribute("thisFile", file_service.getPostFilesByPostId(post_id));
		}

		setMenu(request, menu);
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/write/post/submit", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse InjectionNewPost(MultipartHttpServletRequest request, HttpServletResponse response, @Valid Post post, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		if (!res.validation_data(result, null, "게식글을 등록 하였습니다.", res)) {
			Menu menu = menu_service.getMenuById(post.getMenu_id());
			if (menu == null || menu.getMenu_type() < 2) {
				return res.returnResponse("잘못된 메뉴 아이디입니다.", null);
			}
			// 유효성 검사 종료
			
			res.setNext_url(ControllerCommonMethod.customer_page_path +"/board/index?type="+ menu.getMenu_type() +"&menu_idx="+ post.getMenu_id());
			
			System.out.println(post.getMobile_contents() + "hihi");
			

			//메인이미지 파일
			MultipartFile main_img = request.getFile("post_main_img");
			//첨부파일
			MultipartFile append_file = request.getFile("post_append_file");
			
			if (main_img != null) {
				if (!checkImgfile(main_img.getOriginalFilename())) {
					return res.returnResponse("대표 이미지에는 이미지 파일만 첨부 가능합니다.", "N");
				}
			}
			
			post_service.InsertPost(post);
			
			//메인이미지 업로드
			if (main_img != null) {
				post.setMain_img(InjectionFile(request, "C:\\res\\img\\custom\\board\\"+ post.getMenu_id() + "\\" + post.getPost_id(), "post_main_img").get(0));
			}
			
			//스마트 에디터 이미지 관리
			post.setContents(ManageSmartEditorImg(post.getContents(), "C:/res/img/custom/board/"+ post.getMenu_id() + "/" + post.getPost_id()));
			post.setExplanation(ManageSmartEditorImg(post.getExplanation(), "C:/res/img/custom/board/"+ post.getMenu_id() + "/" + post.getPost_id()));
			post.setMobile_contents(ManageSmartEditorImg(post.getMobile_contents(), "C:/res/img/custom/board/"+ post.getMenu_id() + "/" + post.getPost_id()));
			
			post_service.UpdatePost(post);
			
			if (append_file != null) {
				PostFile file = new PostFile();
				file.setPost_id(post.getPost_id());
				file.setFile(InjectionFile(request, "C:\\res\\img\\custom\\board\\"+ post.getMenu_id() + "\\" + post.getPost_id(), "post_append_file").get(0));
				
				file_service.InsertPostFile(file);
			}
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/write/post/modify", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse ModifyNewPost(MultipartHttpServletRequest request, HttpServletResponse response, @Valid Post post, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		if (!res.validation_data(result, null, "게식글을 수정 하였습니다.", res)) {
			Menu menu = menu_service.getMenuById(post.getMenu_id());
			if (menu == null || menu.getMenu_type() < 2) {
				return res.returnResponse("잘못된 메뉴 아이디입니다.", null);
			}
			// 유효성 검사 종료
			
			res.setNext_url(ControllerCommonMethod.customer_page_path +"/board/index?type="+ menu.getMenu_type() +"&menu_idx="+ post.getMenu_id());

			System.out.println(post.getMobile_contents() + "hihi");

			//메인이미지 파일
			MultipartFile main_img = request.getFile("post_main_img");
			//첨부파일
			MultipartFile append_file = request.getFile("post_append_file");
			
			if (main_img != null) {
				if (!checkImgfile(main_img.getOriginalFilename())) {
					return res.returnResponse("대표 이미지에는 이미지 파일만 첨부 가능합니다.", "N");
				}
			}
			
			Post orignPost = post_service.getPostById(post.getPost_id());
			
			//기존 메인 이미지로 다시 셋팅
			post.setMain_img(orignPost.getMain_img());
			
			//메인이미지  신규 업로드 (수정)
			if (main_img != null) {
				//기존 메인이미지가 있다면 삭제
				if (post.getMain_img() != null && post.getMain_img().length() > 0) {
					removeFile("C:\\res\\img\\custom\\board\\"+ post.getMenu_id() + "\\" + post.getPost_id() + "\\"+ post.getMain_img());
				}
				
				//새로운 이미지 등록
				post.setMain_img(InjectionFile(request, "C:\\res\\img\\custom\\board\\"+ post.getMenu_id() + "\\" + post.getPost_id(), "post_main_img").get(0));
			}
			
			//스마트 에디터 이미지 관리
			post.setContents(ManageSmartEditorImg(post.getContents(), "C:/res/img/custom/board/"+ post.getMenu_id() + "/" + post.getPost_id()));
			post.setExplanation(ManageSmartEditorImg(post.getExplanation(), "C:/res/img/custom/board/"+ post.getMenu_id() + "/" + post.getPost_id()));
			post.setMobile_contents(ManageSmartEditorImg(post.getMobile_contents(), "C:/res/img/custom/board/"+ post.getMenu_id() + "/" + post.getPost_id()));
			
			post_service.UpdatePost(post);
			
			if (append_file != null) {
				
				//기존 첨부파일이 있다면 삭제
				List<PostFile> files = file_service.getPostFilesByPostId(post.getPost_id());
				if (files != null && files.size() > 0 && files.get(0) != null && files.get(0).getFile() != null && files.get(0).getFile().length() > 0) {
					removeFile("C:\\res\\img\\custom\\board\\"+ post.getMenu_id() + "\\" + post.getPost_id() + "\\" + files.get(0).getFile());
					file_service.DeletePostFile(files.get(0).getPost_file_id());
				}
				
				PostFile file = new PostFile();
				file.setPost_id(post.getPost_id());
				file.setFile(InjectionFile(request, "C:\\res\\img\\custom\\board\\"+ post.getMenu_id() + "\\" + post.getPost_id(), "post_append_file").get(0));
				
				file_service.InsertPostFile(file);
			}
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse deletePost(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="delete_post_targets[]") List<String> delete_targets) {
		AjaxResponse res = new AjaxResponse();
		
		try {
			for (String s : delete_targets) {
				Post post = post_service.getPostById(Integer.parseInt(s));
				
				if (post != null) {
					//해당 게시물의 파일들 모두 삭제
					removeDirectory("C:\\res\\img\\custom\\board\\"+ post.getMenu_id() + "\\" + post.getPost_id());
					post_service.DeletePost(post.getPost_id());
					
					Menu menu = menu_service.getMenuById(post.getMenu_id());
					
					res.setMessage("선택한 포스터를 모두 삭제하였습니다.");
					res.setNext_url(ControllerCommonMethod.customer_page_path +"/board/index?type="+ menu.getMenu_type() +"&menu_idx="+ menu.getMenu_idx());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			alertMessage("잘못된 접근입니다.", request, response);
		}
		
		return res;
	}
	
	@RequestMapping(value="/view", method=RequestMethod.GET)
	public String getPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String menu_idx = request.getParameter("menu_idx");
		String post_idx = request.getParameter("post_idx");
		
		if (!validNumber(menu_idx, request, response) || menu_idx == null || menu_idx.length() == 0) {
			return null;
		} else if (post_idx == null || post_idx.length() == 0 || !validNumber(post_idx, request, response)) {
			return null;
		}
		
		Menu menu = menu_service.getMenuById(Integer.parseInt(menu_idx));
		
		request.setAttribute("thisMenu", menu);
		
		if (menu == null ||  menu.getUse_yn() == 1) {
			alertMessage("존재하지않거나 삭제된 메뉴입니다.", request, response);
			return null;
		}
		
		Post post = isMobile(request) == true ? post_service.getMobilePostById(Integer.parseInt(post_idx)) : post_service.getPostById(Integer.parseInt(post_idx));
		
		request.setAttribute("board_mobile", isMobile(request));
		
		if (post == null) {
			alertMessage("존재하지않거나 삭제된 게시글입니다.", request, response);
			return null;
		}
		
		List<PostFile> file = file_service.getPostFilesByPostId(post.getPost_id());
		List<Post> pre_post = post_service.getNextBeforePostById(post.getPost_id(), post.getMenu_id());
		
		request.setAttribute("thisPost", post);
		
		if (file != null && file.size() > 0) {
			request.setAttribute("thisFile", file);
		}
		
		if (pre_post != null && pre_post.size() > 0) {
			request.setAttribute("pre_post", pre_post);
			request.setAttribute("menu_idx", menu_idx);
		}
		
		//쿠키호출 검증 및 조회수 관리 
		
		if (findCookieByName(request, response, "PostView"+ post.getPost_id(), "CustomBoardPostViewCookie") == null) {
			//조회수 증가
			setCookie(response, "PostView"+ post.getPost_id(), RandomString(), "CustomBoardPostViewCookie", 3);
			post_service.UpdateViewCount(post.getPost_id());
		}
		
		setMenu(request, menu);
		
		
		String menu_url = menu.getMenu_url();
			   menu_url = menu_url == null || menu_url.length() == 0 ? getRequestURI(request) : menu_url.substring(1, menu_url.length());
		return menu_url + "/view";
	}
	
	
	
	
	/**
	 * 해당 게시글의 모든 댓글을 가져오는 메서드
	 * @param post_id
	 * @return
	 */
	public List<PostComment> getTreeComment(int post_id) {
		List<PostComment> comment_list = service.getCommentByPostId(post_id);
		List<PostComment> comment_parent = service.getParentcommentByPostId(post_id);
		
		List<PostComment> tree = new ArrayList<>();
		
		for (PostComment parent : comment_parent) {
			tree.addAll(getTreeCommentLoop(parent, comment_list));
		}
		
		return tree;
	}
	
	/**
	 * 해당 게시글의 답글 및 댓글 순서 정렬
	 * @param parent
	 * @param childs
	 * @return
	 */
	public List<PostComment> getTreeCommentLoop(PostComment parent, List<PostComment> childs) {
		List<PostComment> posts = new ArrayList<>();
		
		for (PostComment p : childs) {
			if (p.getComment_id() == parent.getComment_id() && parent.getReply_to() == 0) {
				posts.add(p);
			} else if (p.getReply_to() == parent.getComment_id()) {
				posts.add(p);
				posts.addAll(getTreeCommentLoop(p, childs));
			}
		}
		
		return posts;
	}
	
	public void setMenu(HttpServletRequest request, Menu menu) {
		Menu target_menu = (Menu) request.getSession().getAttribute("target_menu");
		
		request.getSession().setAttribute("target_menu", menu);
		if (target_menu == null) {
			request.getSession().setAttribute("menu_move_cmd", true);
		} else {
			request.getSession().setAttribute("menu_move_cmd", false);
		}
		
	}
}
