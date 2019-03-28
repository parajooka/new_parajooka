<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<link rel="stylesheet" type="text/css" href="/resources/css/admin/access_ip.css" />
<script>
	var acc_count = ${acc_ip_count};
</script>
<script src="/resources/js/admin/access_ip.js"></script>
<div class="acc_container">
	<span class="acc_count">등록된 허용 아이피 갯수 : ${acc_ip_count}개</span>
	<table class="acc_view">
		<c:if test="${acc_ip_count <= 0}">
			<tr>
				<td class="not_found">
					등록된 아이피가 존재하지 않습니다.
				</td>
			</tr>
		</c:if>
		<c:if test="${acc_ip_count > 0}">
			<tr>
				<td>
					순번
				</td>
				<td>
					IP
				</td>
				<td>
					등록자
				</td>
				<td>
					등록일
				</td>
			</tr>
			<c:forEach items="${acc_ip_list}" var="acc" varStatus="status">
			<tr>
				<td class="numberic">${paging.numberic + status.index}</td>
				<td>
					<a class="acc_edit" data_id="${acc.id}">${acc.access_ip}</a>
				</td>
				<td>
					${acc.authorizationer}
				</td>
				<td>
					${acc.auth_date}
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
${search_layout}
<div class="acc_editor" title="접근 허용 아이피 추가">
	<form action="${admin_root_path}/access_ip/insert" class="acc_insert_form">
		<table class="modal_table">
			<tr>
				<td>
					허용 아이피
				</td>
				<td>
					<input type="text" name="access_ip" id="access_ip">
				</td>
			</tr>
			<tr>
				<td>
					권한 부여자
				</td>
				<td>
					<input type="text" name="authorizationer" id="authorizationer">
				</td>
			</tr>
			<tr>
				<td colspan="2" class="dialog_submit">
					<a class="acc_insert_submit dialog_submit_btn">등록</a>
					<a class="acc_edit_submit dialog_submit_btn" style="display: none;">수정</a>
					<a class="acc_delete_submit dialog_submit_btn" style="display: none; margin-top: 1px;">삭제</a>
				</td>
			</tr>
		</table>
	</form>
</div>