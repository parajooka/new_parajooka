<%@page import="org.apache.velocity.runtime.directive.Include"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
	.post_input_form {width: 632px; margin: auto; margin-top: 0; background-color: white; padding: 1vw 1vw; border: 2px solid #bebebe; font-size:15px; margin-bottom:50px;}
	
	.post_title_section {margin-bottom:15px;}
	.post_title_section #title {width: 100%;}
	
	.post_select_section {padding-top: 30px; margin-top: 30px; border-top: 1px solid black;}
	.checkbox_section {float: right;}
	.check_line {clear: both;}
	
	.comment_check {margin-top: 20px; padding-top: 20px; border-top: 1.5px dashed black;}
	
	input[type=text] {font-size:12px;}
</style>
<script type="text/javascript" src="/resources/common/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script>
var OrignData = ['${thisPost.contents}', '${thisPost.explanation}', '${thisPost.mobile_contents}'];
var editorName = ['contents', 'explanation', 'mobile_contents'];
var oEditors = [];

function setEditor(n) {
	nhn.husky.EZCreator.createInIFrame({
		oAppRef : oEditors,
		elPlaceHolder : editorName[n],
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
            $(".main_info_section").find("iframe").contents().find('.se2_multy').remove();
            oEditors.getById[editorName[n]].exec("PASTE_HTML", [OrignData[n]]); //내용밀어넣기
        }
	});
}




function pasteHTML(filepath){
    var sHTML = '<img src="'+ filepath +'">';
    oEditors.getById["contents"].exec("PASTE_HTML", [sHTML]);
}

$(document).ready(function(){
	for (var i = 0; i < editorName.length; i++) {
		if (oEditors[i] == null) {
			oEditors[i] = [];
			setEditor(i);
		}
	}
	
	$(document).on("change", "#post_main_img", handleImgFileSelect);
	
    wrapFile();
    
    var submit_url = '${thisPost == null ? "/custom/board/write/post/submit" : "/custom/board/write/post/modify"}';
    var ment = '${thisPost == null ? "새로운 게시글을 작성하시겠습니까?" : "게시글을 정말 수정하시겠습니까?"}';
    
    $('.post_submit').on('click', function() {
    	if (confirm(ment)) {
    		for (var i = 0; i < editorName.length; i++) {
   				oEditors.getById[editorName[i]].exec("UPDATE_CONTENTS_FIELD", []);
    		}
    		
    		
    		$(".post_input_form").attr("action", submit_url);
    		data_submit($(".post_input_form"));
    	}
    });
});
</script>
<form class="post_input_form" enctype="multipart/form-data" method="post">
	<c:choose>
		<c:when test="${thisPost != null}">
			<input type="hidden" name="menu_id" id="menu_id" value="${thisMenu.menu_idx}">
			<input type="hidden" name="post_id" id="post_id" value="${thisPost.post_id}">
			<h1>'${thisPost.title}' 수정</h1>
		</c:when>
		<c:otherwise>
			<input type="hidden" name="menu_id" id="menu_id" value="${thisMenu.menu_idx}">
			<h1>'${thisMenu.menu_name}' 메뉴 게시글 작성</h1>
		</c:otherwise>
	</c:choose>
	<div class="post_title_section">
		<input type="text" name="title" id="title" placeholder="글의 제목을 입려해주세요." value="${thisPost.title}">
	</div>
	<div class="post_contents_section" style="margin-top:20px;">
		<p>내용을 입력해주세요.(필수)</p>
		<textarea id="contents" name="contents"></textarea>
	</div>
	<div class="post_contents_section" style="margin-top:20px;">
		<p>모바일 내용을 입력해주세요.(필수)</p>
		<textarea id="mobile_contents" name="mobile_contents"></textarea>
	</div>
	<div class="post_contents_section main_info_section" style="margin-top:20px;">
		<p>메인 설명을 입력해주세요.(선택)</p>
		<textarea id="explanation" name="explanation"></textarea>
	</div>
	<div class="post_select_section">
		<div class="preview_img_section">
			<c:choose>
				<c:when test="${thisPost != null}">
					대표 이미지 || [이미지 수정을 원하실 경우 업로드해주세요.]<br>
					[기존 파일 : ${thisPost.main_img}]
				</c:when>
				<c:otherwise>
					대표 이미지 || [갤러리일 경우 메인화면에 나타날 대표 이미지를 설정해주세요.]
				</c:otherwise>
			</c:choose>
			<input type="file" name="post_main_img" id="post_main_img">
		</div>
		<div class="comment_check check_line">
			<div>
				<c:choose>
					<c:when test="${thisPost != null}">
						첨부파일 || [첨부파일 수정을 원하실 경우 업로드해주세요.]<br>
						[기존 파일 : ${thisFile == null || thisFile.size() == 0 || thisFile.get(0) == null ? '첨부파일 없음' : thisFile.get(0).file}]
					</c:when>
					<c:otherwise>
						첨부파일 || [해당 게시글의 첨부파일을 업로드 해보세요.]
					</c:otherwise>
				</c:choose>
				<input type="file" name="post_append_file" id="post_append_file">
			</div>
		</div>
		<div class="comment_check check_line">
			<div>
				댓글 
				<a class="checkbox_section">
					<input type="radio" name="comment_acc" id="comment_acc" value="0" ${thisPost == null || thisPost.comment_acc == 0 ? 'checked="checked"' : ''}> 허용
					<input type="radio" name="comment_acc" id="comment_acc" value="1" ${thisPost != null && thisPost.comment_acc == 1 ? 'checked="checked"' : ''}> 비허용
				</a>
			</div>
		</div>
	</div>
	<div class="comment_check" style="text-align: center;">
		<a class="btn-gray post_submit" style="display: inline-block; padding: 0.5vw 2vw; font-size: 16px;">제출</a>
	</div>
</form>