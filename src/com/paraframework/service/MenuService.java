package com.paraframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraframework.dao.MenuDao;
import com.paraframework.object.Menu;

@Service
public class MenuService implements MenuDao {
	
	@Autowired
	private MenuDao dao;
	
	public int insertMenu(Menu menu) {
		// TODO Auto-generated method stub
		return dao.insertMenu(menu);
	}

	public int moveMenu(Menu menu) {
		// TODO Auto-generated method stub
		return dao.moveMenu(menu);
	}

	public int deleteMenu(int id) {
		// TODO Auto-generated method stub
		return dao.deleteMenu(id);
	}
	
	public int updateMenu(Menu menu) {
		return dao.updateMenu(menu);
	}

	public List<Menu> getAllMenu() {
		// TODO Auto-generated method stub
		return dao.getAllMenu();
	}

	public List<Menu> getMenuByParentId(int id) {
		// TODO Auto-generated method stub
		return dao.getMenuByParentId(id);
	}
	
	public Menu getMenuById(int id) {
		return dao.getMenuById(id);
	}
	
	public List<Menu> getViewMenu() {
		return dao.getViewMenu();
	}

	@Override
	public Menu getMenuByName(String name) {
		// TODO Auto-generated method stub
		return dao.getMenuByName(name);
	}
	

}
