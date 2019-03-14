<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    
<link rel="stylesheet" type="text/css" href="/resources/css/admin/question.css" />
<script type="text/javascript" src="/resources/common/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script>
	var question_count = '${question_count}';
</script>
<script src="/resources/js/admin/question.js"></script>

<div class="question_container">
	<span class="question_count">등록된 문제 갯수 : ${question_count}개</span>
	<table class="question_view">
		<c:if test="${question_count <= 0}">
			<tr>
				<td class="not_found">
					등록된 문제가 존재하지 않습니다.
				</td>
			</tr>
		</c:if>
		<c:if test="${question_count > 0}">
			<tr>
				<td>
					순번
				</td>
				<td>
					문항
				</td>
				<td>
					유형
				</td>
			</tr>
			<c:forEach items="${questions}" var="question" varStatus="status">
			<tr>
				<td class="numberic">${status.count}</td>
				<td>
					<a class="question_edit open_modal" data_id="${question.id}">${question.content}</a>
				</td>
				<td>
					<c:if test="${question.type == 0}">
						객관식
					</c:if>
					
					<c:if test="${question.type == 1}">
						주관식
					</c:if>
				</td>
			</tr>
			</c:forEach>
		</c:if>
	</table>
	<p class="btn_section">
		<a class="open_modal">신규 등록</a>
	</p>
</div>
${paging_layout}

<div class="question_editor" title="문제/질문 등록 및 수정">
	<form:form cssClass="question_insert_form" modelAttribute="question">
		<table class="modal_table">
			<tr>
				<td>
					문항
				</td>
				<td>
					<form:input path="content" placeholder="문제의 문항을 입력해주세요."/>
				</td>
			</tr>
			<tr>
				<td>
					보기
				</td>
				<td style="width: 650px;">
					<form:textarea path="example"/>
				</td>
			</tr>
			<tr>
				<td>
					유형
				</td>
				<td style="padding-bottom: 1vw;">
					<form:select path="type">
						<form:option value="0">객관식</form:option>
						<form:option value="1">주관식</form:option>
					</form:select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding:0;">
					<hr style="border: 1px solid #b5b5b5;"> 
				</td>
			</tr>
			<tr>
				<td>
					<span style="font-size:0.8vw;">정답 여부</span>
					<input type="hidden" name="answer_count" id="answer_count" value="1">
				</td>
				<td style="text-align: left; font-size: 0.8vw;">
					주관식 >> 정답여부 관계없이 모두 정답으로 등록<br>
					객관식 >> 정답 체크가 없을경우 모두 오답처리
					<input type="button" value="답안추가" class="plust_answer" style="float:right; font-size:0.6vw;">
				</td>
			</tr>
			<tr>
				<td style="text-align: center;">
					<input type="checkbox" name="answer_1" value="0"/>
				</td>
				<td>
					<input type="text" id="answer_content" style="width: 100%;" name="answer_1_content"/>
				</td>
			</tr>
			<tr class="submit_section">
				<td colspan="2" class="dialog_submit">
					<a class="question_insert_submit dialog_submit_btn">등록</a>
					<a class="question_edit_submit dialog_submit_btn" style="display: none;">수정</a>
					<a class="question_delete_submit dialog_submit_btn" style="display: none; margin-top: 1px;">삭제</a>
				</td>
			</tr>
		</table>
	</form:form>
</div>