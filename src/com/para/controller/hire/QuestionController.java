package com.para.controller.hire;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.para.object.hire.Answer;
import com.para.object.hire.Question;
import com.para.service.hire.AnswerService;
import com.para.service.hire.QuestionService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.common.PageUtil;

@Controller
@RequestMapping(value=ControllerCommonMethod.admin_page_path + "/question")
public class QuestionController extends ControllerCommonMethod {
	
	@Autowired
	private QuestionService question_service;
	
	@Autowired
	private AnswerService answer_service;
	
	@Override
	public String Index(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String Question(HttpServletRequest request, HttpServletResponse response, Question question, Model model) {
		request.setAttribute("menu", "질문 및 문제 관리");
		model.addAttribute("question", question);
		
		int count = question_service.getCount();
		
		request.setAttribute("question_count", count);
		PageUtil page = AutoPaging(request, response, count);
		request.setAttribute("questions", question_service.getQuestionByPage(page));
		request.setAttribute("pageUtil", page);
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse InsertQuestin(HttpServletRequest request, @Valid Question question, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = ControllerCommonMethod.admin_page_path + "/question/index";
		String success_message = "질문(문제)이 정상적으로 등록 되었습니다.";
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			int block_count = Integer.parseInt(request.getParameter("answer_count"));
			Answer answers = new Answer();
			
			String min_content = request.getParameter("answer_1_content");
			
			if (block_count == 1 && (min_content == null || min_content.length() < 1)) {
				res.setMessage("질문 및 문제에는 최소한 1개의 정답은 필요합니다.");
				res.setProcessing_result(true);
				return res;
			}
			
			//question을 먼저 db에 등록
			question_service.InsertQuestion(question);
			question.setExample(ManageSmartEditorImg(question.getExample(), "C:/res/img/admin/hire/question/" + question.getId()));
			question_service.UpdateQuestion(question);
			
			for (int i = 1; i <= block_count; i++) {
				String answer = request.getParameter("answer_"+ i);
				String content = request.getParameter("answer_"+ i +"_content");
				
				answers.setQuestion_id(question.getId());
				
				if (content == null || content.length() < 1) {
					//글자수가 1미만일 경우 무시
					continue;
				} else if (question.getType() == 1) {
					//문제가 주관식일 경우 그냥 무조건 다 정답처리
					answers.setReply(0);
					answers.setContent(content);
				} else if (answer == null || answer.length() < 1) {
					//콘텐츠는 있으나 정답이 없을경우 오답으로 지정
					answers.setReply(1);
					answers.setContent(content);
				} else {
					//정답으로 지정된 경우
					answers.setReply(0);
					answers.setContent(content);
					answers.setQuestion_id(question.getId());
				}
				
				answer_service.InsertAnswer(answers);
			}
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/get", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getQuestion(HttpServletRequest request, int id) {
		AjaxResponse res = new AjaxResponse();
		
		Question question = question_service.getQuestionById(id);
		question.setAnswers(answer_service.getAnswerByQuestionId(id));
		
		res.setObject(question);
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/delete_answer", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteAnswer(HttpServletRequest request, int id) {
		AjaxResponse res = new AjaxResponse();
		
		if (answer_service.CountAnswerById(id) <= 0) {
			res.setMessage("존재하지 않거나 삭제된 답안입니다.");
		} else {
			answer_service.DeleteAnswer(id);
			res.setMessage("해당 답안이 삭제 되었습니다.");
		}

		res.setNext_url(ControllerCommonMethod.admin_page_path + "/question/index");
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteQuestion(HttpServletRequest request, int id) {
		AjaxResponse res = new AjaxResponse();
		
		if (question_service.getQuestionById(id) == null) {
			res.setMessage("존재하지 않거나 삭제된  문제입니다.");
		} else {
			removeDirectory("C:\\res\\img\\admin\\hire\\question\\"+ id);
			question_service.DeleteQuestion(id);
			res.setMessage("해당 문제가 삭제 되었습니다.");
		}
		
		res.setNext_url(ControllerCommonMethod.admin_page_path + "/question/index");
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse updateQuestion(HttpServletRequest request, @Valid Question question, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = ControllerCommonMethod.admin_page_path + "/question/index";
		String success_message = "질문(문제)이 정상적으로 수정 되었습니다.";
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			List<Answer> answer_list = answer_service.getAnswerByQuestionId(question.getId());
			
			//업데이트 하려는 문제의 정답 리스트들을 불러온다.
			//정답 리스트가 존재하지 않을경우 새로 입력한 답안이 있는지 체크한다.
			int block_count = Integer.parseInt(request.getParameter("answer_count"));
			
			
			//등록된 답안이 없을경우
			if (answer_list == null || answer_list.size() == 0) {
				String min_content = request.getParameter("answer_1_content");
				//등록된 답안도 없고 새로 추가한 답안도 없을 경우
				if (min_content == null || min_content.length() < 1) {
					res.setMessage("질문 및 문제에는 최소한 1개의 정답은 필요합니다.");
					res.setProcessing_result(true);
					return res;
				} else {
					//등록된 답안은 없지만 새로 추가한 답안이 있을 경우 -> 답안 등록
					Answer answers = new Answer();
					for (int i = 1; i <= block_count; i++) {
						String answer = request.getParameter("answer_"+ i);
						String content = request.getParameter("answer_"+ i +"_content");

						answers.setQuestion_id(question.getId());
						
						if (content == null || content.length() < 1) {
							//글자수가 1미만일 경우 무시
							continue;
						} else if (question.getType() == 1) {
							//문제가 주관식일 경우 그냥 무조건 다 정답처리
							answers.setReply(0);
							answers.setContent(content);
						} else if (answer == null || answer.length() < 1) {
							//콘텐츠는 있으나 정답이 없을경우 오답으로 지정
							answers.setReply(1);
							answers.setContent(content);
						} else {
							//정답으로 지정된 경우
							answers.setReply(0);
							answers.setContent(content);
						}
						
						answer_service.InsertAnswer(answers);
					}
				}
			} else {
				//기존 답안 업데이트 시작.
				Answer answers = new Answer();

				//문제에 등록된 답안이 db에 존재할 경우
				//존재하는 답안을 먼저 업데이트한 후에 새로운 답안이 있을시 등록한다.
				for (Answer a : answer_list) {
					//업데이트를 위한 ID를 설정해준다.
					answers.setId(a.getId());
					answers.setQuestion_id(question.getId());
					
					String answer = request.getParameter("answer_"+ a.getId());
					String content = request.getParameter("answer_"+ a.getId() +"_content");
					
					//답안의 내용이 비어있다면 업데이트를 하지않고 다음 답안으로 넘어간다.
					if (content == null || content.length() < 1) {
						continue;
					} else if (question.getType() == 1) {
						//문제가 주관식일 경우 그냥 무조건 다 정답처리
						answers.setReply(0);
						answers.setContent(content);
					} else if (answer == null || answer.length() < 1) {
						//콘텐츠는 있으나 정답이 없을경우 오답으로 지정
						answers.setReply(1);
						answers.setContent(content);
					} else {
						//정답으로 지정된 경우
						answers.setReply(0);
						answers.setContent(content);
					}
					
					answer_service.UpdateAnswer(answers);
					
				}
				
				//입력한 신규 답안이 존재하는지 체크
				if (block_count > 0) {
					//신규 답안 등록 시작
					for (int i = 1; i <= block_count; i++) {
						String answer = request.getParameter("answer_"+ i);
						String content = request.getParameter("answer_"+ i +"_content");

						answers.setQuestion_id(question.getId());
						
						if (content == null || content.length() < 1) {
							//글자수가 1미만일 경우 무시
							continue;
						} else if (question.getType() == 1) {
							//문제가 주관식일 경우 그냥 무조건 다 정답처리
							answers.setReply(0);
							answers.setContent(content);
						} else if (answer == null || answer.length() < 1) {
							//콘텐츠는 있으나 정답이 없을경우 오답으로 지정
							answers.setReply(1);
							answers.setContent(content);
						} else {
							//정답으로 지정된 경우
							answers.setReply(0);
							answers.setContent(content);
						}
						
						answer_service.InsertAnswer(answers);
					}
				}
				//신규 답안 등록 완료.
				
			}
			//답안 등록 종료

			question.setExample(ManageSmartEditorImg(question.getExample(), "C:/res/img/admin/hire/question/" + question.getId()));
			question_service.UpdateQuestion(question);
		}
		
		res.setProcessing_result(true);
		
		return res;
	}

}
