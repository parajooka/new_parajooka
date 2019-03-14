package com.paraframework.dao.board;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.paraframework.common.PageUtil;
import com.paraframework.object.board.Post;

public interface PostDao {
	public int InsertPost(Post post);
	
	public int UpdatePost(Post post);
	
	public int UpdateViewCount(int id);
	
	public int DeletePost(int id);
	
	public Post getPostById(int id);
	
	public Post getMobilePostById(int id);
	
	public List<Post> getPostByPaging(@Param("pageUtil") PageUtil pageUtil, @Param("menu_id") int menu_id);
	
	public List<Post> getNextBeforePostById(@Param("post_id") int post_id, @Param("menu_id") int menu_id);
	
	public int CountPostByMenuId(int id);
	
	public List<String> getTerm(@Param("menu_id") String menu_id);
	
	public List<Post> getPostByYear(@Param("minDate") String minDate, @Param("maxDate") String maxDate, @Param("menu_id") String menu_id);
}
	
