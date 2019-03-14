<%@page import="org.apache.velocity.runtime.directive.Include"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
	.post_contents_container {position: relative; z-index: 3333;}
	.post_contents_container img {max-width: 100%;}
	.project_before {text-align: center; top:0; position: absolute; z-index: 9998; margin-top: 22px; font-size: 15px; left:50%;  margin-left:-52px;}
	
	a:link, a:visited, a:active {color: black;}
	
	.post_contents_container a {display: inline-block; color: #D87646 !important; margin-left: 544px; font-size: 15px;}
	.post_contents_container a:link, .post_contents_container a:visited, .post_contents_container a:active {text-decoration: underline;}
	.post_contents_container p {text-align: left !important;}
	
	@media (max-width:1200px) {
		.post_contents_container a {margin-left:0;}
		.project_before {font-size: 4vw; margin-top: 36px;}
		.post_contents_container a {font-size:5vw;}
	}
}
</style>
<div class="project_before">
	<a href="/custom/board/index?type=${thisMenu.menu_type}&menu_idx=${thisMenu.menu_idx}"><img style="width: 26px; margin-right: 10px;" src="/resources/common/img/backarrow.png">Back to Team</a>
</div>
<div class="post_contents_container">
<p><a>${thisPost.title}</a></p>
	<c:if test="${board_mobile != null && board_mobile == true}">
		${thisPost.mobile_contents}
	</c:if>
	<c:if test="${board_mobile == null || board_mobile == false}">
		${thisPost.contents}
	</c:if>
</div>
<c:if test="${admin == true}">
	<div style="text-align: right; margin-bottom:50px;">
		<a class="btn-gray" style="display: inline-block; padding:5px 15px; color: white !important;" href="/custom/board/write?menu_idx=${thisMenu.menu_idx}&post_id=${thisPost.post_id}">글 수정</a>
	</div>
</c:if>
<script>
$(document).ready(function(){
	$(".mobile_menu_open_btn").on("click", function() {
		$(".project_before").css("z-index", 1);
	});

	$(".mobile_menu_mask").on("click", function() {
		setTimeout(function() {
			$(".project_before").css("z-index", 9998);
		}, mobile_menu_animation);
	});
});
</script>
