package com.para.service.landing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.landing.LandingQuestionChainDao;
import com.para.object.landing.LandingQuestionChain;

@Service
public class LandingQuestionChainService implements LandingQuestionChainDao {
	@Autowired
	private LandingQuestionChainDao dao;

	@Override
	public int InsertChain(LandingQuestionChain chain) {
		// TODO Auto-generated method stub
		return dao.InsertChain(chain);
	}

	@Override
	public int UpdateChain(LandingQuestionChain chain) {
		// TODO Auto-generated method stub
		return dao.UpdateChain(chain);
	}

	@Override
	public int DeleteChain(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteChain(id);
	}

	@Override
	public LandingQuestionChain getChainById(int id) {
		// TODO Auto-generated method stub
		return dao.getChainById(id);
	}

}
