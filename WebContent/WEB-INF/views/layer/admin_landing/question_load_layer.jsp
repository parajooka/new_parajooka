<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.important {font-size: 0.8vw; line-height: 1.3vw; display: inline-block; margin: 1vw 0; color: red;}
.parent_question_section {width:632px;}
.question_select_box {margin-bottom:30px;}
.question_select_paging {margin-top:10px; text-align: center;}
.question_select_paging a {display: inline-block; font-size:15px; cursor: pointer;}
.question_select_before {float: left; margin-top:10px;}
.question_select_after {float: right; margin-top:10px;}
</style>
<script>
$(document).ready(function() {
	var now_page = ${paging.page};
	var total_page = ${paging.total_page};
	$(".question_select_before").on("click", function() {
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null) {
				alert(data['message']);
				if (data['next_url'] != null) {
					location.reaplce(data['next_url']);
				}
				return;
			}
			
			//정삭적인 페이지 요청시
			now_page = parseInt(data['next_url']);
			var question_list = data['object'];
			var str = '<option value="0" selected="selected">최상위 문항 신규등록</option>';
			
			for (var i = 0; i < question_list.length; i++) {
				var target_question = question_list[i];
				if (typeof this_name == "undefined" || target_question["question_title"] != this_name) {
					str += '<option value="'+ target_question["question_id"] +'">[문항] '+ target_question["question_title"] +'</option>';
				}
			}
			
			$("#answer_id").val(0);
			$(".answer_select_box > select").empty();
			$(".answer_select_box > select").html('<option disabled="disabled">신규 최상위 문항을 등록합니다.</option>');
			
			$(".question_select_box > select").html(str);
		}
		
		get_item_info("/jooka/admin/landing/question_injection/get/question", func, {"page":now_page - 1, "search_item":$(".search_item").val(), "keyword": $(".search_keywords").val()}, $('body'));
	});

	$(".question_select_after").on("click", function() {
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null) {
				alert(data['message']);
				if (data['next_url'] != null) {
					location.reaplce(data['next_url']);
				}
				return;
			}
			
			//정삭적인 페이지 요청시
			now_page = parseInt(data['next_url']);
			var question_list = data['object'];
			var str = '<option value="0" selected="selected">최상위 문항 신규등록</option>';
			
			for (var i = 0; i < question_list.length; i++) {
				var target_question = question_list[i];
				if (typeof this_name == "undefined" || target_question["question_title"] != this_name) {
					str += '<option value="'+ target_question["question_id"] +'">[문항] '+ target_question["question_title"] +'</option>';
				}
			}
			
			$("#answer_id").val(0);
			$(".answer_select_box > select").empty();
			$(".answer_select_box > select").html('<option disabled="disabled">신규 최상위 문항을 등록합니다.</option>');
			
			$(".question_select_box > select").html("[문항] "+ str);
		}

		get_item_info("/jooka/admin/landing/question_injection/get/question", func, {"page":now_page + 1, "search_item":$(".search_item").val(), "keyword": $(".search_keywords").val()}, $('body'));
	});

	$(".search_submit").on("click", function() {
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null) {
				alert(data['message']);
				if (data['next_url'] != null) {
					location.reaplce(data['next_url']);
				}
				return;
			}
			
			//정삭적인 페이지 요청시
			now_page = parseInt(data['next_url']);
			var question_list = data['object'];
			var str = '<option value="0" selected="selected">최상위 문항 신규등록</option>';
			
			for (var i = 0; i < question_list.length; i++) {
				var target_question = question_list[i];
				if (typeof this_name == "undefined" || target_question["question_title"] != this_name) {
					str += '<option value="'+ target_question["question_id"] +'">[문항] '+ target_question["question_title"] +'</option>';
				}
			}
			
			$("#answer_id").val(0);
			$(".answer_select_box > select").empty();
			$(".answer_select_box > select").html('<option disabled="disabled">신규 최상위 문항을 등록합니다.</option>');
			
			$(".question_select_box > select").html(str);
		}

		get_item_info("/jooka/admin/landing/question_injection/get/question", func, {"page": 1, "search_item":$(".search_item").val(), "keyword": $(".search_keywords").val()}, $('body'));
	});

	$(".search_keywords").keydown(function(key) {
	    if (key.keyCode == 13) {
	    	$(".search_submit").trigger("click");
	    }
	});

	$(".question_select").on("change", function() {
		if ($(this).val() < 1) {
			$("#answer_id").val(0);
			$(".answer_select_box > select").empty();
			$(".answer_select_box > select").html('<option disabled="disabled">신규 최상위 문항을 등록합니다.</option>');
			return;
		}
		
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null) {
				alert(data['message']);
				if (data['next_url'] != null) {
					location.reaplce(data['next_url']);
				}
				return;
			}
			
			//정삭적인 페이지 요청시
			var answer_list = data['object'];
			var str = '';
			
			for (var i = 0; i < answer_list.length; i++) {
				var target_answer = answer_list[i];
				var answer_title = target_answer["answer_title"];
					answer_title = (answer_title == null || typeof answer_title == "undefined" || answer_title.length == 0) ? target_answer["answer_img"] : answer_title;
				str += '<option value="'+ target_answer["answer_id"] +'">[답안 내용] '+ answer_title +'</option>';	
			}
			
			$(".answer_select_box > select").html(str);
		}

		get_item_info("/jooka/admin/landing/question_injection/get/answer", func, {"question_id":$(this).val()}, $('body'));
	});

	$(".answer_select_box > select").on("change", function() {
		$("#answer_id").val($(this).val());
	});
});
</script>
<c:choose>
	<c:when test="${question_id == null || question_id.length() == 0}">
		<input type="hidden" name="answer_id" id="answer_id" value="${answer_id == null || answer_id.length() == 0 ?  0 : answer_id}">
	</c:when>
	<c:otherwise>
		<input type="hidden" name="question_id" id="question_id" value="${question_id}">
	</c:otherwise>
</c:choose>
<c:if test="${question_id == null || question_id.length() == 0}">
<c:if test="${answer_id == null || answer_id.length() == 0}">
<div class="parent_question_section">
		<a class="important">1. 부모로 지정할 문제를 선택해주세요.<br>최상위 문항 등록시에는 '최상위 문항 신규등록'을 선택해주세요.</a>
		<div class="question_select_box">
			<select size="${paging.limit}" style="width:632px;" class="question_select">
				<option value="0" selected="selected">최상위 문항 신규등록</option>
				<c:forEach items="${question_list}" var="question" varStatus="status">
					<option value="${question.question_id}">[문항] ${question.question_title}</option>
				</c:forEach>
			</select>
			<c:if test="${paging.total_page > 1}">
			<div class="question_select_paging">
				<a class="question_select_before">이전</a>
				<select style="height:32px; font-size:11px;" class="search_item">
					<c:forEach items="${search_conditions.keySet()}" var="key" varStatus="status">
						<option value="${search_conditions.get(key)}">${key}</option>
					</c:forEach>
				</select>
				<input type="text" class="search_keywords" style="vertical-align: top; height: 32px; font-size:11px;" placeholder="검색어를 입력해주세요.">
				<a class="btn-green search_submit" style="padding: 6px 10px; vertical-align: top; color: white;">검색</a>
				<a class="question_select_after">다음</a>
			</div>
			</c:if>
		</div>
	</div>
	<div class="parent_question_section">
		<a class="important">2. 신규등록 하고자 하는 문제가 해당 문제에서 어떤 답안을 선택했을때 나타나게할지 선택해주세요.</a>
		<div class="answer_select_box" style="padding-bottom: 30px; border-bottom: 2px solid black;"> 
			<select size="5" style="width:632px;" class="answer_select">
				<option disabled="disabled">신규 최상위 문항을 등록합니다.</option>
			</select>
		</div>
	</div>
</c:if>
</c:if>