package com.paraframework.dao;

import java.util.List;

import com.paraframework.object.Category;


public interface CategoryDao {
	public int insertCategory(Category category);
	
	public int moveCategory(Category category);
	
	public int updateCategory(Category category);
	
	public int deleteCategory(int id);
	
	public List<Category> getAllCategory();
	
	public List<Category> getCategoryByParentId(int id);
	
	public Category getCategoryById(int id);
	
	public List<Category> getViewCategory();
	
	public List<Category> getCategoryByStep(int id);
}
