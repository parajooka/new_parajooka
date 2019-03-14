package com.para.service.hire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.hire.AnswerDao;
import com.para.object.hire.Answer;

@Service
public class AnswerService {
	@Autowired
	private AnswerDao dao;

	public int InsertAnswer(Answer answer) {
		// TODO Auto-generated method stub
		return dao.InsertAnswer(answer);
	}

	public int UpdateAnswer(Answer answer) {
		// TODO Auto-generated method stub
		return dao.UpdateAnswer(answer);
	}

	public int DeleteAnswer(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteAnswer(id);
	}
	
	public List<Answer> getAnswerByQuestionId(int id) {
		return dao.getAnswerByQuestionId(id);
	}
	
	public int CountAnswerById(int id) {
		return dao.CountAnswerById(id);
	}

	
}
