package com.paraframework.dao;

import java.util.List;


import com.paraframework.object.Menu;

public interface MenuDao {
	public int insertMenu(Menu menu);
	
	public int moveMenu(Menu menu);
	
	public int updateMenu(Menu menu);
	
	public int deleteMenu(int id);
	
	public Menu getMenuByName(String name);
	
	public List<Menu> getAllMenu();
	
	public List<Menu> getMenuByParentId(int id);
	
	public Menu getMenuById(int id);
	
	public List<Menu> getViewMenu();
	
	public List<Menu> getMenuByType(int type);
}
