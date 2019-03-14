package com.para.service.hire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.hire.QnHJoinDao;
import com.para.object.hire.QnHJoin;
import com.para.object.hire.Question;

@Service
public class QnHJoinService {

	@Autowired
	private QnHJoinDao dao;
	
	public int InsertQnH(QnHJoin qnh) {
		// TODO Auto-generated method stub
		return dao.InsertQnH(qnh);
	}

	public int UpdateQnH(QnHJoin qnh) {
		// TODO Auto-generated method stub
		return dao.UpdateQnH(qnh);
	}

	public int DeleteQnH(int hire_id, int question_id) {
		// TODO Auto-generated method stub
		return dao.DeleteQnH(hire_id, question_id);
	}

	public List<Question> getQuestionByQnH(int id) {
		// TODO Auto-generated method stub
		return dao.getQuestionByQnH(id);
	}
	
	public int countQnH(int hire_id, int question_id) {
		
		return dao.countQnH(hire_id, question_id);
	}

}
