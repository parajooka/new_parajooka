package com.paraframework.service.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraframework.common.PageUtil;
import com.paraframework.dao.board.PostDao;
import com.paraframework.object.board.Post;

@Service
public class PostService implements PostDao {
	@Autowired
	private PostDao dao;

	@Override
	public int InsertPost(Post post) {
		// TODO Auto-generated method stub
		return dao.InsertPost(post);
	}

	@Override
	public int UpdatePost(Post post) {
		// TODO Auto-generated method stub
		return dao.UpdatePost(post);
	}

	@Override
	public int DeletePost(int id) {
		// TODO Auto-generated method stub
		return dao.DeletePost(id);
	}

	@Override
	public Post getPostById(int id) {
		// TODO Auto-generated method stub
		return dao.getPostById(id);
	}

	@Override
	public List<Post> getPostByPaging(PageUtil pageUtil, int menu_id) {
		// TODO Auto-generated method stub
		return dao.getPostByPaging(pageUtil, menu_id);
	}

	@Override
	public int UpdateViewCount(int id) {
		// TODO Auto-generated method stub
		return dao.UpdateViewCount(id);
	}

	@Override
	public int CountPostByMenuId(int id) {
		// TODO Auto-generated method stub
		return dao.CountPostByMenuId(id);
	}

	@Override
	public List<Post> getNextBeforePostById(int post_id, int menu_id) {
		// TODO Auto-generated method stub
		return dao.getNextBeforePostById(post_id, menu_id);
	}

	@Override
	public List<String> getTerm(String menu_id) {
		// TODO Auto-generated method stub
		return dao.getTerm(menu_id);
	}

	@Override
	public List<Post> getPostByYear(String minDate, String maxDate, String menu_id) {
		// TODO Auto-generated method stub
		return dao.getPostByYear(minDate, maxDate, menu_id);
	}

	@Override
	public Post getMobilePostById(int id) {
		// TODO Auto-generated method stub
		return dao.getMobilePostById(id);
	}

}
