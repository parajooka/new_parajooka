<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    
<link rel="stylesheet" type="text/css" href="/resources/common/css/mailtip.css" />
<link rel="stylesheet" type="text/css" href="/resources/css/custom/hire/survey.css" />
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script  src="/resources/common/js/jquery.mailtip.js"></script>
<script type="text/javascript" src="/resources/common/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script  src="/resources/js/custom/hire/survey.js"></script>
<style>
	.container, .section{background-color: white !important;}
	.survey_detail_layout {width: 100% !important; padding:inherit !important;}
</style>
<p class="survey_title">'${volunteer.name}'님의 '${hire.title}' 지원서 관리</p>
<form:form modelAttribute="volunteer" cssClass="survey_detail_layout">
	<table class="survey_detail_table">
		<tr>
			<td>
				성명
			</td>
			<td>
				<form:input path="name"/>
			</td>
		</tr>
		<tr>
			<td>
				연락처
			</td>
			<td>
				<form:input path="phone" cssStyle="width: 14vw;"/>
			</td>
		</tr>
		<c:if test="${hire.gender_use == 0 || hire.gender_use == 1}">
			<tr>
				<td>
					성별
				</td>
				<td>
					<form:select path="gender">
						<form:option value="0">남</form:option>
						<form:option value="1">여</form:option>
					</form:select>
				</td>
			</tr>
		</c:if>
		<c:if test="${hire.email_use == 0 || hire.email_use == 1}">
			<tr>
				<td>
					이메일
				</td>
				<td>
					<form:input path="email" cssStyle="width: 15vw;"/>
				</td>
			</tr>
		</c:if>
		<c:if test="${hire.address_use == 0 || hire.address_use == 1}">
			<tr>
				<td>
					주소
				</td>
				<td>
					<form:input path="post" cssStyle="width:4vw;"/>
					<form:input path="address" cssStyle="width:45vw;"/>
				</td>
			</tr>
		</c:if>
		<c:if test="${hire.portfolio == 0 || hire.portfolio == 1}">
			<tr>
				<td>
					포트폴리오
				</td>
				<td>
					<c:if test="${volunteer_result.file == null}">
						등록된 포트폴리오가 존재하지 않습니다.
					</c:if>
					<c:if test="${volunteer_result.file != null}">
						<a href="/res/img/custom/hire/${volunteer_result.file}" target="_blank">포트 폴리오 바로가기</a>
					</c:if>
				</td>
			</tr>
		</c:if>
	</table>
	<div class="question_section">
		<c:forEach items="${questions}" var="question">
			<div class="question_box">
				<div class="question_content">
					${question.content}
				</div>
				<c:if test="${question.example != null && question.example.length() > 0}">
					<div class="question_example">
						${question.example}
					</div>
				</c:if>
				<c:if test="${question.type == 0}">
					<c:forEach items="${question.answers}" var="answer">
						<div class="answer_section">
							<c:choose>
								<c:when test="${hire_results.get(answer.id) != null}">
									<input type="checkbox" id="answer_${answer.id}" name="answer_${question.id}" style="cursor: pointer;" value="${answer.id}" checked="checked">								
								</c:when>
								<c:otherwise>
									<input type="checkbox" id="answer_${answer.id}" name="answer_${question.id}" style="cursor: pointer;" value="${answer.id}">
								</c:otherwise>
							</c:choose>
							<label style="cursor: pointer;" for="answer_${answer.id}">${answer.content}</label>
						</div>
					</c:forEach>
				</c:if>
				<c:if test="${question.type == 1}">
					<c:forEach items="${question.answers}" var="answer">
						<div class="answer_section">
							<textarea id="${question.id}_open_answer" name="${question.id}_open_answer" style="background-color: white; width: 100%; min-height: 200px;">${hire_results.get(question.id)}</textarea>
						</div>
					</c:forEach>
				</c:if>
			</div>
		</c:forEach>
	</div>
	<p class="survey_btn">
		<a class="btn-darkblue" href="/jooka/admin/manage_hire_volunteer/survey_result?id=${volunteer_result.id}&value=1" style="margin: 0 1vw;">합격</a>
		<a class="btn-orange" href="/jooka/admin/manage_hire_volunteer/survey_result?id=${volunteer_result.id}&value=2" style="margin: 0 1vw;">불합격</a>
	</p>
</form:form>
