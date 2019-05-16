package com.para.service.workboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.workboard.WorkMenuDao;
import com.para.object.workboard.WorkMenu;

@Service
public class WorkMenuService implements WorkMenuDao {
	
	@Autowired
	private WorkMenuDao dao;

	@Override
	public int insertMenu(WorkMenu menu) {
		// TODO Auto-generated method stub
		return dao.insertMenu(menu);
	}

	@Override
	public int moveMenu(WorkMenu menu) {
		// TODO Auto-generated method stub
		return dao.moveMenu(menu);
	}

	@Override
	public int updateMenu(WorkMenu menu) {
		// TODO Auto-generated method stub
		return dao.updateMenu(menu);
	}

	@Override
	public int deleteMenu(int id) {
		// TODO Auto-generated method stub
		return dao.deleteMenu(id);
	}

	@Override
	public WorkMenu getMenuByName(String name) {
		// TODO Auto-generated method stub
		return dao.getMenuByName(name);
	}

	@Override
	public List<WorkMenu> getAllMenu() {
		// TODO Auto-generated method stub
		return dao.getAllMenu();
	}

	@Override
	public List<WorkMenu> getMenuByParentId(int id) {
		// TODO Auto-generated method stub
		return dao.getMenuByParentId(id);
	}

	@Override
	public WorkMenu getMenuById(int id) {
		// TODO Auto-generated method stub
		return dao.getMenuById(id);
	}

	@Override
	public List<WorkMenu> getViewMenu() {
		// TODO Auto-generated method stub
		return dao.getViewMenu();
	}

	@Override
	public List<WorkMenu> getParentMenu() {
		// TODO Auto-generated method stub
		return dao.getParentMenu();
	}
}
