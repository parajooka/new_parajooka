<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>임시 관리자 등록</title>
<script  src="/resources/common/js/jquery.js"></script>
<script  src="/resources/common/js/common.js"></script>
<script  src="/resources/common/js/ajax_controll.js"></script>
</head>
<body>
<h3>임시 관리자 아이피 생성 (임시 관리자 아이피는 30분후에 자동 삭제처리됩니다.)</h3>
<form id="frm" method="post">
	<input type="text" name="access_ip" id ="access_ip" placeholder="IP입력">
	<input type="hidden" name="authorizationer" id ="authorizationer" value="임시아이피">
	<button type="button" class="temp_acc_injection">등록</button>
</form>
<script>
$(document).ready(function() {
	$(".temp_acc_injection").on("click", function() {
		var frm = $("#frm");
		frm.attr('action', '/temp_file/temp_acc_injection/insert');
		form_submit(frm);
	});
});
</script>
</body>
</html>