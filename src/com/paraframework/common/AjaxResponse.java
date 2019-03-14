package com.paraframework.common;



import org.springframework.validation.BindingResult;


public class AjaxResponse {
	
	private String next_url;
	private String message;
	//요청 처리 정상 : true , 예외 발생 : false
	private boolean processing_result = false; 
	private Object object;

	public String getNext_url() {
		return next_url;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public void setNext_url(String next_url) {
		this.next_url = next_url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isProcessing_result() {
		return processing_result;
	}
	public void setProcessing_result(boolean processing_result) {
		this.processing_result = processing_result;
	}
	
	public AjaxResponse returnResponse(String message, String nextUrl) {
		this.message = message;
		this.next_url = nextUrl;
		this.processing_result = true;
		
		return this;
	}
	
	/**
	 * 예외처리 담당 메서드 (로그 기록 등)
	 * @param e 데이터 처리과정에서 발생 예외
	 */
	public void occurred_error(Exception e) {
		System.err.println("### 에러내용 : "+ e.getLocalizedMessage());
	}
	
	/**
	 * 유효성 검사
	 * @param result 유효성 검사 결과가 담긴 객체
	 * @param next_url 다음 페이지 url(유효성 검사 통과시 res에 담김)
	 * @param success_message 통신 성공 메세지(유효성 검사 통과시 res에 담김)
	 * @param res ajax통신의 결과를 담게 될 객체
	 * @return
	 */
	public boolean validation_data(BindingResult result, String next_url, String success_message, AjaxResponse res) {
		//유효성 검사 통과 => 매개변수로 받은 성공 메세지와 다음 페이지 url을 res에 담고 false return
		if (!result.hasErrors()) {
			res.setMessage(success_message);
			res.setNext_url(next_url);
		//유효성 검사 불통과 => 입력되지않은 멤버변수 명과 멘트를 res message에 담고 return true
		} else {
			String error_message = result.getAllErrors().get(0).getDefaultMessage();
			res.setMessage(error_message.length() > 50 ? "관리자에게 문의 바랍니다." : error_message); 
			System.err.println(result.getTarget().toString() +"||"+error_message);
		}
		
		return result.hasErrors();
	}
}
