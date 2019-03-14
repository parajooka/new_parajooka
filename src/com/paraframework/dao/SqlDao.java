package com.paraframework.dao;

import java.util.List;


import com.paraframework.common.SqlUtil;

public interface SqlDao {

	public List<Integer> SearchObjectId(SqlUtil util);
	
}
