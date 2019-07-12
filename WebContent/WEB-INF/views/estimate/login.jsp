<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>견적서</title>
<style>
html body {padding:0; margin:0; font-family: 'Malgun Gothic';}
</style>
<script  src="/resources/common/js/jquery.js"></script>
<script  src="/resources/common/js/common.js"></script>
<script  src="/resources/common/js/ajax_controll.js"></script>
</head>
<body>
<div style="display: table; width:100%; height: 100vh;" class="login_section">
	<div style="display: table-cell; width:100%; height: 100%; vertical-align: middle;">
		<div style="text-align: center; margin-bottom:5px;">
			<img style="width:200px;" src="/resources/common/img/estimate_logo.png">
		</div>
		<div style="text-align: center; margin-bottom: 10px; font-size: 20px; font-weight: bold;">
			Stop.. Who are you?
		</div>
		<div style="text-align: center;  font-size: 0;">
			<div style="display: inline-block;" class="insert_form">
				<div style="text-align: center;">
					<input id="estimate_id" style="outline: none; padding: 5px 10px; border: 1px solid #DFDFDF; border-radius: 3px 0 0 3px; font-size: 15px; width: 224px; height: 22px;" type="text" placeholder="Company ID">
				</div>
				<div style="text-align: center;">
					<input id="estimate_pw" style="outline: none; padding: 5px 10px; border: 1px solid #DFDFDF; border-radius: 3px 0 0 3px; font-size: 15px; width: 224px; height: 22px;" type="password" placeholder="Company PW">
				</div>
			</div>
			<a class="login_btn" style="vertical-align: top; color: white; cursor:pointer; background-color: #e4e4e4; display: inline-block; border: 1px solid #DFDFDF; padding: 21.8px; font-size: 17px; border-radius: 0 3px 3px 0;">
				Login
			</a>
		</div>
	</div>
</div>
<div style="width: 100%; height: 100vh; display: none;" class="estimate_section">
	<img class="estimate" style="width:100%;">
</div>
<script>
	$(document).ready(function() {
		$(".login_btn").height($(".insert_form").height() - 22 * 2 - 1.5);
		
		$(".login_btn").on("click", function() {
			var func = function(data) {
				//잘모된 페이지 요청시
				if (data['message'] != null) {
					if (typeof data['message'] != "undefined" && data['message'].length > 0) {
						alert(data['message']);
					}
					
					if (data['next_url'] != null) {
						location.href = data['next_url'];
					}
					return;
				}
				
				var img = data['object'];
				
				$(".estimate").attr("src", "/resources/img/estimate/"+ img +".png");
				$(".login_section").hide();
				$(".estimate_section").show();
			}
			
			get_item_info("/estimate/validLogin", func, {id:$("#estimate_id").val(), pw:$("#estimate_pw").val()}, $('body'));
		});
	});
</script>
</body>
</html>