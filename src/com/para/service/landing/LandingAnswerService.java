package com.para.service.landing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.landing.LandingAnswerDao;
import com.para.object.landing.LandingAnswer;

@Service
public class LandingAnswerService implements LandingAnswerDao {
	@Autowired
	private LandingAnswerDao dao;

	@Override
	public int InsertAnswer(LandingAnswer answer) {
		// TODO Auto-generated method stub
		return dao.InsertAnswer(answer);
	}

	@Override
	public int UpdateAnswer(LandingAnswer answer) {
		// TODO Auto-generated method stub
		return dao.UpdateAnswer(answer);
	}

	@Override
	public int DeleteAnswer(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteAnswer(id);
	}

	@Override
	public List<LandingAnswer> getAnswerByQuestionId(int id) {
		// TODO Auto-generated method stub
		return dao.getAnswerByQuestionId(id);
	}

	@Override
	public List<LandingAnswer> getAnswerByChain(int id) {
		// TODO Auto-generated method stub
		return dao.getAnswerByChain(id);
	}

	@Override
	public int ValidtaionAnswerId(int id) {
		// TODO Auto-generated method stub
		return dao.ValidtaionAnswerId(id);
	}

	@Override
	public LandingAnswer getAnswerById(int id) {
		// TODO Auto-generated method stub
		return dao.getAnswerById(id);
	}

}
