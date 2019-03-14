package com.paraframework.dao;



import com.paraframework.object.Homepage;


public interface HomepageDao {
	public int insertHompage(Homepage homepage);

	public int updateHomepage(Homepage homepage);
	
	public int deleteHomepage(int id);
	
	public Homepage getHomepage();
}
