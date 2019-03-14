<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    
<link rel="stylesheet" type="text/css" href="/resources/css/admin/hirejoin.css" />
<script>
	var question_count = '${question_count}';
	var hire_idx = '${hire_id}';
</script>
<script src="/resources/js/admin/hirejoin.js"></script>
<div class="hire_join_submit_form">
	<select class="hire_list">
			<option value="0">문항을 관리할 공고를 선택해주세요.</option>
		<c:forEach items="${hire_list}" var="hire" varStatus="status">
			<option value="${hire.id}">
				명칭 : [ ${hire.title} ] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; || &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				상태 :  
				<c:choose>
					<c:when test="${hire.getStatus() == 1}">
						[공고 진행중]
					</c:when>
					<c:when test="${hire.getStatus() == 0}">
						[공고 종료]
					</c:when>
					<c:otherwise>
						[공고 진행 예정]
					</c:otherwise>
				</c:choose>
			</option>
		</c:forEach>
	</select>
	<div class="hire_question_table_section">
		<table class="hire_question_table qnh_table"></table>
	</div>
	
	<div class="select_question_section">
		<span class="question_list_title">질문(문제) 리스트</span>
		<table class="qnh_table question_table">
			<c:forEach items="${question_list}" var="question" varStatus="staus">
				<tr>
					<td>
						${question.content}
					</td>
					<td data_id="${question.id}" class="question_status"></td>
				</tr>
			</c:forEach>
		</table>
	</div>
	${paging_layout}
</div>