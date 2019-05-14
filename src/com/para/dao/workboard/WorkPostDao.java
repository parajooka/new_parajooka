package com.para.dao.workboard;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.para.object.workboard.WorkPost;
import com.paraframework.common.PageUtil;

public interface WorkPostDao {
	public int InsertPost(WorkPost post);
	
	public int UpdatePost(WorkPost post);
	
	public int DeletePost(@Param("ids") List<Integer> ids);
	
	public WorkPost getPostById(int id);
	
	public List<WorkPost> getPostByIds(@Param("ids") List<Integer> ids);
	
	public List<WorkPost> getPostByPaging(PageUtil page);
	
	public List<WorkPost> getPostByPagingAndMenu(@Param("menu_idx") int menu_idx, @Param("pageUtil") PageUtil pageUtil);
	
	public int CountPost();
	
	public int CountPostByMenu(int menu_idx);
}
