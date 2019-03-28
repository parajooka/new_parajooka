<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>	
	.dark_table {font-size:1vw; margin-top: 1vw;}
	.dark_table tr td:nth-child(3) {width:72%;}
	.dark_table tr td:nth-child(2) {width:8%;}
	.dark_table tr td:first-child {width:5%;}
	.dark_table tr td:last-child {width:15%;}
	
	.btn-orange {padding: 0.2vw 1vw; display: inline-block;}
	.btn-gray {font-size: 1vw; padding: 0.5vw 1vw; display: inline-block; margin-top: 20px;}
	.important {font-size: 0.8vw; line-height: 1.3vw; display: inline-block; margin: 1vw 0; color: red;}
</style>
<div class="landing_question_container">
	<div style="text-align: right; margin-top:1vw;">
		${paging_limit_layout}
	</div>
	<table class="dark_table">
		<tr>
			<td><input type="checkbox" class="select_all"/></td>
			<td>순번</td>
			<td>문항(제목)</td>
			<td>기능</td>
		</tr>
		<c:forEach items="${question_list}" var="question" varStatus="status">
		<tr>
			<td>
				<input type="checkbox" class="del_question" value="${question.question_id}"/>
			</td>
			<td>
				${paging.numberic + status.index}
			</td>
			<td>
				${question.question_title}
			</td>
			<td>
				<a class="btn-orange" href="${admin_root_path}/landing/question_manage?question_id=${question.question_id}">관리</a>
			</td>
		</tr>
		</c:forEach>
		<c:if test="${question_list == null || question_list.size() < 1}">
			<tr>
				<td colspan="4">
					등록된 문제가 존재하지않습니다.
				</td>
			</tr>
		</c:if>
	</table>
	<div style="text-align: right;">
		<a class="btn-gray delete_question" style="float:left;">문항 삭제</a>
		<a class="btn-gray injection_parent_question" href="${admin_root_path}/landing/question_injection">신규문항 등록</a>
	</div>
	${paging_layout}
	${search_layout}
</div>
<script>
$(document).ready(function() {
	$(".select_all").on("change", function() {
		if ($(this).prop("checked")) {
			$(".del_question").prop("checked", true);
		} else {
			$(".del_question").prop("checked", false);
		}
	});
	
	$(".delete_question").on("click", function() {
		if (confirm("선택한 문항들을 모두 삭제하시겠습니까?\r\n삭제후에는 복원이 절대 불가능합니다.")) {
			if (confirm("정말로 삭제하시겠습니까?")) {
				var func = function(data) {
					//잘모된 페이지 요청시
					if (data['message'] != null) {
						alert(data['message']);
						if (data['next_url'] != null) {
							location.href = data['next_url'];
						}
						return;
					}
				}
				
				var delete_question_targets = [];
				
				$.each($(".del_question"), function(index, value) {
					if ($(value).prop("checked") == true) {
						delete_question_targets.push($(value).val());
					}
				});
				
				
				get_item_info("${admin_root_path}/landing/delete/question", func, {"delete_question_targets":delete_question_targets}, $('body'));
			}
		}
	});
});
</script>
