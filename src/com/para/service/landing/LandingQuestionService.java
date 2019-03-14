package com.para.service.landing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.landing.LandingQuestionDao;
import com.para.object.landing.LandingQuestion;
import com.paraframework.common.PageUtil;

@Service
public class LandingQuestionService implements LandingQuestionDao {
	@Autowired
	private LandingQuestionDao dao;

	@Override
	public int InsertQuestion(LandingQuestion question) {
		// TODO Auto-generated method stub
		return dao.InsertQuestion(question);
	}

	@Override
	public int UpdateQuestion(LandingQuestion question) {
		// TODO Auto-generated method stub
		return dao.UpdateQuestion(question);
	}

	@Override
	public int DeleteQuestion(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteQuestion(id);
	}

	@Override
	public int CountQuestion() {
		// TODO Auto-generated method stub
		return dao.CountQuestion();
	}

	@Override
	public List<LandingQuestion> getQuestionByAnswerId(int answer_id) {
		// TODO Auto-generated method stub
		return dao.getQuestionByAnswerId(answer_id);
	}

	@Override
	public List<LandingQuestion> getQuestionByPaging(PageUtil paging) {
		// TODO Auto-generated method stub
		return dao.getQuestionByPaging(paging);
	}

	@Override
	public LandingQuestion getQuestionById(int id) {
		// TODO Auto-generated method stub
		return dao.getQuestionById(id);
	}

	@Override
	public List<LandingQuestion> getParentQuestionById(int question_id) {
		// TODO Auto-generated method stub
		return dao.getParentQuestionById(question_id);
	}

	@Override
	public int ValidationQuestion(int id) {
		// TODO Auto-generated method stub
		return dao.ValidationQuestion(id);
	}

}
