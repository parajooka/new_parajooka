package com.para.service.hire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.hire.QuestionDao;
import com.para.object.hire.Question;
import com.paraframework.common.PageUtil;

@Service
public class QuestionService {
	@Autowired
	private QuestionDao dao;

	public int InsertQuestion(Question question) {
		// TODO Auto-generated method stub
		return dao.InsertQuestion(question);
	}

	public int UpdateQuestion(Question question) {
		// TODO Auto-generated method stub
		return dao.UpdateQuestion(question);
	}

	public int DeleteQuestion(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteQuestion(id);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return dao.getCount();
	}

	public List<Question> getQuestionByPage(PageUtil page) {
		// TODO Auto-generated method stub
		return dao.getQuestionByPage(page);
	}
	
	public Question getQuestionById (int id) {
		return dao.getQuestionById(id);
	}
	
}
