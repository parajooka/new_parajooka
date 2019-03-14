<%@page import="org.apache.velocity.runtime.directive.Include"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
	.project_container {width:100%;position: relative; margin-top: 33px;}
	.project_year {text-align: center; top:0; position: absolute; z-index: 9999; margin-top: 22px; font-size: 14px; left:50%; }
	.project_section {width: 100%; text-align: center;}
	.project_section .project_layout {display: inline-block; margin-right: 4%; width: 30%; vertical-align: top;}
	.project_section .project_layout img {width:100%;}
	
	.project_wrapper {margin-bottom: 150px;}
	.project_title, .project_explanation {display: inline-block; width: 100%; text-align: left;}
	.project_title {color: #d87646; font-size: 13px;}
	.project_explanation {font-size: 13px; line-height: 27px;}
	
	.call_project {cursor: pointer;}
	
	a:link, a:visited, a:active {color: black;}
	
	.post_view {cursor: pointer;}
    .project_layout_4 {display: none !important;}
	
	@media (max-width:1200px) {
		.project_wrapper {margin-bottom: 15vw;}
        .project_section .project_layout {width:100%; margin:0; display: none;}
        .project_layout_4 {display: block !important;}
		.project_title {color: #d87646; font-size: 3.5vw;}
		.project_explanation {font-size: 3.5vw; line-height: 6.5vw;}
		.project_year {margin-top:36px; font-size:4vw;}
	}
</style>
<script>
$(document).ready(function() {
	$(".project_year").css("margin-left", "-"+ ($(".project_year").width()/2) + "px");
	
	$(".delete_post").on("click", function() {
		if (confirm("선택한 포스터들을 모두 삭제하시겠습니까?\r\n삭제후에는 복원이 절대 불가능합니다.")) {
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
				
				var delete_post_targets = [];
				
				$.each($(".remove_checkbox"), function(index, value) {
					if ($(value).prop("checked") == true) {
						delete_post_targets.push($(value).val());
					}
				});
				
				
				get_item_info("/custom/board/delete", func, {"delete_post_targets":delete_post_targets}, $('body'));
			}
		}
	});
	
	$(".mobile_menu_open_btn").on("click", function() {
		$(".project_year").css("z-index", 1);
	});

	$(".mobile_menu_mask").on("click", function() {
		setTimeout(function() {
			$(".project_year").css("z-index", 9998);
		}, mobile_menu_animation);
	});
	

	$(".project_year").css("margin-left", "-"+ $(".project_year").width()/2 + "px");
});

$(window).on("resize", function() {
	$(".project_year").css("margin-left", "-"+ $(".project_year").width()/2 + "px");
});

var project_index = 1;
var menu_idx = '${menu_idx}';
function InjectionProject(img_src, title, explanation, id) {
	$(".project_layout_"+ project_index).append("<div class='project_wrapper'>"+
												"<a class='post_view' href='/custom/board/view?menu_idx="+ menu_idx +"&post_idx="+ id +"'>"+
												"<img src='"+ img_src +"'>"+
												"<span class='project_title'>"+ title +
												<c:if test="${admin == true}">
												"<input class='remove_checkbox' type='checkbox' value='"+ id +"' style='float:right;'>"+
												</c:if>
												"</span>"+
												"<span class='project_explanation'>"+ explanation +"</span></a></div>");
	
	if (project_index == 3) {
		project_index = 1;
	} else {
		project_index ++;
	}
}
</script>
<c:if test="${post_list != null && post_list.size() > 0}">
<div class="project_year">
	<a href="/custom/board/index?type=${menu_type}&menu_idx=${menu_idx}&post_year=all" class='call_project'>All</a>
	<c:forEach items="${post_year}" var="year" varStatus="status">
		 / <a href="/custom/board/index?type=${menu_type}&menu_idx=${menu_idx}&post_year=${year}" class='call_project'>${year}</a>
	</c:forEach>
</div>
<div class="project_container">
	<div class="project_section">
		<div class="project_layout_1 project_layout">
		</div>
		<div class="project_layout_2 project_layout">
		</div>
		<div class="project_layout_3 project_layout" style="margin-right: 0;">
		</div>
		<div class="project_layout_4 project_layout">
			<c:forEach items="${post_list}" var="post">
				<div class='project_wrapper'>
					<a class='post_view' href='/custom/board/view?menu_idx=${menu_idx}&post_idx=${post.post_id}'>
						<c:choose>
							<c:when test="${post.main_img == null || post.main_img.length() == 0}">
								<img src="/resources/common/img/no-image.png">
							</c:when>
							<c:otherwise>
								<img src="/res/img/custom/board/${menu_idx}/${post.post_id}/${post.main_img}">
							</c:otherwise>
						</c:choose>
						<span class='project_title'>
							${post.title}
							<c:if test="${admin == true}">
								<input class='remove_checkbox' type='checkbox' value='${post.post_id}' style='float:right;'>
							</c:if>
						</span>
						<span class='project_explanation'>${post.explanation}</span>
					</a>
				</div>
			</c:forEach>
		</div>
		<script type="text/javascript">
			<c:forEach items="${post_list}" var="post">
				<c:choose>
					<c:when test="${post.main_img == null || post.main_img.length() == 0}">
						InjectionProject('/resources/common/img/no-image.png', '${post.title}', '${post.explanation}', '${post.post_id}');
					</c:when>
					<c:otherwise>
						InjectionProject('/res/img/custom/board/${menu_idx}/${post.post_id}/${post.main_img}', '${post.title}', '${post.explanation}', '${post.post_id}');
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</script>
	</div>
</div>
</c:if>
<c:if test="${post_list == null || post_list.size() == 0}">
	<div style="width: 100%; height: 80vh; display: table; margin-top:-80px;">
		<div style="width: 100%; height: 100%; display: table-cell; vertical-align: middle; text-align: center;">
			해당 메뉴에 작성된 게시글이 존재하지 않습니다.
		</div>
	</div>
</c:if>
<c:if test="${admin == true}">
	<div style="text-align: right; margin-bottom:50px;">
		<a class="btn-gray" style="display: inline-block; padding:5px 15px; color: white !important;" href="/custom/board/write?menu_idx=${menu_idx}">글 작성</a>
		<a class="btn-gray delete_post" style="float:left; display: inline-block; padding:5px 15px; color: white !important;">포스터 삭제</a>
	</div>
</c:if>
