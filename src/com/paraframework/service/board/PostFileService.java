package com.paraframework.service.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraframework.dao.board.PostFileDao;
import com.paraframework.object.board.PostFile;

@Service
public class PostFileService implements PostFileDao {
	@Autowired
	private PostFileDao dao;

	@Override
	public int InsertPostFile(PostFile file) {
		// TODO Auto-generated method stub
		return dao.InsertPostFile(file);
	}

	@Override
	public int DeletePostFile(int id) {
		// TODO Auto-generated method stub
		return dao.DeletePostFile(id);
	}

	@Override
	public PostFile getPostFileById(int id) {
		// TODO Auto-generated method stub
		return dao.getPostFileById(id);
	}

	@Override
	public List<PostFile> getPostFilesByPostId(int id) {
		// TODO Auto-generated method stub
		return dao.getPostFilesByPostId(id);
	}

	@Override
	public int UpdatePostFile(PostFile file) {
		// TODO Auto-generated method stub
		return dao.UpdatePostFile(file);
	}

}
