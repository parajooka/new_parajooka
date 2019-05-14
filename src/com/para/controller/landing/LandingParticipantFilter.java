package com.para.controller.landing;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.paraframework.common.NabsysRequest;
import com.paraframework.common.StringCryPto;

public class LandingParticipantFilter implements Filter {
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		//검색어가 있는지 검사
		String keyword = httpRequest.getParameter("keyword");
		
		//검색어가 있다면 암호화하여 Controller에 전달한다
		if (keyword != null && keyword.length() > 0) {
			//request 변환 객체 생성 [Controller로 전달될 request]
			NabsysRequest customRequest = new NabsysRequest(httpRequest);
			try {
				//기존의 키워드는 삭제
				customRequest.removeAttribute("keyword");
				//새로운 키워드로 바꿔치기
				String crypto = StringCryPto.encrypt("CustomerLandingResult", keyword);
				customRequest.setParameter("keyword", crypto);
				//검색후에 복호화하기위해 암호화라는것을 표시
				customRequest.setParameter("crypto", StringCryPto.decrypt("CustomerLandingResult", crypto));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//
			chain.doFilter(customRequest, response);
			return;
		}
		
		//키워드가 없다면 그냥 진행
		chain.doFilter(request, response);

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
