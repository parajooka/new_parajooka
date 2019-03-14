package com.paraframework.dao.board;

import java.util.List;

import com.paraframework.object.board.PostFile;

public interface PostFileDao {
	public int InsertPostFile(PostFile file);
	
	public int UpdatePostFile(PostFile file);
	
	public int DeletePostFile(int id);
	
	public PostFile getPostFileById(int id);
	
	public List<PostFile> getPostFilesByPostId(int id);
}
