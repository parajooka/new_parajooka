<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
	.question_injection_container {width:60%; margin:auto;}
	.question_inejction_section, .answer_injection_section, .answer_edit_section  {width:632px;}
	.answer_injection_section, .answer_edit_section {border-top: 8px solid #737373;}
	.answer_edit_section {padding-top:2vw;}
	.question_title_section {width:100%; margin-top: 2vw;}
	.question_title_section input{width:100%;}
	.question_contents_section{margin-top:2vw;}
	
	.important {font-size: 0.8vw; line-height: 1.3vw; display: inline-block; margin: 1vw 0; color: red;}
	
	.answer_title {width:100%; height: 100px; margin-top:5px;}
	.answer_inejction, .answer_edit {border-bottom: 2px dashed black; padding-top: 30px;}
	.btn-gray {float: right; padding: 0.2vw 1vw; font-size: 15px;}
	.btn-darkblue {padding:0.5vw 1vw; display:inline-block; font-size:1vw;}
	
	.answer_input_section {width:100%; text-align: right; padding-bottom: 2vw;}
</style>
<script type="text/javascript" src="/resources/common/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script>
var oEditors = ["question_contents"];
function pasteHTML(filepath){
    var sHTML = '<img src="'+ filepath+'">';
    oEditors.getById["question_contents"].exec("PASTE_HTML", [sHTML]);
}
$(document).ready(function(){
	var question_id = '${question_id}';
	var question_contents = '${question.question_contents == null || question.question_contents.length() < 1 ? "" : question.question_contents}';
	
	//전역변수선언
    nhn.husky.EZCreator.createInIFrame({
        oAppRef: oEditors,
        elPlaceHolder: "question_contents",
        sSkinURI: "/resources/common/smarteditor/SmartEditor2Skin.html",
        htParams : {
            // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseToolbar : true,            
            // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseVerticalResizer : true,    
            // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
            bUseModeChanger : true,
        },
        fOnAppLoad: function () {    
            $("iframe").css("width", "100%").css("height", "254px");
            
            if (typeof question_id != "undefined" && question_id.length > 0) {
            	oEditors.getById["question_contents"].exec("PASTE_HTML", [question_contents]); //내용밀어넣기
            }
        }
    });
	
	var answer_layer = $(".answer_inejction").clone();
	answer_layer.find(".add_answer_layer").attr("class", "btn-gray remove_answer_layer").html("답안 제거");
	
	var answer_layer_count = 0;
	$(".add_answer_layer").on("click", function() {
		$('.only_number').spinner({min : 0, max:99999});
		$('.only_number').val(0);
		var answer_layer_clone = answer_layer.clone();
		
		answer_layer_count ++;
		answer_layer_clone.find("#answer_layer_count").val(answer_layer_count);
		answer_layer_clone.find(".answer_img").attr("name", "answer_img_"+ answer_layer_count).attr("id", "answer_img_"+ answer_layer_count);
		answer_layer_clone.find(".answer_title").attr("name", "answer_title_"+ answer_layer_count).attr("id", "answer_title_"+ answer_layer_count);
		
		answer_layer_clone.find(".only_number").remove();
		answer_layer_clone.find(".only_number_section").append('<input type="text" class="only_number" name="answer_seq_'+ answer_layer_count +'" id="answer_seq_'+ answer_layer_count +'">');
		answer_layer_clone.find(".only_number").spinner({min : 0, max:99999}).val(0);
		
		answer_layer_clone.find(".answer_event").attr("name", "answer_event_" + answer_layer_count).attr("id", "answer_event_" + answer_layer_count);
		answer_layer_clone.find(".answer_event_img").attr("name", "answer_event_img_" + answer_layer_count).attr("id", "answer_event_img_" + answer_layer_count);
		
		$(".answer_injection_section").append(answer_layer_clone);
	});
	
	$(document).on("click", ".remove_answer_layer", function() {
		$(this).parent().parent().remove();
	});
	
	var parents;
	$(".new_question_submit").on("click", function() {
		var parent_idx = $("#answer_id").val();
		if (confirm("신규 문항을 등록 하시겠습니까?")) {
			oEditors.getById["question_contents"].exec("UPDATE_CONTENTS_FIELD", []);
			data_submit($(".question_injection_container"));
		}
	});
	
	var q_title = '${question.question_title}';
	$(".edit_question_submit").on("click", function() {
		if (confirm("'"+ q_title +"' 문항을 수정 하시겠습니까?'")) {
			oEditors.getById["question_contents"].exec("UPDATE_CONTENTS_FIELD", []);
			$(".question_injection_container").attr("action", "${admin_root_path}/landing/edit/question")
			data_submit($(".question_injection_container"));
		}
	});
});
</script>
<form action="${admin_root_path}/landing/submit/question" enctype="multipart/form-data" method="post" class="question_injection_container">
	<%@include file="/WEB-INF/views/layer/admin_landing/question_load_layer.jsp"%>
	<div class="question_inejction_section">
		<div class="question_title_section">
			<input type="text" id="question_title" name="question_title" placeholder="등록할 문제의 문항(제목)을 입력해주세요." value="${question.question_title}">
		</div>
		<div class="question_contents_section">
			<textarea id="question_contents" name="question_contents"></textarea>
			<a class="important">* 문제의 보기가 필요하다면 위에 박스에 작성해주세요.</a>
		</div>
	</div>
	<div class="answer_injection_section">
		<div class="answer_inejction">
			<div>
				<input type="hidden" name="answer_layer_count" id="answer_layer_count" value="0">
				<input type="file" name="answer_img_0" id="answer_img_0" class="answer_img">
				<a class="btn-gray add_answer_layer">답안 추가</a>
			</div>
			<textarea class="answer_title" id="answer_title_0" name="answer_title_0" placeholder="답안을 입력해주세요."></textarea>
			<a class="important" style="margin-bottom:3px;">* 공백을 제외하고 1글자 이상 입력해주세요.</a><br>
			<a class="important" style="margin-top:0;">* 답안의 이미지가 필요하다면 첨부해주세요.</a>
			<div class="answer_input_section only_number_section">
				<a class="important" style="margin:0; float: left; text-align:left; color:black;">* 문항에서 보여질 답안의 순서를 지정해주세요.<br>최소 0 (숫자가 낮을수록 먼저 출력)</a>
				<input type="text" class="only_number" name="answer_seq_0" id="answer_seq_0">
			</div>
			<div class="answer_input_section">
				<a class="important" style="margin:0; float: left; text-align:left; color:black;">* 해당 답안을 선택했을경우 발생할 이벤트 명칭을 입력해주세요.<br>없다면 입력하지않아도 무관</a>
				<input type="text" placeholder="이벤트 명칭" class="answer_event" name="answer_event_0" id="answer_event_0">
			</div>
			<div class="answer_input_section">
				<a class="important" style="margin:0; float: left; text-align:left; color:black;">* 이벤트에 필요한 이미지를 첨부해주세요.<br>없다면 입력하지않아도 무관</a>
				<input type="file" class="answer_event_img" name="answer_event_img_0" id="answer_event_img_0">
			</div>
		</div>
	</div>
	<c:if test="${question_id != null && question_id.length() > 0}">
		<input type="hidden" name="question_id" value="${question_id}">
		<div class="answer_edit_section">
			<h1>기존 등록된 답안목록</h1>
			<c:forEach items="${answers}" var="answer" varStatus="status">
				<div class="answer_edit">
					<div>
						<input type="file" name="answer_img_${answer.answer_id}" id="answer_img_${answer.answer_id}" class="answer_img">
						<a class="impotertant">기존 파일명 : ${answer.answer_img == null || answer.answer_img.length() == 0 ? '없음' : answer.answer_img }</a>
						<a style="float: right;">
							<font color="red">삭제</font> <input type="checkbox" name="delete_answers" value=${answer.answer_id}>
						</a>
					</div>
					<textarea class="answer_title" id="answer_title_${answer.answer_id}" name="answer_title_${answer.answer_id}" placeholder="답안을 입력해주세요.">${answer.answer_title}</textarea>
					<a class="important" style="margin-bottom:3px;">* 공백을 제외하고 1글자 이상 입력해주세요.</a><br>
					<a class="important" style="margin-top:0;">* 기존 답안의 이미지와 교체하려면 파일을 첨부해주세요.</a>
					<div class="answer_input_section only_number_section">
						<a class="important" style="margin:0; float: left; text-align:left; color:black;">* 문항에서 보여질 답안의 순서를 지정해주세요.<br>최소 0 (숫자가 낮을수록 먼저 출력)</a>
						<input type="text" class="only_number" name="answer_seq_${answer.answer_id}" id="answer_seq_${answer.answer_id}" value="${answer.pring_seq}">
					</div>
					<div class="answer_input_section">
						<a class="important" style="margin:0; float: left; text-align:left; color:black;">* 해당 답안을 선택했을경우 발생할 이벤트 명칭을 입력해주세요.<br>없다면 입력하지않아도 무관 </a>
						<input type="text" placeholder="이벤트 명칭" class="answer_event" name="answer_event_${answer.answer_id}" id="answer_event_${answer.answer_id}" value="${answer.event_function}">
					</div>
					<div class="answer_input_section">
						<a class="important" style="margin:0; float: left; text-align:left; color:black;">* 기존 이벤트 이미지를 다른이미지로 변경하려면 파일을 첨부해주세요.</a>
						<input type="file" class="answer_event_img" name="answer_event_img_${answer.answer_id}" id="answer_event_img_${answer.answer_id}"><br>
						${answer.event_img}
					</div>
				</div>
			</c:forEach>
		</div>
	</c:if>
</form>
<div style="text-align: center; margin-top: 2vw; border-top: 3px solid #939393; padding-top: 1vw;">
<c:if test="${question_id != null && question_id.length() > 0}">
</c:if>
<c:choose>
	<c:when test="${question_id != null && question_id.length() > 0}">
		<a class="btn-darkblue edit_question_submit">수정하기</a>
	</c:when>
	<c:otherwise>
		<a class="btn-darkblue new_question_submit">등록하기</a>
	</c:otherwise>
</c:choose>
</div>