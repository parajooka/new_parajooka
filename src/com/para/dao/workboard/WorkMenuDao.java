package com.para.dao.workboard;

import java.util.List;

import com.para.object.workboard.WorkMenu;

public interface WorkMenuDao {
	public int insertMenu(WorkMenu menu);
	
	public int moveMenu(WorkMenu menu);
	
	public int updateMenu(WorkMenu menu);
	
	public int deleteMenu(int id);
	
	public WorkMenu getMenuByName(String name);
	
	public List<WorkMenu> getAllMenu();
	
	public List<WorkMenu> getMenuByParentId(int id);
	
	public WorkMenu getMenuById(int id);
	
	public List<WorkMenu> getViewMenu();
	
	public List<WorkMenu> getParentMenu();
}
