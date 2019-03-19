<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>	
	.dark_table {font-size:1vw; margin-top: 1vw;}
	.dark_table tr td:first-child {width:5%;}
	.dark_table tr td:last-child {width:8%;}
	.dark_table tr td:nth-child(6) {width: 22%;}
	.dark_table tr td:nth-child(7) {width: 22%;}
	
	.btn-orange {padding: 0.2vw 1vw; display: inline-block;}
	.btn-gray {font-size: 1vw; padding: 0.5vw 1vw; display: inline-block; margin-top: 20px;}
	.important {font-size: 0.8vw; line-height: 1.3vw; display: inline-block; margin: 1vw 0; color: red;}
</style>
<div class="landing_participant_container">
	<table class="dark_table">
		<tr>
			<td><input type="checkbox" class="select_all"/></td>
			<td>순번</td>
			<td>이름</td>
			<td>소속</td>
			<td>IP</td>
			<td>설문 시작시간</td>
			<td>설문 종료시간</td>
			<td>기능</td>
		</tr>
		<c:forEach items="${participant_list}" var="participant" varStatus="status">
		<tr>
			<td>
				<input type="checkbox" class="del_participant" value="${participant.participant_id}"/>
			</td>
			<td>
				${paging.numberic + status.index}
			</td>
			<td>
				${participant.name}
			</td>
			<td>
				${participant.company}
			</td>
			<td>
				${participant.ip_address}
			</td>
			<td>
				${participant.start_time}
			</td>
			<td>
				${participant.end_time}
			</td>
			<td>
				<a class="btn-orange" href="/jooka/admin/landing/participant_manage?participant_id=${participant.participant_id}">관리</a>
			</td>
		</tr>
		</c:forEach>
		<c:if test="${participant_list == null || participant_list.size() < 1}">
			<tr>
				<td colspan="8">
					랜딩에 참여한 참여자가 없습니다.
				</td>
			</tr>
		</c:if>
	</table>
	<div style="text-align: right;">
		<a class="btn-gray delete_participant" style="float:left;">참여자 삭제</a>
	</div>
	${paging_layout}
	${search_layout}
</div>
<script>
$(document).ready(function() {
	$(".select_all").on("change", function() {
		if ($(this).prop("checked")) {
			$(".del_participant").prop("checked", true);
		} else {
			$(".del_participant").prop("checked", false);
		}
	});
	
	$(".delete_participant").on("click", function() {
		if (confirm("선택한 참여자들을 모두 삭제하시겠습니까?\r\n삭제후에는 복원이 절대 불가능합니다.")) {
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
				
				var delete_participant_targets = [];
				
				$.each($(".del_participant"), function(index, value) {
					if ($(value).prop("checked") == true) {
						delete_participant_targets.push($(value).val());
					}
				});
				
				
				get_item_info("/jooka/admin/landing/delete/participant", func, {"delete_participant_targets":delete_participant_targets}, $('body'));
			}
		}
	});
});
</script>
