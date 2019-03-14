package com.paraframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraframework.dao.CategoryDao;
import com.paraframework.object.Category;


@Service
public class CategoryService {
	
	@Autowired
	private CategoryDao dao;
	
	public int insertCategory(Category category) {
		// TODO Auto-generated method stub
		return dao.insertCategory(category);
	}

	public int moveCategory(Category category) {
		// TODO Auto-generated method stub
		return dao.moveCategory(category);
	}

	public int deleteCategory(int id) {
		// TODO Auto-generated method stub
		return dao.deleteCategory(id);
	}
	
	public int updateCategory(Category category) {
		return dao.updateCategory(category);
	}

	public List<Category> getAllCategory() {
		// TODO Auto-generated method stub
		return dao.getAllCategory();
	}

	public List<Category> getCategoryByParentId(int id) {
		// TODO Auto-generated method stub
		return dao.getCategoryByParentId(id);
	}
	
	public Category getCategoryById(int id) {
		return dao.getCategoryById(id);
	}
	
	public List<Category> getViewCategory() {
		return dao.getViewCategory();
	}
	
	public List<Category> getCategoryByStep(int id) {
		return dao.getCategoryByStep(id);
	}
}
