package com.paraframework.dao;

import java.util.List;

import com.paraframework.common.PageUtil;
import com.paraframework.object.AccessIp;

public interface AccessIpDao {
	public int insertAccessIp(AccessIp access_ip);

	public int updateAccessIp(AccessIp access_ip);

	public int deleteAccessIp(int id);

	public List<AccessIp> getItemByPage(PageUtil pageUtil);
	
	public List<AccessIp> getAccessip();
	
	public int getAccessIpCount();
	
	public AccessIp getAccessIpById(int id);
}
