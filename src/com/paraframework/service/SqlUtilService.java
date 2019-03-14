package com.paraframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraframework.common.SqlUtil;
import com.paraframework.dao.SqlDao;

@Service
public class SqlUtilService {
	@Autowired
	private SqlDao dao;

	public List<Integer> SearchObjectId(SqlUtil util) {
		// TODO Auto-generated method stub
		return dao.SearchObjectId(util);
	}

}
