package com.para.service.workboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.workboard.WorkPostDao;
import com.para.object.workboard.WorkPost;
import com.paraframework.common.PageUtil;

@Service
public class WorkPostService implements WorkPostDao {
	
	@Autowired
	private WorkPostDao dao;

	@Override
	public int InsertPost(WorkPost post) {
		// TODO Auto-generated method stub
		return dao.InsertPost(post);
	}

	@Override
	public int UpdatePost(WorkPost post) {
		// TODO Auto-generated method stub
		return dao.UpdatePost(post);
	}

	@Override
	public int DeletePost(List<Integer> ids) {
		// TODO Auto-generated method stub
		return dao.DeletePost(ids);
	}

	@Override
	public WorkPost getPostById(int id) {
		// TODO Auto-generated method stub
		return dao.getPostById(id);
	}

	@Override
	public List<WorkPost> getPostByPaging(PageUtil page) {
		// TODO Auto-generated method stub
		return dao.getPostByPaging(page);
	}

	@Override
	public List<WorkPost> getPostByPagingAndMenu(int menu_idx, PageUtil pageUtil) {
		// TODO Auto-generated method stub
		return dao.getPostByPagingAndMenu(menu_idx, pageUtil);
	}

	@Override
	public int CountPost() {
		// TODO Auto-generated method stub
		return dao.CountPost();
	}

	@Override
	public int CountPostByMenu(int menu_idx) {
		// TODO Auto-generated method stub
		return dao.CountPostByMenu(menu_idx);
	}

	@Override
	public List<WorkPost> getPostByIds(List<Integer> ids) {
		// TODO Auto-generated method stub
		return dao.getPostByIds(ids);
	}

	@Override
	public int movePost(List<Integer> ids, int menu_idx) {
		// TODO Auto-generated method stub
		return dao.movePost(ids, menu_idx);
	}
}
