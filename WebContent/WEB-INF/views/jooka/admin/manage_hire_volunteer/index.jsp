<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    
<link rel="stylesheet" type="text/css" href="/resources/css/admin/manage_hire_volunteer.css" />
<script src="/resources/js/admin/manage_hire_volunteer.js"></script>
<div class="hire_join_submit_form">
	<select class="hire_list">
			<option value="0">지원자를 관리할 공고를 선택해주세요.</option>
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
	
	<table class="volunteer_table">
		<tr>
			<td>
				이름
			</td>
			<td>
				연락처
			</td>
			<td>
				처리상태
			</td>
			<td>
				지원일자
			</td>
			<td>
				지원서 수정 일자
			</td>
			<td>
			</td>
		</tr>
		<tr class="first_page">
			<td colspan="6">지원자를 관리할 공고를 선택 해주세요.</td>
		</tr>
	</table>
</div>