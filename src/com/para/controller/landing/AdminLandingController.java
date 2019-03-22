package com.para.controller.landing;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.para.object.landing.LandingAnswer;
import com.para.object.landing.LandingQuestion;
import com.para.object.landing.LandingQuestionChain;
import com.para.service.landing.LandingAnswerService;
import com.para.service.landing.LandingQuestionChainService;
import com.para.service.landing.LandingQuestionService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.common.PageUtil;

import edu.emory.mathcs.backport.java.util.Arrays;

@Controller
@RequestMapping(value="/jooka/admin/landing")
public class AdminLandingController extends ControllerCommonMethod {
	@Autowired
	private LandingQuestionService question_service;
	@Autowired
	private LandingQuestionChainService chain_service;
	@Autowired
	private LandingAnswerService answer_service;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String Landing(HttpServletRequest request, HttpServletResponse response, PageUtil page) {
		request.setAttribute("menu", "랜딩페이지 문항 관리");
		//검색 조건 부여 -- 시작
		//검색 옵션 지정
			Map<String, String> search_items = new LinkedHashMap<String, String>();
			search_items.put("문항(제목)", "question_title");
			search_items.put("내용", "question_contents");
		//검색 조건 부여 -- 종료
		
		LocationSearchCheck(request, response, "landingquestion", "question_id", search_items, new LocationSearchResultFunction() {
			@Override
			public void SearchTrue(int result, List<Integer> column_value) {
				// TODO Auto-generated method stub
				List<LandingQuestion> question_list = new ArrayList<>();
				
				for (int i : column_value) {
					question_list.add(question_service.getQuestionById(i));
				}	
				
				request.setAttribute("question_list", question_list);
				request.setAttribute("question_count", result);
			}
			
			@Override
			public void SearchFalse() {
				int SearchCount = question_service.CountQuestion();
				List<LandingQuestion>  question_list = question_service.getQuestionByPaging(AutoPaging(request, response, SearchCount));
				
				request.setAttribute("question_list", question_list);
				request.setAttribute("question_count", SearchCount);
			}
		});
		return getRequestURI(request);
	}
	
	
	
	/**
	 * 문항 삭제
	 * @param request
	 * @param response
	 * @param delete_targets
	 * @return
	 */
	@RequestMapping(value="/delete/question", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse DeleteQuestion(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="delete_question_targets[]") List<String> delete_targets) {
		AjaxResponse res = new AjaxResponse();
		
		
		try {
			for (String key : delete_targets) {
				removeDirectory("C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ key);
				question_service.DeleteQuestion(Integer.parseInt(key));
			}
			
			res.setMessage("선택한 문항들이 정상적으로 삭제되었습니다.");
			res.setNext_url("/jooka/admin/landing/index");
		} catch (Exception e) {
			alertMessage("잘못된 접근입니다.", request, response);
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	/**
	 * 문항 관리페이지에서 하위 문항들을 호출하는 메서드
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/get/question/byAnswerId", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getQuestionByAnswerId(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		
		String answer_id = request.getParameter("answer_id");
		
		if (answer_id == null || answer_id.length() == 0) {
			res.setMessage("잘못된 접근입니다.");
			res.setNext_url("/jooka/admin/landing/index");
			res.setProcessing_result(true);
			return res;
		}
		
		try {
			List<LandingQuestion> questions = question_service.getQuestionByAnswerId(Integer.parseInt(answer_id));
			res.setObject(questions);
		} catch (Exception e) {
			// TODO: handle exception
			res.setMessage("존재하지않거나 삭제된 답안입니다.");
			res.setNext_url("/jooka/admin/landing/index");
			res.setProcessing_result(true);
			return res;
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	/**
	 * 문항관리페이지
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/question_manage", method=RequestMethod.GET)
	public String ManageQuestion(HttpServletRequest request, HttpServletResponse response) {
		String question_idx = request.getParameter("question_id");
		
		if (question_idx == null || question_idx.length() == 0) {
			alertMessage("존재하지 않거나 삭제된 문항입니다.", request, response);
			return null;
		}
		
		int question_id = 0;
		try {
			question_id = Integer.parseInt(question_idx);
		} catch (Exception e) {
			// TODO: handle exception
			alertMessage("잘못된 문항 주소입니다.", request, response);
		}
		
		LandingQuestion question = question_service.getQuestionById(question_id);
		if (question == null) {
			alertMessage("존재하지 않거나 삭제된 문항입니다.", request, response);
			return null;
		} else {
			request.setAttribute("landing_question", question);
			request.setAttribute("menu", "'"+question.getQuestion_title() + "' 관리");
			request.setAttribute("lading_answer", answer_service.getAnswerByQuestionId(question_id));
			
			List<LandingQuestion> question_list = question_service.getParentQuestionById(question_id);
			request.setAttribute("parent_landing_question", question_list);
			request.setAttribute("parent_answer_list", answer_service.getAnswerByChain(question_id));
			
			//검색 조건 부여 -- 시작
			//검색 옵션 지정
				Map<String, String> search_items = new LinkedHashMap<String, String>();
				search_items.put("문항(제목)", "question_title");
				search_items.put("내용", "question_contents");
			//검색 조건 부여 -- 종료
			
			LocationSearchCheck(request, response, "landingquestion", "question_id", search_items, new LocationSearchResultFunction() {
				@Override
				public void SearchTrue(int result, List<Integer> column_value) {
					// TODO Auto-generated method stub
					List<LandingQuestion> question_list = new ArrayList<>();
					
					for (int i : column_value) {
						question_list.add(question_service.getQuestionById(i));
					}	
					
					request.setAttribute("question_list", question_list);
					request.setAttribute("question_count", result);
				}
				
				@Override
				public void SearchFalse() {
					int SearchCount = question_service.CountQuestion();
					List<LandingQuestion>  question_list = question_service.getQuestionByPaging(AutoPaging(request, response, SearchCount));
					
					request.setAttribute("question_list", question_list);
					request.setAttribute("question_count", SearchCount);
				}
			});
			//검색 조건 부여 -- 종료
		}

		return getRequestURI(request);
	}
	
	/**
	 * 문항 관리페이지에서 새로운 부모문항을 추가하는 메서드
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/add/parent_question", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse AddParentQuestion(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		
		String question_id = request.getParameter("question_id");
		String answer_id = request.getParameter("answer_id");
		
		if (question_id == null || question_id.length() == 0 || answer_id == null || answer_id.length() == 0) {
			res.setMessage("잘못된 접근입니다.");
			res.setNext_url("/jooka/admin/landing/index");
			res.setProcessing_result(true);
			
			return res;
		}
		
		try {
			LandingQuestionChain chain = new LandingQuestionChain();
			chain.setQuestion_id(Integer.parseInt(question_id));
			chain.setAnswer_id(Integer.parseInt(answer_id));
			
			chain_service.InsertChain(chain);
			
			res.setMessage("해당 문항에 새로운 상위(부모)문항이 정상적으로 추가되었습니다.");
			res.setNext_url("/jooka/admin/landing/question_manage?question_id="+ question_id);
		} catch(Exception e) {
			res.setMessage("존재하지 않거나 삭제된 문항 혹은 답안입니다.");
			res.setNext_url("/jooka/admin/landing/index");
			res.setProcessing_result(true);
			
			return res;
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	/**
	 * 문항 등록 혹은 수정 페이지 이동메서드
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/question_injection", method=RequestMethod.GET)
	public String InjectionQuestion(HttpServletRequest request, HttpServletResponse response) {
		String answer_id = (String) request.getParameter("answer_id");
		String question_id = (String) request.getParameter("question_id");
		
		request.setAttribute("answer_id", answer_id);
		request.setAttribute("question_id", question_id);

		if (question_id != null && question_id.length() > 0) {
			request.setAttribute("menu", "문항 수정");
		} else if (answer_id == null || answer_id.length() == 0) {
			request.setAttribute("menu", "신규 문항 등록");
		} else {
			request.setAttribute("menu", "신규 하위문항 등록");
		}
		
		if (question_id != null && question_id.length() > 0) {
			try {
				request.setAttribute("question", question_service.getQuestionById(Integer.parseInt(question_id)));
				request.setAttribute("answers", answer_service.getAnswerByQuestionId(Integer.parseInt(question_id)));
			} catch (Exception e) {
				// TODO: handle exception
				alertMessage("존재하지 않거나 삭제된 문항입니다.", request, response);
			}
		}
		
		//검색 조건 부여 -- 시작
		//검색 옵션 지정
			Map<String, String> search_items = new LinkedHashMap<String, String>();
			search_items.put("문항(제목)", "question_title");
			search_items.put("내용", "question_contents");
		//검색 조건 부여 -- 종료
		
		LocationSearchCheck(request, response, "landingquestion", "question_id", search_items, new LocationSearchResultFunction() {
			@Override
			public void SearchTrue(int result, List<Integer> column_value) {
				// TODO Auto-generated method stub
				List<LandingQuestion> question_list = new ArrayList<>();
				
				for (int i : column_value) {
					question_list.add(question_service.getQuestionById(i));
				}	
				
				request.setAttribute("question_list", question_list);
				request.setAttribute("question_count", result);
			}
			
			@Override
			public void SearchFalse() {
				int SearchCount = question_service.CountQuestion();
				List<LandingQuestion>  question_list = question_service.getQuestionByPaging(AutoPaging(request, response, SearchCount));
				
				request.setAttribute("question_list", question_list);
				request.setAttribute("question_count", SearchCount);
			}
		});
		//검색 조건 부여 -- 종료
		
		request.getSession().setAttribute("fileUploadPath", "C:/res/img/admin/landing/question_content_img");
		return getRequestURI(request);
	}
	
	/**
	 * 문항 등록 혹은 수정 페이지에서 부모 문제를 선택하기위해 호출하는 메서드
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/question_injection/get/question", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getQuestionByPaging(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		PageUtil page = (PageUtil) request.getSession().getAttribute("paging");
		if (page == null || request.getParameter("page") == null) {
			res.setMessage("잘못된 접근입니다.");
			res.setNext_url("/jooka/admin/landing/question_injection");
			res.setProcessing_result(true);
			return res;
		} else {
			int request_page = Integer.parseInt(request.getParameter("page"));
			if (request_page < 1) {
				res.setMessage("이전 페이지가 존재하지않습니다.");
				res.setProcessing_result(true);
				return res;
			} else if (request_page > page.getTotal_page()) {
				res.setMessage("다음 페이지가 존재하지 않습니다.");
				res.setProcessing_result(true);
				return res;
			}
		}
		
		//검색 조건 부여 -- 시작
			//검색 옵션 지정
				Map<String, String> search_items = new LinkedHashMap<String, String>();
				search_items.put("문항(제목)", "question_title");
				search_items.put("내용", "question_contents");
			//검색 조건 부여 -- 종료
			
			LocationSearchCheck(request, response, "landingquestion", "question_id", search_items, new LocationSearchResultFunction() {
				@Override
				public void SearchTrue(int result, List<Integer> column_value) {
					// TODO Auto-generated method stub
					List<LandingQuestion> question_list = new ArrayList<>();
					
					for (int i : column_value) {
						question_list.add(question_service.getQuestionById(i));
					}	
					
					PageUtil pge = (PageUtil) request.getSession().getAttribute("paging");
					res.setObject(question_list);
					res.setNext_url(pge.getPage() + "");
				}
				
				@Override
				public void SearchFalse() {
					int SearchCount = question_service.CountQuestion();
					List<LandingQuestion>  question_list = question_service.getQuestionByPaging(AutoPaging(request, response, SearchCount));

					PageUtil pge = (PageUtil) request.getSession().getAttribute("paging");
					res.setObject(question_list);
					res.setNext_url(pge.getPage() + "");
				}
			});
			
			res.setProcessing_result(true);
			
		return res;
	}
	
	/**
	 * 문항 등록 혹은 수정 페이지에서 부모문항 선택시 해당 부모문항의 답안들을 호출하는 메서드
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/question_injection/get/answer", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getAnswerByQuestion(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		
		String question_idx = request.getParameter("question_id");
		
		if (question_idx == null | question_idx.length() == 0) {
			res.setMessage("잘못된 접근입니다.");
			res.setNext_url("/jooka/admin/landing/question_injection");
			res.setProcessing_result(true);
			return res;
		}
		
		
		int question_id = 0;
		
		try {
			question_id = Integer.parseInt(question_idx);
		} catch (Exception e) {
			// TODO: handle exception
			res.setMessage("잘못된 접근입니다.");
			res.setNext_url("/jooka/admin/landing/question_injection");
			res.setProcessing_result(true);
			return res;
		}
		
		res.setObject(answer_service.getAnswerByQuestionId(question_id));
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/edit/question", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse SubmitEditQuestion(MultipartHttpServletRequest request, @Valid LandingQuestion question, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		int question_id = question.getQuestion_id();
		
		if (!res.validation_data(result, "/jooka/admin/landing/question_manage?question_id="+ question.getQuestion_id(), "해당 문항이 정상적으로 수정되었습니다.", res)) {
			// 유효성 검사 :: START
				try {
					if (question_service.ValidationQuestion(question_id) == 0) {
						return res.returnResponse("존재하지않는 대상이 부모로 지정되었습니다.", "/jooka/admin/landing/question_injection");
					}
				} catch (Exception e) {
					// TODO: handle exception
					return res.returnResponse("존재하지않는 대상이 부모로 지정되었습니다.", "/jooka/admin/landing/question_injection");
				}
			
				String[] answer_layer_array = request.getParameterValues("answer_layer_count");
				
				if (answer_layer_array == null || answer_layer_array.length == 0) {
					return res.returnResponse("잘못된 접근입니다.", "N");
				}
				
				//배열을 List로 변경해준다.
				@SuppressWarnings("unchecked")
				List<String> answer_layer_list = new ArrayList<String>(Arrays.asList(answer_layer_array));
				
				//답안의 내용 혹은 파일이 첨부되어있는지 체크
				//둘다 없다면 리스트에 삭제처리한다.
				for (int i = 0; i < answer_layer_list.size(); i++) {
					String answer_contents = request.getParameter("answer_title_" + answer_layer_list.get(i));
					MultipartFile answer_file = request.getFile("answer_img_" + answer_layer_list.get(i));
					String answer_seq = request.getParameter("answer_seq_" + answer_layer_list.get(i));
					
					if ((answer_contents == null || answer_contents.length() == 0) &&
							(answer_file) == null) {
							
							answer_layer_list.remove(i);
					}
					
					if (answer_seq == null || answer_seq.length() == 0) {
						return res.returnResponse("잘못된 접근입니다.", "/jooka/admin/landing/question_injection");
					}
					
					try {
						Integer.parseInt(answer_seq);
					} catch (Exception e) {
						// TODO: handle exception
						return res.returnResponse("잘못된 접근입니다.", "/jooka/admin/landing/question_injection");
					}
				}
				
				if (question_id == 0) {
					return res.returnResponse("잘못된 접근입니다.", "/jooka/admin/landing/question_injection");
				}
				
				//기존 데이터 유효성 검사 시작
				//기존 답안 호출
				List<LandingAnswer> orign_answer_list = answer_service.getAnswerByQuestionId(question_id);
				//삭제하려는 답안 리스트 호출
				String[] delete_answer_list = request.getParameterValues("delete_answers");
				Map<Integer, Boolean> delete_answer_map = new HashMap<>();
				
				if (delete_answer_list != null && delete_answer_list.length > 0) {
					for (String s : delete_answer_list) {
						delete_answer_map.put(Integer.parseInt(s), true);
					}
					
					//답안이 하나도 없다면 경고
					if (delete_answer_list.length == orign_answer_list.size() && answer_layer_list.size() == 0) {
						return res.returnResponse("문항에는 최소 1개이상의 답안이 필요합니다.", "N");
					}
				}
				
				for (LandingAnswer answer : orign_answer_list) {
					String answer_contents = request.getParameter("answer_title_" + answer.getAnswer_id());
					MultipartFile answer_file = request.getFile("answer_img_" + answer.getAnswer_id());
					String answer_seq = request.getParameter("answer_seq_" + answer.getAnswer_id());
					
					//삭제 대상은 아닌데 답안내용도없고 첨부된 이미지파일도 없을경우 경고
					if ((answer_contents == null || answer_contents.length() == 0) && answer_file == null && answer.getAnswer_img() == null && delete_answer_map.get(answer.getAnswer_id()) == null) {
						return res.returnResponse("기존 등록된 답안중 내용도없고 이미지도없는 답안이 존재합니다.\r\n답안은 내용 혹은 이미지 둘중 하나는 존재해야합니다.", "N");
					}
					
					//정렬 순서가 존재하지않을경우 경고
					if (answer_seq == null || answer_seq.length() == 0) {
						return res.returnResponse("잘못된 접근입니다.", "/jooka/admin/landing/question_injection");
					}
					
					//정렬 순서에 이상한값이 입력된 경우 경고
					try {
						Integer.parseInt(answer_seq);
					} catch (Exception e) {
						// TODO: handle exception
						return res.returnResponse("잘못된 접근입니다.", "/jooka/admin/landing/question_injection");
					}
				}
			// 유효성 검사 :: END
				
			// 데이터 삽입 시작
			// 문항 업데이트
				question.setQuestion_contents(ManageSmartEditorImg(question.getQuestion_contents(), "C:/res/img/admin/landing/answer_content_img/question_"+ question.getQuestion_id()));
				question_service.UpdateQuestion(question);
			// 신구 답안 등록
				//문항의 답안 생성시작
				LandingAnswer answer = new LandingAnswer();
				answer.setQuestion_id(question.getQuestion_id());
				
				for (String key : answer_layer_list) {
					String answer_contents = request.getParameter("answer_title_" + key);
					String answer_seq = request.getParameter("answer_seq_" + key);
					String answer_event = request.getParameter("answer_event_" + key);
					
					//파일 업로드 및 파일명 지정 없을경우 null
					
					//메인이미지 업로드
					List<String> files = InjectionFile(request, "C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id(), "answer_img_" + key);
					answer.setAnswer_img(files == null ? null : files.get(0));
					
					//이벤트 이미지 업로드
					List<String> files2 = InjectionFile(request, "C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id(), "answer_event_img_" + key);
					answer.setEvent_img(files2 == null ? null : files2.get(0));
					
					answer.setAnswer_title(answer_contents);
					answer.setPring_seq(Integer.parseInt(answer_seq));
					answer.setEvent_function(answer_event == null || answer_event.length() == 0 ? null : answer_event);
					
					answer_service.InsertAnswer(answer);
				}
			//기존 답안 업데이트
				
				for (LandingAnswer a : orign_answer_list) {
					// 삭제 처리인지 체크 및 삭제
					if (delete_answer_map.get(a.getAnswer_id()) != null) {
						if (a.getAnswer_img() != null) {
							removeFile("C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id() + "\\" + a.getAnswer_img());
						}

						if (a.getEvent_img() != null) {
							removeFile("C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id() + "\\" + a.getEvent_img());
						}
						
						answer_service.DeleteAnswer(a.getAnswer_id());
						
						continue;
					} else {
						String answer_contents = request.getParameter("answer_title_" + a.getAnswer_id());
						String answer_seq = request.getParameter("answer_seq_" + a.getAnswer_id());
						String answer_event = request.getParameter("answer_event_" + a.getAnswer_id());
						

						MultipartFile answer_file = request.getFile("answer_img_" + a.getAnswer_id());
						MultipartFile event_file = request.getFile("answer_event_img_" + a.getAnswer_id());
						
						//파일 업로드 및 파일명 지정 없을경우 null
						
						//신규 이미지 첨부요청
						if (answer_file != null) {
							//기존 파일 삭제
							if (a.getAnswer_img() != null) {
								removeFile("C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id() + "\\" + a.getAnswer_img());
							}
							
							//메인이미지 업로드
							List<String> files = InjectionFile(request, "C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id(), "answer_img_" + a.getAnswer_id());
							answer.setAnswer_img(files == null ? null : files.get(0));
						}
						
						
						//신규 이벤트 이미지 첨부 요청
						if (event_file != null) {
							//기존 파일 삭제
							if (a.getEvent_img() != null) {
								removeFile("C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id() + "\\" + a.getEvent_img());
							}
							
							//이벤트 이미지 업로드
							List<String> files2 = InjectionFile(request, "C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id(), "answer_event_img_" + a.getAnswer_id());
							answer.setEvent_img(files2 == null ? null : files2.get(0));
						}
						
						answer.setAnswer_title(answer_contents);
						answer.setPring_seq(Integer.parseInt(answer_seq));
						answer.setEvent_function(answer_event == null || answer_event.length() == 0 ? null : answer_event);
						answer.setAnswer_id(a.getAnswer_id());
						
						answer_service.UpdateAnswer(answer);
					}
				}
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	/**
	 * 신규문항 등록 submit 메서드
	 * @param request
	 * @param question
	 * @param result
	 * @return
	 */
	@RequestMapping(value="/submit/question", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse SubmitQuestion(MultipartHttpServletRequest request, @Valid LandingQuestion question, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		String answer_id = request.getParameter("answer_id");
		
		
		if (!res.validation_data(result, "/jooka/admin/landing/index", "신규문항 등록이 완료되었습니다.", res)) {
			// 유효성 검사 :: START
				try {
					if (answer_service.ValidtaionAnswerId(Integer.parseInt(answer_id)) == 0) {
						return res.returnResponse("존재하지않는 대상이 부모로 지정되었습니다.", "/jooka/admin/landing/question_injection");
					}
				} catch (Exception e) {
					// TODO: handle exception
					return res.returnResponse("존재하지않는 대상이 부모로 지정되었습니다.", "/jooka/admin/landing/question_injection");
				}
			
				String[] answer_layer_array = request.getParameterValues("answer_layer_count");
				
				if (answer_layer_array == null || answer_layer_array.length == 0) {
					return res.returnResponse("잘못된 접근입니다.", "N");
				}
				
				//배열을 List로 변경해준다.
				@SuppressWarnings("unchecked")
				List<String> answer_layer_list = new ArrayList<String>(Arrays.asList(answer_layer_array));
				
				//답안의 내용 혹은 파일이 첨부되어있는지 체크
				//둘다 없다면 리스트에 삭제처리한다.
				for (int i = 0; i < answer_layer_list.size(); i++) {
					String answer_contents = request.getParameter("answer_title_" + answer_layer_list.get(i));
					MultipartFile answer_file = request.getFile("answer_img_" + answer_layer_list.get(i));
					String answer_seq = request.getParameter("answer_seq_" + answer_layer_list.get(i));
					
					if ((answer_contents == null || answer_contents.length() == 0) &&
							(answer_file) == null) {
							
							answer_layer_list.remove(i);
					}
					
					if (answer_seq == null || answer_seq.length() == 0) {
						return res.returnResponse("잘못된 접근입니다.", "/jooka/admin/landing/question_injection");
					}
					
					try {
						Integer.parseInt(answer_seq);
					} catch (Exception e) {
						// TODO: handle exception
						return res.returnResponse("잘못된 접근입니다.", "/jooka/admin/landing/question_injection");
					}
				}
				
				if (answer_layer_list.size() == 0) {
					return res.returnResponse("문항에는 최소 1개 이상의 답안이 필요합니다.", "N");
				}
				
				if (answer_id == null || answer_id.length() == 0) {
					return res.returnResponse("잘못된 접근입니다.", "/jooka/admin/landing/question_injection");
				}
			// 유효성 검사 :: END
			
			// 데이터 삽입 :: START
			
				//문제 삽입
				question_service.InsertQuestion(question);
				
				//스마트 에디터 이미지 관리
				question.setQuestion_contents(ManageSmartEditorImg(question.getQuestion_contents(), "C:/res/img/admin/landing/answer_content_img/question_"+ question.getQuestion_id()));
				
				//본문 내용  업데이트
				question_service.UpdateQuestion(question);
				
				//해당 문항의 부모와 연결
				LandingQuestionChain chain = new LandingQuestionChain();
				try {
					chain.setAnswer_id(Integer.parseInt(answer_id));
				} catch (Exception e) {
					// TODO: handle exception
					return res.returnResponse("잘못된 부모 문항의 아이디가 입력되었습니다.", "/jooka/admin/landing/question_injection");
				}
				chain.setQuestion_id(question.getQuestion_id());
				chain_service.InsertChain(chain);
				
				//문항의 답안 생성시작
				LandingAnswer answer = new LandingAnswer();
				answer.setQuestion_id(question.getQuestion_id());
				
				for (String key : answer_layer_list) {
					String answer_contents = request.getParameter("answer_title_" + key);
					String answer_seq = request.getParameter("answer_seq_" + key);
					
					String answer_event = request.getParameter("answer_event_" + key);
					
					//파일 업로드 및 파일명 지정 없을경우 null
					
					//메인이미지 업로드
					List<String> files = InjectionFile(request, "C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id(), "answer_img_" + key);
					answer.setAnswer_img(files == null ? null : files.get(0));
					
					//이벤트 이미지 업로드
					List<String> files2 = InjectionFile(request, "C:\\res\\img\\admin\\landing\\answer_content_img\\question_"+ question.getQuestion_id(), "answer_event_img_" + key);
					answer.setEvent_img(files2 == null ? null : files2.get(0));
					
					answer.setAnswer_title(answer_contents);
					answer.setPring_seq(Integer.parseInt(answer_seq));
					answer.setEvent_function(answer_event == null || answer_event.length() == 0 ? null : answer_event);
					
					answer_service.InsertAnswer(answer);
				}
			// 데이터 삽입 :: END
			
			//해당 문항의 관리페이지로 이동시켜준다.
			res.setNext_url("/jooka/admin/landing/question_manage?question_id="+ question.getQuestion_id());
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
}
