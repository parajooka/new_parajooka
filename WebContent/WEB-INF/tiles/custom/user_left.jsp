<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="menubar">
	<!-- 로고 및 좌측 메뉴바 -->
	<div class="fotter_copy">
		Daegu, Korea.
	</div>
	<div class="fotter_toTop">
		<img src="/resources/common/img/toTop.png">
	</div>
	<div class="logo_section">
		<a class="logo_img_box" href="/">
			<span class="deco_round"></span>
			<img id="logo_img" src="/resources/common/img/renewal/logo.png">
		</a>
		
		<span class="mobile_menu_open_btn"><img src="/resources/common/img/mobile_menu.png"></span>
		<div class="mobile_menu_mask"></div>
		<div class="mobile_menu_container">
			<ul>
				<li style="padding:4vw 2vw;">
					<span style="text-align: center; display: inline-block; width: 100%; color: white; font-weight: bold; font-size: 4vw;">Navigation</span>
				</li>
				<c:forEach items="${menu_list}" var="menu">
					<li>
						<a style="color:white; display: inline-block; width:100%; height: 100%; text-align: center;" href="/custom/menu/index?menu_idx=${menu.menu_idx}">${menu.menu_name}</a>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div class="left_menu_container">
			<div class="left_menu_layout">
				<div class="follw_me_container">
					<p>
						<img class="behance_logo" src="/resources/common/img/renewal/behance.png">
						<img class="instagram_logo" src="/resources/common/img/renewal/instagram.png">
					</p>
					<p style="margin-top:-5px;">
						<span style="display: inline-block; vertical-align: top; font-size: 12px;">follow us</span>
						<img class="heart_logo" src="/resources/common/img/renewal/heart_logo.gif">
					</p>
				</div>
				<div class="left_menu_layout_cell">
					<img class="menu_select_arrow" src="/resources/common/img/renewal/triangle.png">
					<!-- <div class="menu_mask_top menu_mask"></div>
					<div class="menu_mask_bottom menu_mask"></div> -->
				</div>
			</div>
			<div class="left_menu_section">
				<c:forEach begin="1" end="${menu_list.size()}">
					<p class="menu_line_box top_op_menu">
						<span class="menu_line"></span>
					</p>
				</c:forEach>
				<c:forEach items="${menu_list}" var="menu">
					<p class="menu_line_box real_menu menu_${menu.menu_name}" data-menu-name="menu_${menu.menu_name}">
						<span class="menu_line"></span>
						<a class="menu_btn" data-href="/custom/menu/index?menu_idx=${menu.menu_idx}" data-menu-name="menu_${menu.menu_name}">
							${menu.menu_name}
						</a>
					</p>
				</c:forEach>
				<c:forEach begin="1" end="${menu_list.size()}">
					<p class="menu_line_box bottom_op_menu">
						<span class="menu_line"></span>
					</p>
				</c:forEach>
			</div>
			<script type="text/javascript">
			try {
				var op_position = 0; //현재 선택된 메뉴의 index
			
				var menu = '${target_menu.menu_name}'; //선택된 메뉴의 이름
				var menu_move_timer = 500; //메뉴바의 모든 이동속도 (위아래, 양옆)
				var menu_move_cmd = '${menu_move_cmd}'; //왼쪽에서 나오는 애니메이션 여부
				
				var resizing = false;
				
				//메뉴를 클릭했을때 모든 움직임을 담당하는 함수
					var menu_move = function(target) {
					if (typeof target != "undefined" && target != null)  {
						if ($(window).width() > 1200) {
							//메뉴 좌표의 눈금 위치
							var arrow_top = $(".menu_select_arrow").offset().top;
							//사용자가 선택한 메뉴의 위치
							var target_top = $(target).offset().top;
							//눈금과 선택 메뉴의 거리
							var diff_top = arrow_top - target_top;
							//최종적으로 움직여야할 거리
							//현재위치 + 눈금과 메뉴의 거리 - 눈금의 크기 + 2 < 임의 숫자
							var result_top = $(".left_menu_section").offset().top + diff_top - $(".menu_select_arrow").height() + 2;
								result_top = result_top - $(window).scrollTop();
								
							//메뉴 전체의 글자색을 검정색으로 변경
							$(".menu_line_box").stop().animate({
								color : "black"
							}, menu_move_timer);
							
							//메뉴 전체의 눈금 투명도 설정
							$(".menu_line").stop().css("opacity", 1);
							
							//선택한 메뉴의 글자색 변경
							$(target).stop().animate({
								color : "#d87646"
							}, menu_move_timer);
							
							//선택한 메뉴의 눈금을 가리기
							$(target).find(".menu_line").stop().animate({
								opacity: 0
							}, menu_move_timer);
							
							//메뉴가 없는 눈금들 opacity 지정을위한 포지션 계산
							$.each($(".real_menu"), function(index, value) {
								if ($(value).attr("data-menu-name") == $(target).attr("data-menu-name")) {
									op_position = index;
								}
							});
							
							//메뉴가 없는 눈금들 opacity 지정 :: START
							var top_op = document.getElementsByClassName("top_op_menu");
							
							for(var i = top_op.length - 1; i >= 0; i--){
								if (i > op_position) {
							    	$(top_op[i]).stop().animate({opacity : "1"}, menu_move_timer);
								} else if (i == op_position) {
							    	$(top_op[i]).stop().animate({opacity : "0.3"}, menu_move_timer);
							    } else if (i < op_position) {
							    	$(top_op[i]).stop().animate({opacity : "0"}, menu_move_timer);
							    }
							}
			
							var bottom_op = document.getElementsByClassName("bottom_op_menu");
							
							for(var i = bottom_op.length - 1; i >= 0; i--){
							    if (i < op_position) {
							    	$(bottom_op[i]).stop().animate({opacity : "1"}, menu_move_timer);
							    } else if (i == op_position) {
							    	$(bottom_op[i]).stop().animate({opacity : "0.3"}, menu_move_timer);
							    } else if (i > op_position) {
							    	$(bottom_op[i]).stop().animate({opacity : "0"}, menu_move_timer);
							    }
							}
							
							op_position = 0;
		
							//메뉴가 없는 눈금들 opacity 지정 :: START
			
							//메뉴 위치 눈금에 맞추기위해 이동시작
							//리사이징이 아니고 현재페이지와 같은 링크를 클릭했을경우
							if (!resizing && menu == $(target).attr("data-menu-name")) {
								var href = $(target).find(".menu_btn").attr("data-href");
								location.href = href;
							} else {
								$(".left_menu_section").stop().animate({
									"top" : result_top + "px"
								}, menu_move_timer, function() {
									if (!resizing) {
										var href = $(target).find(".menu_btn").attr("data-href");
										
										//이동 완료후 0.3초뒤에 주소이동
										setTimeout(function() {
											location.href = href;
										}, 300);
									}
									
									resizing = false;
								});
							} 
						}
					}
				}
				
				$(document).ready(function(){
					$(".left_menu_container").css({
						"position" : "relative"
					});
					
					$(".left_menu_container").css("left", "-100px");
					
					$(".menu_btn").on("click", function() {
						var target = $("."+ $(this).attr("data-menu-name"));
						menu_move(target);
					});
					
					$(window).on("resize", function() {
						resizing = true;
						menu_move($("."+ menu));
					});
					
					//메뉴바 위치 선정 시작
					var section_height = $(".left_mnue_section").height();
					var margin_top = "-" + section_height/2 + "px";
					
					$(".left_menu_container").css({
						"margin-top" : -($(".logo_img_box").height() + parseInt($(".logo_img_box").css("margin-top"))) +"px"
					});
					
					$(".menu_mask").css("left", ($(".left_menu_layout").width() + 1) +"px");
					
					$(".left_menu_section").css({
						"margin-top" : margin_top,
						"top" : $(window).height()/2 + "px",
						"position" : "absolute",
						"left" : $(".left_menu_layout").width() + "px"
					});
					
					if (menu_move_cmd == "true" || menu_move_cmd.length == 0) {
						$(".left_menu_container").stop().animate({
							left : 0
						}, 900, function() {
							resizing = true;
							menu_move($("."+ menu));
						});
					} else {
						$(".left_menu_container").css("left", "0px");
					}
					
					menu = (typeof menu == "undefined" ||menu.length == 0) ? "menu_About" : "menu_"+menu;
					
					var arrow_top = $(".menu_select_arrow").offset().top;
					var target_top = $("."+ menu).offset().top;
					var diff_top = arrow_top - target_top;
					var result_top = $(window).height()/2 + diff_top - $(".menu_select_arrow").height() + 2;
					
					$(".left_menu_section").css({
						"top" : result_top + "px"
					}, 500);
	
					$("."+ menu).css("color","#d87646").find(".menu_line").css("opacity", 0);
					
					$.each($(".real_menu"), function(index, value) {
						if ($(value).attr("data-menu-name") == menu) {
							op_position = index;
						}
					});
					
					var top_op = document.getElementsByClassName("top_op_menu");
					
					for(var i = top_op.length - 1; i >= 0; i--){
					    if (i > op_position) {
					    	$(top_op[i]).css("opacity", "1");
					    } else if (i == op_position) {
					    	$(top_op[i]).css("opacity", "0.3");
					    } else if (i < op_position) {
					    	$(top_op[i]).css("opacity", "0");
					    }
					}
					
					var bottom_op = document.getElementsByClassName("bottom_op_menu");
					
					for(var i = bottom_op.length - 1; i >= 0; i--){
					    if (i < op_position) {
					    	$(bottom_op[i]).css("opacity", "1");
					    } else if (i == op_position) {
					    	$(bottom_op[i]).css("opacity", "0.3");
					    } else if (i > op_position) {
					    	$(bottom_op[i]).css("opacity", "0");
					    }
					}
					
					op_position = 0;
				});
			} catch (e) {
				// TODO: handle exception
			}
			</script>
			<!-- Mobile Menu Controller jQuery -->
			<script>
				var mobile_menu_animation = 500;
				$(".mobile_menu_open_btn").on("click", function() {
					$(".mobile_menu_mask").show().stop().animate({
						opacity : 0.5
					}, mobile_menu_animation);
					
					$(".mobile_menu_container").stop().animate({
						right : 0
					}, mobile_menu_animation);
				});
				
				$(".mobile_menu_mask").on("click", function() {
					$(".mobile_menu_mask").stop().animate({
						opacity : 0
					}, mobile_menu_animation, function() {
						$(".mobile_menu_mask").hide();
					});
					
					$(".mobile_menu_container").stop().animate({
						right : "-55vw"
					}, mobile_menu_animation);
				});
			</script>
		</div>
	</div>
	<!-- 로고 및 좌측 메뉴바 -->
</div>