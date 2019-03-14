<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.question_layer {padding: 1vw 0; border-top: 1.5px solid #a3a3a3;}
.question_title {padding-bottom: 10px; margin-bottom: 10px; border-bottom: 1px dashed black;}
</style>
<h1 style="margin-top:30px;">사용자 정보</h1>
<div>
	<p class="question_title">이름 :: ${participant.name}</p>
	<p class="question_title">소속 :: ${participant.company}</p>
	<p class="question_title">참여IP :: ${participant.ip_address}</p>
	<p class="question_title">참여 시작일 :: ${participant.start_time}</p>
	<p class="question_title">참여 종료일 :: ${participant.end_time}</p>
</div>
<h1 style="margin-top:50px">사용자가 선택한 답안</h1>
<c:forEach items="${question_list}" var="question" varStatus="status">
	<div class="question_layer" ${status.count == question_list.size() ? 'style="border-bottom:1.5px solid #a3a3a3;"' : ''}>
		<p class="question_title">문항 :: ${question.question_title}</p>
		<c:choose>
			<c:when test="${answer_map.get(question.question_id).answer_title == null || answer_map.get(question.question_id).answer_title.length() == 0}">
				<p class="answer_title">답안 :: <img src="/res/img/admin/landing/answer_content_img/question_${question.question_id}/${answer_map.get(question.question_id).answer_img}"></p>
			</c:when>
			<c:otherwise>
				<p class="answer_title">답안 :: ${answer_map.get(question.question_id).answer_title}</p>			
			</c:otherwise>
		</c:choose>
	</div>
</c:forEach>
