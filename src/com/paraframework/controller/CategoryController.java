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
import com.paraframework.common.BaseController;
import com.paraframework.object.Category;
import com.paraframework.service.CategoryService;

@Controller
@RequestMapping(value="/jooka/admin/category")
public class CategoryController extends BaseController {
	
	private static int CategoryMaxDepth = 4;
	
	@Autowired
	private CategoryService service;
	
	@Override
	public String Index(HttpServletRequest request) {
		return null;
	}
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String main(HttpServletRequest request) {
		request.setAttribute("menu", "카테고리 관리");
		request.setAttribute("max_depth", CategoryMaxDepth);
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/bytree", method=RequestMethod.GET)
	public @ResponseBody AjaxResponse getAllCategoryByTree() {
		AjaxResponse res = new AjaxResponse();
		List<Category> Category_list = new ArrayList<Category>();
		
		Category Category = new Category();
		
		Category.setGroup_idx(-1);
		Category.setCategory_idx(0);
		Category.setCategory_name("최상위");
		Category.setParent_category_idx(-1);
		Category.setPrint_seq(0);
		Category.setView_yn(0);
		
		
		Category_list.add(Category);
		Category_list.addAll(service.getAllCategory());
		
		res.setObject(Category_list);
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/getCategory", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getCategoryById(HttpServletRequest request, int category_idx) {
		
		AjaxResponse res = new AjaxResponse();
		
		List<Object> Category = new ArrayList<Object>();
		
		Category.add(service.getCategoryById(category_idx));
		
		res.setObject(Category);
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/moveCategory", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse moveCategory(HttpServletRequest request, Category Category) {
		AjaxResponse res = new AjaxResponse();
		
		//이동을 요청한 카테고리
		Category move_category = service.getCategoryById(Category.getCategory_idx());
		
		//최상위 카테고리로 이동된 경우
		if (Category.getParent_category_idx() == 0) {
			move_category.setParent_category_idx(0);
			move_category.setGroup_idx(move_category.getCategory_idx());
		} else {
			//이동의 타깃이되는 카테고리
			Category target_category = service.getCategoryById(Category.getParent_category_idx());
			//요청 카테고리의 그룹을 부모의 그룹과 동일하게 변경
			move_category.setGroup_idx(target_category.getGroup_idx());
			//요청 카테고리의 부모아이디를 타깃의 아이디로 변경
			move_category.setParent_category_idx(target_category.getCategory_idx());
		}
		
			//요청 카테고리 업데이트
			service.moveCategory(move_category);
			
			//이동 요청된 카테고리의 자식들
			List<Category> child_Category = service.getCategoryByParentId(move_category.getCategory_idx());
			
			//자식들의 그룹을 요청 카테고리의 그룹으로 변경 및 업데이트
			for (Category child : child_Category) {
				child.setGroup_idx(move_category.getGroup_idx());
				service.moveCategory(child);
			}
			
			res.setMessage("카테고리 이동이 완료되었습니다.");
			

			if (request.getServletContext().getAttribute("admin_Category_list") != null) {
				BaseController.AdminCategoryUpload = false;
			}

		res.setNext_url("/jooka/admin/category/index");
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse insertCategory(HttpServletRequest request, @Valid Category Category, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		String next_url = "/jooka/admin/category/index";
		String success_message = "카테고리가 추가 되었습니다.";
		
		Category parent_Category = service.getCategoryById(Category.getParent_category_idx());
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			if ((Category.getParent_category_idx() == 0 && Category.getGroup_idx() == 0) ||
				(Category.getParent_category_idx() != 0 && parent_Category != null && Category.getGroup_idx() == parent_Category.getGroup_idx())) {
					//추가하려는 카테고리가 최상위가 아닐경우 부모의 그룹아이디를 따라간다.
					service.insertCategory(Category);
					
					//추가하려는 카테고리가 최상위일경우 자신의 아이디를 그룹아이디에 집어넣는다.
					//자신의 아이디는 위에서 이미 insert를 한상태이기 때문에 자동으로 객체에 들어가있다.
					if (Category.getParent_category_idx() == 0) {
						Category.setGroup_idx(Category.getCategory_idx());
						service.moveCategory(Category);
					}
					
					if (request.getServletContext().getAttribute("admin_Category_list") != null) {
						BaseController.AdminCategoryUpload = false;
					}
			} else {
				res.setMessage("잘못된 접근입니다.");
			}
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse editCategory(HttpServletRequest request, @Valid Category Category, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = "/jooka/admin/category/index";
		String success_message = "카테고리가 수정 되었습니다.";
		
		Category parent_Category = service.getCategoryById(Category.getParent_category_idx());
		Category target_Category = service.getCategoryById(Category.getCategory_idx());

		if (!res.validation_data(result, next_url, success_message, res)) {
			if ((Category.getParent_category_idx() == 0 && Category.getGroup_idx() == target_Category.getGroup_idx()) ||
					(Category.getParent_category_idx() != 0 && parent_Category != null && Category.getGroup_idx() == parent_Category.getGroup_idx())) {
					service.updateCategory(Category);
		
					if (request.getServletContext().getAttribute("admin_Category_list") != null) {
						BaseController.AdminCategoryUpload = false;
					}
			} else {
				res.setMessage("잘못된 접근입니다.");
			}
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteCategory(HttpServletRequest request, int category_idx) {
		AjaxResponse res = new AjaxResponse();
		
		if (category_idx <= 0) {
			res.setMessage("잘못된 접근입니다.");
			res.setNext_url("/jooka/admin/category/index");
			res.setProcessing_result(true);
			
			return res;
		} else if (service.getCategoryById(category_idx) == null) {
			res.setMessage("존재하지않거나 삭제된 카테고리입니다.");
			res.setNext_url("/jooka/admin/category/index");
			res.setProcessing_result(true);
			
			return res;
		} else {
			
			//자식 카테고리들 먼저 삭제
			List<Category> childs = service.getCategoryByParentId(category_idx);
			
			for (Category c : childs) {
				service.deleteCategory(c.getCategory_idx());
			}
			
			//카테고리 삭제
			service.deleteCategory(category_idx);
			
			res.setMessage("카테고리가 삭제되었습니다.");
			res.setNext_url("/jooka/admin/category/index");
			res.setProcessing_result(true);
			

			if (request.getServletContext().getAttribute("admin_Category_list") != null) {
				BaseController.AdminCategoryUpload = false;
			}
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/getParentcategory", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getParentCategory(HttpServletRequest request) {
		AjaxResponse res = new AjaxResponse();
		res.setObject(service.getCategoryByStep(0));
		res.setMessage(CategoryMaxDepth +"");
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/getCategoryByStep", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getCategoryByStep(HttpServletRequest request, int category_idx) {
		AjaxResponse res = new AjaxResponse();
		
		if (category_idx == 0) {
			res.setMessage("잘못된 접근입니다.");
		} else {
			res.setObject(service.getCategoryByStep(category_idx));
		}
		
		res.setProcessing_result(true);
		return res;
	}
}
