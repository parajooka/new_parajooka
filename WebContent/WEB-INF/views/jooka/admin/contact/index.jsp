<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>	
	.dark_table {font-size:1vw; margin-top: 1vw;}
	.dark_table tr td:nth-child(2) {width:8%;}
	.dark_table tr td:first-child {width:5%;}
	
	.btn-orange {padding: 0.2vw 1vw; display: inline-block;}
	.btn-gray {font-size: 1vw; padding: 0.5vw 1vw; display: inline-block; margin-top: 20px;}
	.important {font-size: 0.8vw; line-height: 1.3vw; display: inline-block; margin: 1vw 0; color: red;}
</style>
<div class="contact_container">
	<table class="dark_table">
		<tr>
			<td><input type="checkbox" class="select_all"/></td>
			<td>순번</td>
			<td>신청자 명</td>
			<td>소속</td>
			<td>연락처</td>
			<td>미팅 수단</td>
			<td>미팅 요청 일시</td>
			<td>참여 일시</td>
			<td>메모</td>
		</tr>
		<c:forEach items="${contact_list}" var="contact" varStatus="status">
		<tr>
			<td>
				<input type="checkbox" class="del_contact" value="${contact.contact_id}"/>
			</td>
			<td>
				${paging.numberic + status.index}
			</td>
			<td>
				${participant_list.get(status.index).name}
			</td>
			<td>
				${participant_list.get(status.index).company}
			</td>
			<td>
				${contact.reservation_pw}
			</td>
			<td>
				<c:choose>
					<c:when test="${contact.reservation_type == 0}">
						방문
					</c:when>
					<c:when test="${contact.reservation_type == 1}">
						전화
					</c:when>
					<c:otherwise>
						메일
					</c:otherwise>				
				</c:choose>
			</td>
			<td>
				${contact.reservation_date}
			</td>
			<td>
				${contact.contact_Date}
			</td>
			<td>
				<a class="btn-orange showMemo" data-contact-id="${contact.contact_id}">메모보기</a>
			</td>
		</tr>
		</c:forEach>
		<c:if test="${contact_list == null || contact_list.size() < 1}">
			<tr>
				<td colspan="8">
					미팅 신청자가 존재하지 않습니다.
				</td>
			</tr>
		</c:if>
	</table>
	<div style="text-align: right;">
		<a class="btn-gray delete_contact" style="float:left;">미팅 삭제</a>
	</div>
	${paging_layout}
</div>
<div class="acc_editor" title="사용자 메모 확인">
	<textarea class="reservation_memo" readonly="readonly" style="width:350px; height: 400px;"></textarea>
</div>
<script>
$(document).ready(function() {
	$('.acc_editor').dialog({
		autoOpen: false,
		appendTo : ".container",
		show: { effect: "blind", duration: 300 },
		hide: { effect: "blind", duration: 300 }
	});
	
	$(".showMemo").on("click", function() {
		var contact_id = $(this).attr("data-contact-id");
		
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null) {
				alert(data['message']);
				if (data['next_url'] != null) {
					location.href = data['next_url'];
				}
				return;
			}
			
			$(".reservation_memo").val(data['object']);
			$('.acc_editor').dialog("open");
		}
		
		
		get_item_info("/jooka/admin/contact/getMemo", func, {"contact_id":contact_id}, $('body'));
	});
	
	$(".select_all").on("change", function() {
		if ($(this).prop("checked")) {
			$(".del_contact").prop("checked", true);
		} else {
			$(".del_contact").prop("checked", false);
		}
	});
	
	$(".delete_contact").on("click", function() {
		if (confirm("선택한 미팅을 모두 삭제하시겠습니까?\r\n삭제후에는 복원이 절대 불가능합니다.")) {
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
				
				var delete_contact_targets = [];
				
				$.each($(".del_contact"), function(index, value) {
					if ($(value).prop("checked") == true) {
						delete_contact_targets.push($(value).val());
					}
				});
				
				
				get_item_info("/jooka/admin/contact/delete", func, {"delete_contact_targets":delete_contact_targets}, $('body'));
			}
		}
	});
});
</script>
