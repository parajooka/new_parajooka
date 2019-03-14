package com.paraframework.dao.board;

import java.util.List;

import com.paraframework.object.board.PostComment;

public interface PostCommentDao {
	public int InserComment(PostComment comment);
	
	public int UpdateComment(PostComment comment);
	
	public int DeleteComment(int id);
	
	public PostComment getCommentById(int id);
	
	public List<PostComment> getCommentByPostId(int id);
	
	public List<PostComment> getParentcommentByPostId(int id);
}
