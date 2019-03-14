package com.paraframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraframework.common.PageUtil;
import com.paraframework.dao.AccessIpDao;
import com.paraframework.object.AccessIp;

@Service
public class AccessIpService {
	
	@Autowired
	private AccessIpDao dao;
	
	public int insertAccessIp(AccessIp access_ip) {
		// TODO Auto-generated method stub
		return dao.insertAccessIp(access_ip);
	}

	public int updateAccessIp(AccessIp access_ip) {
		// TODO Auto-generated method stub
		return dao.updateAccessIp(access_ip);
	}

	public int deleteAccessIp(int id) {
		// TODO Auto-generated method stub
		return dao.deleteAccessIp(id);
	}

	public List<AccessIp> getItemByPage(PageUtil pageUtil) {
		// TODO Auto-generated method stub
		return dao.getItemByPage(pageUtil);
	}
	
	public List<AccessIp> getAccessIp() {
		return dao.getAccessip();
	}
	
	public int getAccessIpCount() {
		return dao.getAccessIpCount();
	}
	
	public AccessIp getAccessIpById(int id) {
		return dao.getAccessIpById(id);
	}
	

}
