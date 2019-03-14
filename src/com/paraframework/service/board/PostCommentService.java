package com.paraframework.service.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraframework.dao.board.PostCommentDao;
import com.paraframework.object.board.PostComment;

@Service
public class PostCommentService implements PostCommentDao {
	@Autowired
	private PostCommentDao dao;

	@Override
	public int InserComment(PostComment comment) {
		// TODO Auto-generated method stub
		return dao.InserComment(comment);
	}

	@Override
	public int UpdateComment(PostComment comment) {
		// TODO Auto-generated method stub
		return dao.UpdateComment(comment);
	}

	@Override
	public int DeleteComment(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteComment(id);
	}

	@Override
	public PostComment getCommentById(int id) {
		// TODO Auto-generated method stub
		return dao.getCommentById(id);
	}

	@Override
	public List<PostComment> getCommentByPostId(int id) {
		// TODO Auto-generated method stub
		return dao.getCommentByPostId(id);
	}

	@Override
	public List<PostComment> getParentcommentByPostId(int id) {
		// TODO Auto-generated method stub
		return dao.getParentcommentByPostId(id);
	}
}
