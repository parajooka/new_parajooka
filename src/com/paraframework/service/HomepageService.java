package com.paraframework.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraframework.dao.HomepageDao;
import com.paraframework.object.Homepage;

@Service
public class HomepageService {
	
	@Autowired
	private HomepageDao dao;

	public int insertHompage(Homepage homepage) {
		// TODO Auto-generated method stub
		return dao.insertHompage(homepage);
	}

	public int updateHomepage(Homepage homepage) {
		// TODO Auto-generated method stub
		return dao.updateHomepage(homepage);
	}

	public int deleteHomepage(int id) {
		// TODO Auto-generated method stub
		return dao.deleteHomepage(id);
	}

	public Homepage getHomepage() {
		// TODO Auto-generated method stub
		return dao.getHomepage();
	}
}
