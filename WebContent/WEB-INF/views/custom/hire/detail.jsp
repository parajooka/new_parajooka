<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<link rel="stylesheet" type="text/css" href="/resources/css/custom/hire/detail.css" />
<script  src="/resources/js/custom/hire/detail.js"></script>
<p class="hire_title">${hire.title}</p>
<div class="hire_detail_layout">
	<table class="hire_detail_table">
		<tr>
			<td>
				남은 마감 시간
			</td>
			<td>	
				<span class="minute" data_value="${hire.getKrDate(hire.end_date)}"></span>
			</td>
		</tr>
		<tr>
			<td>
				상세 정보
			</td>
			<td class="info_section">	
				${hire.info}
			</td>
		</tr>
		<tr>
			<td>
				모집 인원
			</td>
			<td>	
				<span>
					<c:if test="${hire.personnel == 0}">
			  			상시 모집
			  		</c:if>
			  		<c:if test="${hire.personnel != 0}">
			  			${hire.personnel}명
			  		</c:if>
				</span>
			</td>
		</tr>
		<tr>
			<td>
				지원자
			</td>
			<td>	
				<span>
					${counts.get(hire.id)}명
				</span>
			</td>
		</tr>
	</table>
	<p class="survey_btn">
		<a class="btn-darkblue" href="/custom/hire/survey?id=${hire.id}">지원하기</a>
	</p>
</div>
