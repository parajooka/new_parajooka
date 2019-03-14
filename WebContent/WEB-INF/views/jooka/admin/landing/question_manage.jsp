<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
	.parent_question_box {padding-top:1vw;}
	.dark_table {margin-top:1vw;}
	.parent_link {color: black; font-weight: normal; text-decoration: none; cursor: pointer;}
	.parent_link:hover {text-decoration: underline;}
	.dark_table tr td:first-child {width:10%;}
	
	.important {font-size: 0.8vw; line-height: 1.3vw; display: inline-block; margin: 1vw 0; color: red;}
	.btn-gray {display: inline-block; padding: 0.3vw 1vw; font-size: 1vw;}
	
	.this_answer_list {margin-top:1vw;}
	.this_answer {font-size: 1.2vw; padding: 1vw 0; border-bottom: 1px dashed black;}
</style>
<script>
$(document).ready(function() {
	$('.add_parent_question_dialog').dialog({
		autoOpen: false,
		appendTo : ".container",
		show: { effect: "blind", duration: 300 },
		hide: { effect: "blind", duration: 300 }
	});
	
	$(".add_parent_btn").on("click", function() {
		$('.add_parent_question_dialog').dialog("open");
	});
	
	var parent_idx = ${landing_question.question_id};
	
	$(".submit_add_parent").on("click", function() {
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
		
		get_item_info("/jooka/admin/landing/add/parent_question", func, {"question_id":parent_idx, "answer_id":$("#answer_id").val()}, $('body'));
	});
	
	var this_answer_id;
	var self
	$(".show_row_question").on("click", function() {
		if ($(this).parent().find("table").length == 0) {
			this_answer_id = $(this).attr("data-answer-id");
			self = $(this);
			var func = function(data) {
				//잘모된 페이지 요청시
				if (data['message'] != null) {
					alert(data['message']);
					if (data['next_url'] != null) {
						location.href = data['next_url'];
					}
					return;
				}
				
				var questions = data['object'];
				
				if (questions != null && typeof questions != "undefined" && questions.length > 0) {
					var table_str = "<table class='dark_table'>"+
									"<tr>"+
										"<td>"+
											"순번"+
										"</td>"+
										"<td>"+
											"문항(제목)"+
										"</td>"+
									"</tr>";
					
					for (var i = 0; i < questions.length; i++) {
						var child_question = questions[i];
						
						table_str += "<tr>"+
										"<td>"+
											""+ (i + 1) +""+
										"</td>"+
										"<td>"+
											"<a title='\""+ child_question['question_title'] +"\"문항으로 이동' class='parent_link' href='/jooka/admin/landing/question_manage?question_id="+ child_question['question_id'] +"'>"+
												"[문항]"+ child_question['question_title'] +""+
											"</a>"+
										"</td>"+
									"</tr>";
					}
					
						table_str += "</talbe>"
						self.parent().append(table_str);
						self.html("하위문항 닫기")
				} else {
					alert("하위문항이 존재하지 않습니다.");
				}
			}
			
			get_item_info("/jooka/admin/landing/get/question/byAnswerId", func, {"answer_id":this_answer_id}, $('body'));
		} else {
			$(this).parent().find("table").remove();
			$(this).html("하위문항 보기")
		}
	});
});

var this_name = '${landing_question.question_title}';

$(window).on("load", function() {
/* 	var orign_parents = $(".parent_link");
	//이미 부모로 등록되있는 문항은 삭제한다.
	$.each(orign_parents, function(index, value) {
		var htm = "[문항] "+ $(value).html();
		$.each($(".question_select > option"), function(index2, value2) {
			if ($(value2).html() === htm) {
				$(value2).remove();
			}
		});
	}); */
	
	//자기 자신을 삭제한다.
	$.each($(".question_select > option"), function(index2, value2) {
		if ($(value2).html() === "[문항] "+ this_name) {
			$(value2).remove();
		}
	});
});
</script>
<div class="parent_question_box">
	<h1>- 해당 문항의 상위(부모)문항 리스트</h1>
	<table class="dark_table">
		<tr>
			<td>순번</td>
			<td>문항(제목)</td>
		</tr>
		<c:choose>
			<c:when test="${parent_landing_question == null || parent_landing_question.size() == 0}">
				<tr>
					<td colspan="2">
						해당 문항의 상위(부모) 문항가 존재하지않습니다.
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${parent_landing_question}" var="parent_question" varStatus="status">
					<tr>
						<td>
							${status.count}
						</td>
						<td>
							<a title="'${parent_question.question_title}'문항으로 이동" class="parent_link" href="/jooka/admin/landing/question_manage?question_id=${parent_question.question_id}">
								[문항] ${parent_question.question_title}<br>
								<span class="parent_answer_name">[답안] ${parent_answer_list.get(status.index).getAnswer_title()}</span>
							</a>
						</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
	<div style="text-align: right; margin-top: 1vw;">
		<a class="btn-gray add_parent_btn">상위(부모) 문항 추가</a>
	</div>
</div>
<div class="this_answer_list">
	<h1>- 해당 문항의 답안 리스트</h1>
	<c:forEach items="${lading_answer}" var="answer" varStatus="status">
		<div class="this_answer">
			${answer.answer_title == null || answer.answer_title.length() == 0 ? answer.answer_img : answer.answer_title}
			<a class="btn-gray" href="/jooka/admin/landing/question_injection?answer_id=${answer.answer_id}" style="float: right; vertical-align: top; margin-left:1vw;">신규 하위문항 추가</a>
			<a class="btn-gray show_row_question" data-answer-id="${answer.answer_id}" style="float: right; vertical-align: top;">하위문항 보기</a>
			<c:if test="${answer.answer_title == null || answer.answer_title.length() == 0}">
				<p style="width:6vw;">
					<img style="width:100%;" src="/res/img/admin/landing/answer_content_img/question_${landing_question.question_id}/${answer.answer_img}">
				</p>
			</c:if>
		</div>
	</c:forEach>
</div>
<div style="text-align: center; margin-top:2vw;">
	<a class="btn-orange" style="font-size: 1.3vw; padding: 0.5vw 1vw; display: inline-block;" href="/jooka/admin/landing/question_injection?question_id=${landing_question.question_id}">
		해당문항 수정
	</a>
</div>
<div class="add_parent_question_dialog">
	<%@include file="/WEB-INF/views/layer/admin_landing/question_load_layer.jsp"%>
	<div style="text-align: center; margin-top:1vw;">
		<a class="btn-gray submit_add_parent" style="float: none; color:white;">추가</a>
	</div>
</div>
