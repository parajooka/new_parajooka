<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.container {margin-top:0;}
.landing_landing_container {width:100%; height:100vh; background-color:#000; position: absolute; z-index: 99999; display: table; top:0; left: 0; opacity: 0;}
.landing_landing_section {width:100%; height:100%; display: table-cell; vertical-align: middle; text-align: center; color: #424347; font-size:17px;}

.question_contianer {width:100%; min-height:100vh; position: relative; display: table; opacity: 0; font-size:15px;}
.question_section {width:100%; height:100%; display: table-cell; vertical-align: middle;}
.question_section_box {width:800px; margin:auto;}
.answer_title {opacity:0; margin-top:45px;}

.op_input {background: none; box-shadow: none; outline: none; border: none; text-align:center; border-bottom: 1px solid #a5a5a5; width: 72px; margin-right: 5px; font-size:15px;}
.arrow {position: absolute; bottom: -99%; left: 62px; width: 8px;}

.submit_info {border: 1px solid #bbbab6; padding: 2px 15px; color: #bbbab6; margin-left: 20px; cursor:pointer;}

.deco_round_section {display: inline-block; vertical-align: top; margin-right: 10px;}
.first_question_deco {margin-top:4px !important; margin-left:0 !important; background-color: #48744c;}

.answer_section {display:inline-block;}

.answer_title .answer_section {margin-right: 50px;}
.answer_title .answer_section:last-child {margin-left: 0px !important;}

.target_answer {cursor:pointer;}
.message_box {position: fixed; bottom: 0; font-size: 15px; border: 1px solid #888; padding: 8px 10px;}
.message_box span {display: inline-block; vertical-align: top;}

.mobile_next_btn {display: none;}

.question_title img {max-height: 50vh; max-width:100%;}
.contents_img_box {text-align: center; margin-top:20px;}

input::placeholder {
	color :#bbbab6;
}

.point_container {text-align: center; width:100%;}
.point_section {display: inline-block; position: relative;}
.point_btn {display: inline-block; font-size: 15px; border: 1px solid #292020; padding: 10px 17px; margin: 0 6px;}
.umyes {display: block; text-align: right; font-size: 10px; margin-top:5px;}
.umno {display: block; text-align: left; font-size: 10px; margin-bottom:5px;}

.mobile_point_container {display: none;}
#progressbar1 {height:3px; width: 300px; margin:auto; margin-top:10px;}

.ui-widget.ui-widget-content {border:none !important;}
.ui-widget-header {background:#D87646;}

.contact_clear {display: none; width:800px; margin:auto; color:#d87646;}
.clear_content {height: 193px; text-align: center; line-height:27px;}
.clear_btn {text-align: center; margin-top: 8px; visibility: hidden; opacity: 0;}
.go_contact_btn {display: inline-block; border: 1px solid #575757; padding: 2px 15px; cursor: pointer; color: black; font-size: 13px;}
.clear_title {text-align: center; font-family: 'Gothic NeoSB' !important; font-weight: bold; font-size: 27px; margin-bottom: 12px;}

@media (max-width:1200px) {
	#progressbar1 {width: 100%;}
	.message_box {font-size: 3vw; padding: 2vw 2vw;}
	.question_section_box {width:100%; margin-top:-19vh;}
	.question_contianer {font-size:3.5vw;}
	.typing_container {font-size:3.5vw !important;}
	.op_input {font-size:3.5vw; width:20vw;}
	.submit_info {display: none;}
	.mobile_next_btn {display: inline-block !important; width: 98%; margin: 0; padding: 1vw 0; margin-top: 11vw;}
	.deco_round_section {margin-right:1vw;}
	.first_question_deco {width: 2vw; height: 2vw; margin-top: 1.4vw !important;}
	.arrow {width:2vw;}
	
	.answer_title {margin-top:5vw;}
	.answer_title .answer_section {min-width:100%; margin:0; margin-top:3vw;}
	
	.landing_landing_section {font-size:3.5vw;}
	video {width:100% !important; margin:0 !important; margin-top:-20vw !important;}
	.question_title img {max-width: 100%; max-height: none;}
	
	.message_box {display: none;}
	.target_answer {max-width:86vw;} 
	
	.point_container {display: none;}
	.mobile_point_container {width:100%; text-align: center; display: block;}
	.mobile_point_section {display: inline-block;}
	.mobile_point_btn {font-size: 5vw; border: 1px solid #292020; width: 21vw; padding: 7vw 0; display: inline-block; margin: 1vw 1vw;}
	.mobile_umyes {text-align: right; display: block;}
	.mobile_umno {text-align: left; display: block;}
	
	.contact_clear {display: none; width:100%; margin:auto; color:#d87646;}
	.clear_title {font-size: 8vw; margin-bottom: 3vw;}
	.clear_content {height: 48vw; text-align: center; line-height: 7.5vw;}
	.clear_btn {margin-top:2vw;}
	.go_contact_btn {border: 1px solid #575757; padding: 1vw 5vw; font-size: 3.5vw;}
	.contact_clear {margin-top:7vw;}
}
</style>
<script src="/resources/common/js/jQuery_mechanical_typing.js"></script>
<script>
var hide_speed = 300;
var total_time = 3;
var max_data = 100; //20 -> 0.2
var current_time = 0;
var delay_time = 0.01;
var refresh_interval = delay_time * 1000;

var timer;
 
function refresh_bar() {
  if(current_time > max_data) {
    clearInterval( timer );
    current_time = max_data;
  }
  
  var second_current = max_data / total_time;
  var mille_data = 100/(delay_time * 100);
  var milli_current = second_current/mille_data;
  	  
  $( "#progressbar1" ).progressbar({ value: current_time});
  current_time += milli_current;
}

/**
 * 문항 타이틀부분에 글을쓰는 함수
 */
function write(dom, message, typing_func, end_callback) {
	
	//답안부분을 먼저 가린다
	$(".answer_title").stop().animate({
		opacity : 0
	}, hide_speed, function() {
		$(".answer_title").css("visibility", "hidden");
		$(dom).typing({
			typing_speed: 0.1,
			del_speed : 0.1,
			text : message,
			color : '#d87646',
			font_size : '15px',
			line_height : '28px',
			min_height: '45px',
			background : "none",
			cursor_width : '0.5',
			cursor_color : '#d87646',
			load_end_callback : null,
			typing_start_callback : typing_func,
			typing_end_callback : end_callback
		});
	});
}

/**
 * 화살표가 위아래로 움직이는 함수
 */
var arrow_move_falg = true;
function arrow_move() {
	if (arrow_move_falg) {
		var move_y = $(window).width() > 1200 ? "-85%" : "-70%"
		$(".arrow").stop().animate({
			bottom: move_y
		}, 500, function() {
			var move_y2 = $(window).width() > 1200 ? "-99%" : "-80%"
			$(".arrow").stop().animate({
				bottom: move_y2
			}, 500, function() {
				arrow_move();
			});
		});
	}
}


/**
 * 답안 클릭시에 로고 위에점이 깜빡이도록하는 함수
 */
var dotted_change_flag = true;
var dotted_change = function() {
	if (dotted_change_flag) {
		$(".logo_img_box > .deco_round").stop().animate({
			backgroundColor:"#48744c"
		}, 300, function() {
			$(".logo_img_box > .deco_round").stop().animate({
				backgroundColor:"#ff5400"
			}, 300, function() {
				dotted_change();
			});
		});
	}
}

$(window).on("load", function() {
	var background = $("body").css("background-color");
	var fade_speed = 500;

	$(".message_box").css({
		"left" : "50%",
		"margin-left" : "-"+ ($(".message_box").width()/2 + parseInt($(".message_box").css("padding-left"))) + "px",
		"bottom" : "-"+ ($(".message_box").height() * 2) + "px"
	});
	
	var landing_landing_timer = 1;
	
	setTimeout(function() {
		//랜딩의 랜딩 감추기
		$(".landing_landing_container").fadeOut(1, function() {
			//랜딩 감춘후 body색 원래색으로 변경
			/* $("body").stop().animate({
				backgroundColor:background
			}, fade_speed); */
			
			$(".message_box").stop().animate({
				bottom: "10px" 
			 }, hide_speed);
			
			//동시에 문항 출력부분 투명도 삭제
			$(".question_contianer").stop().animate({
				opacity : 1
			}, 1, function() {
				write($(".question_title"), "조금 늦으셨네요!\\방문 예약하셨던 Mr.P님 이시죠?", function(){
				}, function() {
					$(".answer_title").css("visibility", "visible").stop().animate({
						opacity : 1
					}, hide_speed);
					
					//input에 값이 미리 입력되어있다면 밑줄제거
					
					$.each($(".op_input"), function(index,value) {
						if ($(value).val().length > 0) {
							$(value).css("border", "none");
							
							thisValue = $(value).val()
							
							$(value).focus().val("").val(thisValue).blur();
						}
					});
				});
			});
		});
		
		//$("body").css("background-color", "#fff");
	}, landing_landing_timer);
});

$(window).on("resize", function() {
	$(".message_box").css({
		"left" : "50%",
		"margin-left" : "-"+ ($(".message_box").width()/2 + parseInt($(".message_box").css("padding-left"))) + "px",
		"bottom" : "10px"
	});
	
	if ($(window).width() > 1200) {
		$(".question_section_box").css("margin-top", "auto");
	} else {
		if ($(".question_title").find(".contents_img_box").length == 0) {
			$(".question_section_box").css("margin-top", "-19vh");
		} else {
			$(".question_section_box").css("margin-top", "auto");
		}
	}
	
});

$(document).ready(function() {
	var move_x = ["64px", "177px", "354.5px"];
	var move_x2 = ["18vw", "50vw", "41vw"];
	
	//화살표 움직이는 함수 실행
	arrow_move();
	//입력창에 포커스가 들어갈시 placeholder 삭제 및 아래선 삭제
	$(".op_input").on("focusin", function() {
		$(this).attr("placeholder", "");
		$(this).css("border-bottom", "none");
	});
	
	$(document).on("mouseover", ".point_btn", function() {
		$(this).css({
			"background-color" : "#292020",
			"color" : "white"
		});
	});
	
	$(document).on("mouseleave", ".point_btn", function() {
		$(this).css({
			"background-color" : "unset",
			"color" : "black"
		});
	});
	
	//이름 입력칸에서 포커스가 나올시
	$("#custom_name").on("focusout", function() {
		//placeholder를 다시 추가한다. 어차피 글있으면 안보임
		$(this).attr("placeholder", "팀쿡");
		
		//이름 입력칸에 내용이 없을경우 화살표를 다시 이름 입력칸으로 이동,
		//다음 버튼 색깔 비활성화
		if ($("#custom_name").val().length == 0) {
			$(".arrow").css("left", ($(window).width() > 1200) ? move_x[0] : move_x2[0]).css("margin-bottom", "0px");
			$(".submit_info").css({
				"border" : "1px solid #bbbab6",
				"color" : "#bbbab6"
			});
		//소속 입력칸에 내용이 없을경우 화살표를 다시 소속 입력칸으로 이동
		//다음 버튼 색깔 비활성화
		} else if ($("#custom_company").val().length == 0) {
			$(".arrow").css("left", ($(window).width() > 1200) ? move_x[1] : move_x2[1]).css("margin-bottom", "0px");
			$(".submit_info").css({
				"border" : "1px solid #bbbab6",
				"color" : "#bbbab6"
			});
		}
		
		//이름 입력칸에 내용이 없을경우 밑줄 재생성
		if ($("#custom_name").val().length == 0) {
			$("#custom_name").css("border-bottom", "1px solid #a5a5a5");
		} 
		
		//소속 입력칸에 내용이 없을경우 밑줄 재생성
		if ($("#custom_company").val().length == 0) {
			$("#custom_company").css("border-bottom", "1px solid #a5a5a5");
		}
	});
	
	$("#custom_company").on("focusout", function() {
		$(this).attr("placeholder", "애플");
		
		//이름 입력칸에 내용이 없을경우 화살표를 다시 이름 입력칸으로 이동,
		//다음 버튼 색깔 비활성화
		if ($("#custom_name").val().length == 0) {
			$(".arrow").css("left", ($(window).width() > 1200) ? move_x[0] : move_x2[0]).css("margin-bottom", "0px");
			$(".submit_info").css({
				"border" : "1px solid #bbbab6",
				"color" : "#bbbab6"
			});
		//소속 입력칸에 내용이 없을경우 화살표를 다시 소속 입력칸으로 이동
		//다음 버튼 색깔 비활성화
		} else if ($("#custom_company").val().length == 0) {
			$(".arrow").css("left", ($(window).width() > 1200) ? move_x[1] : move_x2[1]).css("margin-bottom", "0px");
			$(".submit_info").css({
				"border" : "1px solid #bbbab6",
				"color" : "#bbbab6"
			});
		}
		
		//이름 입력칸에 내용이 없을경우 밑줄 재생성
		if ($("#custom_name").val().length == 0) {
			$("#custom_name").css("border-bottom", "1px solid #a5a5a5");
		} 
		
		//소속 입력칸에 내용이 없을경우 밑줄 재생성
		if ($("#custom_company").val().length == 0) {
			$("#custom_company").css("border-bottom", "1px solid #a5a5a5");
		}
	});
	
	$("#custom_name").on("focusin", function() {
		//소속 입력란에 포커스가 들어갈시
		//내용이 1글자 이상이면 클릭버튼으로 화살표 이동,
		//내용이 없다면 소속 입력란으로 화살표 이동
		if ($("#custom_company").val().length == 0) {
			$(".arrow").css("left", ($(window).width() > 1200) ? move_x[1] : move_x2[1]).css("margin-bottom", "0px");
		} else {
			if ($(window).width() > 1200) {
				$(".arrow").css("left", move_x[2]);
			} else {
				$(".arrow").css({
					"left" : move_x2[2],
					"margin-bottom" : "-20vw"
				});
			}
			
			$(".submit_info").css({
				"border" : "1px solid #575757",
				"color" : "black"
			});
		}
	});
	
	$("#custom_company").on("focusin", function() {
		//이름 입력란에 포커스가 들어갈시
		//내용이 1글자 이상이면 클릭버튼으로 화살표 이동,
		//내용이 없다면 이름 입력란으로 화살표 이동
		if ($("#custom_name").val().length == 0) {
			$(".arrow").css("left", ($(window).width() > 1200) ? move_x[0] : move_x2[0]).css("margin-bottom", "0px");
		} else {
			if ($(window).width() > 1200) {
				$(".arrow").css("left", move_x[2]);
			} else {
				$(".arrow").css({
					"left" : move_x2[2],
					"margin-bottom" : "-20vw"
				});
			}
			
			$(".submit_info").css({
				"border" : "1px solid #575757",
				"color" : "black"
			});
		}
	});

	//이름과 소속을 서버로 전송하는 함수
	var first_ajax = function() {
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null) {
				if (typeof data['message'] != "undefined" && data['message'].length > 0) {
					alert(data['message']);
				}
				
				if (data['next_url'] != null) {
					location.href = data['next_url'];
				}
				
				$(".first_question_deco").css("background-color", "#48744c");
				return;
			}
			
			//화살표 움직이는 함수 중지
			if (arrow_move_falg) {arrow_move_falg = false;}
			
			var obj = data['object'];
			var question = obj['question'];
			var answer = obj['answer'];
			
			dotted_change_flag = true;
			dotted_change();
			
			setTimeout(function() {
				dotted_change_flag = false;
			}, 1800);
			
			$(".answer_title").stop().animate({
				opacity : 0
			}, hide_speed, function() {
				var dec_change_flag = true;
				$(".question_title").find(".contents_img_box").remove();
				$(".question_title").css("display", "block");
				for (var i = 0; i < question.length; i++) {
					var t_question = question[i];
					
					write($(".question_title"), t_question["question_title"], function() {
						$(".answer_title").empty();
						$(".question_title").find(".contents_img_box").remove();
						
						var temp_dom;
						var t_answer = answer[t_question['question_id']];
						
						//문항 내용중 이미지가 존재한다면.
						if (t_question["question_contents"] != null && typeof t_question["question_contents"] != "undefined" && t_question["question_contents"].length > 0) {
							$(".answer_title").css("display", "none");
							temp_dom = "<div class='contents_img_box'>"+ t_question["question_contents"] +"<div id='progressbar1'></div></div>";
							
							//화면이 1200보다 작을경우 마진을 없애준다.
							if ($(window).width() <= 1200) {
								$(".question_section_box").css("margin-top", "0");
							}
							
							//=====PC용 포인트 버튼 추가 :: 시작
							$(".answer_title").append(
									'<div class="point_container">'+
										'<a class="point_section">'+
											'<span class="umno">별로인걸?</span>'+
										'</a>'+
									'</div>'
							);
							
							//1 ~ 9번까지의 답안을 넓게 뿌려줌
							for (var x = 0; x < t_answer.length; x++) {
								$(".answer_title").find(".point_section").append(
									"<span class='target_answer point_btn' data-value='"+ t_answer[x]['answer_id'] +"'>"+ t_answer[x]['answer_title'] +"</span>"
								);
							}
							
							$(".answer_title").find(".point_section").append(
									"<span class='umyes'>너무좋아!</span>"
							);
							//=====PC용 포인트 버튼 추가 :: 종료
							
							
							//=====Mobile용 포인트 버튼 추가 :: 시작
							$(".answer_title").append(
									'<div class="mobile_point_container">'+
										'<a class="mobile_point_section">'+
											'<span class="mobile_umno">별로인걸?</span>'+
										'</a>'+
									'</div>'
							);
							
							var flag = 0;
							//1 ~ 9번까지의 답안을 넓게 뿌려줌
							for (var x = 0; x < t_answer.length; x++) {
								var temp_doms;
								
								temp_doms = (flag == 2) ? "<span class='target_answer mobile_point_btn' data-value='"+ t_answer[x]['answer_id'] +"'>"+ t_answer[x]['answer_title'] +"</span><br>" : "<span class='target_answer mobile_point_btn' data-value='"+ t_answer[x]['answer_id'] +"'>"+ t_answer[x]['answer_title'] +"</span>"
										
								$(".answer_title").find(".mobile_point_section").append(temp_doms);
								
								flag ++;
								
								if (flag > 2) {
									flag = 0;
								}
							}
							
							$(".answer_title").find(".mobile_point_section").append(
									"<span class='mobile_umyes'>너무좋아!</span>"
							);
							//=====Mobile용 포인트 버튼 추가 :: 종료
							
							//중앙 정령 및 이미지 삽입
							$(".question_title").css("text-align", "center").append(temp_dom).find(".contents_img_box").css({
								"visibility" : "hidden",
								"opacity" : 0
							});
						//이미지가 존재하지 않는다면.
						} else {
							$(".answer_title").css("display", "block");
							//왼쪽 글자 정렬
							$(".question_title").css("text-align", "left");
							if ($(window).width() <= 1200) {
								$(".question_section_box").css("margin-top", "-19vh");
							}
							
							for (var x = 0; x < t_answer.length; x++) {
								$(".answer_title").append(
										'<div class="answer_section">'+
											'<p class="deco_round_section">'+
												'<span class="deco_round first_question_deco"></span>'+
											'</p>'+
											'<p class="target_answer" data-value="'+ t_answer[x]['answer_id'] +'" style="position:relative; display: inline-block;">'+
												''+ t_answer[x]['answer_title'] +''+
											'</p>'+
										'</div>'
								);
							}
						}
					}, function() {
						if ($(".answer_title").find(".mobile_point_section").length > 0 || $(".answer_title").find(".point_section").length > 0) {
							 $( "#progressbar1" ).progressbar({ max: max_data });
							  current_time = 0;
							  refresh_bar();
							  timer = setInterval(refresh_bar, refresh_interval);
							 
							setTimeout(function() {
								$(".question_title").css("display", "none");
								$(".answer_title").css({
									"visibility" : "visible",
									"opacity" : 1,
									"display": "block"
								});
							}, total_time * 1000);
						} else {
							$(".answer_title").css("visibility", "visible").stop().animate({
								opacity : 1
							}, hide_speed);
						}
						
						//타이핑이 끝난후 이미지를 보여준다.
						$(".question_title").find(".contents_img_box").css({
							"visibility" : "visible"
						}).stop().animate({
							opacity : 1
						}, hide_speed);
					});
				}
			});
		}
		
		
		get_item_info("/custom/renewal/landing/send/customer/info", func, {"user_name":$("#custom_name").val(), "user_company":$("#custom_company").val()}, $('.ace'));
	}
	
	$(".submit_info").on("click", function() {
		if (!$(".answer_title").is(":animated") && $(".answer_title").css("opacity") == 1) {
			if ($(".first_question_deco").css("background-color") == "#ff5400" || $(".first_question_deco").css("background-color") == "rgb(255, 84, 0)") {
				first_ajax();
			} else if (!$(".first_question_deco").is(":animated") && $(".first_question_deco").css("background-color") != "#ff5400" && $(".first_question_deco").css("background-color") != "rgb(255, 84, 0)") {
				$(".first_question_deco").stop().animate({
					width:"7px",
					height:"7px",
					backgroundColor:"#ff5400"
				}, function() {
					first_ajax();
				});
			}
		}
	});
	
	var next_ajax = function(answer_id) {
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null && data['message'] != "ClearContact") {
				if (typeof data['message'] != "undefined" && data['message'].length > 0) {
					alert(data['message']);
				}
				
				if (data['next_url'] != null) {
					location.href = data['next_url'];
				}
				
				$(".first_question_deco").css("background-color", "#48744c");
				return;
			} else if (data['message'] == "ClearContact") {
				var next_url = data['next_url'];
				
				$(".question_section_box").fadeOut(hide_speed, function() {
					setTimeout(function() {
						$(".contact_clear").fadeIn(1500, function() {
							console.log(next_url);
							$(".clear_btn").css("visibility", "visible").stop().animate({
								opacity: 1
							}, 1000);
							
							$(".go_contact_btn").attr("href", next_url);
						});
					}, 500);
				});
				
				return;
			}
			
			var obj = data['object'];
			var question = obj['question'];
			var answer = obj['answer'];
			
			dotted_change_flag = true;
			dotted_change();
			
			setTimeout(function() {
				dotted_change_flag = false;
			}, 1800);
			
			$(".answer_title").stop().animate({
				opacity : 0
			}, hide_speed, function() {
				if ($(".question_title").find(".contents_img_box").length > 0) {
					$(".answer_title").empty();
				}
				
				$(".question_title").css("display", "block");
				$(".question_title").find(".contents_img_box").css("visibility", "hidden")
				for (var i = 0; i < question.length; i++) {
					var t_question = question[i];
					$(".question_title").find(".contents_img_box").stop().animate({
						opacity : 0
					}, hide_speed, function() {
						$(".question_title").find(".contents_img_box").css("visibility", "hidden");
					});
					write($(".question_title"), t_question["question_title"], function() {
						$(".answer_title").empty();
						$(".question_title").find(".contents_img_box").remove();
						
						var temp_dom;
						var t_answer = answer[t_question['question_id']];
						
						//문항 내용중 이미지가 존재한다면.
						if (t_question["question_contents"] != null && typeof t_question["question_contents"] != "undefined" && t_question["question_contents"].length > 0) {
							$(".answer_title").css("display", "none");
							temp_dom = "<div class='contents_img_box'>"+ t_question["question_contents"] +"<div id='progressbar1'></div></div>";
							
							//화면이 1200보다 작을경우 마진을 없애준다.
							if ($(window).width() <= 1200) {
								$(".question_section_box").css("margin-top", "0");
							}
							
							//=====PC용 포인트 버튼 추가 :: 시작
							$(".answer_title").append(
									'<div class="point_container">'+
										'<a class="point_section">'+
											'<span class="umno">별로인걸?</span>'+
										'</a>'+
									'</div>'
							);
							
							//1 ~ 9번까지의 답안을 넓게 뿌려줌
							for (var x = 0; x < t_answer.length; x++) {
								$(".answer_title").find(".point_section").append(
									"<span class='target_answer point_btn' data-value='"+ t_answer[x]['answer_id'] +"'>"+ t_answer[x]['answer_title'] +"</span>"
								);
							}
							
							$(".answer_title").find(".point_section").append(
									"<span class='umyes'>너무좋아!</span>"
							);
							//=====PC용 포인트 버튼 추가 :: 종료
							
							
							//=====Mobile용 포인트 버튼 추가 :: 시작
							$(".answer_title").append(
									'<div class="mobile_point_container">'+
										'<a class="mobile_point_section">'+
											'<span class="mobile_umno">별로인걸?</span>'+
										'</a>'+
									'</div>'
							);
							
							var flag = 0;
							//1 ~ 9번까지의 답안을 넓게 뿌려줌
							for (var x = 0; x < t_answer.length; x++) {
								var temp_doms;
								
								temp_doms = (flag == 2) ? "<span class='target_answer mobile_point_btn' data-value='"+ t_answer[x]['answer_id'] +"'>"+ t_answer[x]['answer_title'] +"</span><br>" : "<span class='target_answer mobile_point_btn' data-value='"+ t_answer[x]['answer_id'] +"'>"+ t_answer[x]['answer_title'] +"</span>"
										
								$(".answer_title").find(".mobile_point_section").append(temp_doms);
								
								flag ++;
								
								if (flag > 2) {
									flag = 0;
								}
							}
							
							$(".answer_title").find(".mobile_point_section").append(
									"<span class='mobile_umyes'>너무좋아!</span>"
							);
							//=====Mobile용 포인트 버튼 추가 :: 종료
							
							//중앙 정령 및 이미지 삽입
							$(".question_title").css("text-align", "center").append(temp_dom).find(".contents_img_box").css({
								"visibility" : "hidden",
								"opacity" : 0
							});
						//이미지가 존재하지 않는다면.
						} else {
							$(".answer_title").css("display", "block");
							//왼쪽 글자 정렬
							$(".question_title").css("text-align", "left");
							if ($(window).width() <= 1200) {
								$(".question_section_box").css("margin-top", "-19vh");
							}
							
							for (var x = 0; x < t_answer.length; x++) {
								$(".answer_title").append(
										'<div class="answer_section">'+
											'<p class="deco_round_section">'+
												'<span class="deco_round first_question_deco"></span>'+
											'</p>'+
											'<p class="target_answer" data-value="'+ t_answer[x]['answer_id'] +'" style="position:relative; display: inline-block;">'+
												''+ t_answer[x]['answer_title'] +''+
											'</p>'+
										'</div>'
								);
							}
						}
					}, function() {
						if ($(".answer_title").find(".mobile_point_section").length > 0 || $(".answer_title").find(".point_section").length > 0) {
							 $( "#progressbar1" ).progressbar({ max: max_data });
							  current_time = 0;
							  refresh_bar();
							  timer = setInterval( refresh_bar, refresh_interval );
							 
							setTimeout(function() {
								$(".question_title").css("display", "none");
								$(".answer_title").css({
									"visibility" : "visible",
									"opacity" : 1,
									"display": "block"
								});
							}, total_time * 1000);
						} else {
							$(".answer_title").css("visibility", "visible").stop().animate({
								opacity : 1
							}, hide_speed);
						}
						
						//타이핑이 끝난후 이미지를 보여준다.
						$(".question_title").find(".contents_img_box").css({
							"visibility" : "visible"
						}).stop().animate({
							opacity : 1
						}, hide_speed);
					});
				}
			});
		}
		
		
		get_item_info("/custom/renewal/landing/send/answer", func, {"answer_id":answer_id}, $('.ace'));
	}
	
	$(document).on("click", ".target_answer", function() {
		var answer_id = $(this).attr("data-value");
		
		if ($(".question_title").find(".contents_img_box").length == 0) {
			if (!$(".answer_title").is(":animated") && $(".answer_title").css("opacity") == 1) {
				var deco_round = $(this).parent().find(".deco_round");
				
				if (deco_round.css("background-color") == "#ff5400" || deco_round.css("background-color") == "rgb(255, 84, 0)") {
					next_ajax(answer_id);
				} else if (!deco_round.is(":animated") && deco_round.css("background-color") != "#ff5400" && deco_round.css("background-color") != "rgb(255, 84, 0)") {
					deco_round.stop().animate({
						width:"7px",
						height:"7px",
						backgroundColor:"#ff5400"
					}, function() {
						next_ajax(answer_id);
					});
				}
			}
		} else {
			next_ajax(answer_id);
		}
	});
});
</script>
<div class="landing_landing_container">
	<div class="landing_landing_section">
	    <video style="width:600px; margin-top:-50px;" loop="loop" id="hergry_intro" muted="muted" autoplay="autoplay" playsinline>
			<source src="/video/landing.mp4" type="video/mp4">
			<source src="/video/landing.mp4" type="video/mp4">
			Your browser does not support the video tag.
		</video>
		<p>
			Google translator is a real nobel prize winner
		</p>
	</div>
</div>
<div class="question_contianer">
	<div class="question_section">
		<div class="contact_clear">
			<div class="clear_title">
				Congratulations!
			</div>
			<div class="clear_content">
				휴,이제 모든 것이 끝났어요.<br>
				당신이 원하는 것과 디자인적 취향도 알게되었군요.<br>
				이제 당신의 선택만이 남았습니다.<br>
				우리와 함께 하길 원한다면 아래의 버튼을 눌러 우리의<br> 
				최첨단 인공지능 비서 시스템 실히를 통해 스케줄을 잡아주세요.<br>
				오늘 미팅 즐거웠습니다. 조심히 가세요.
			</div>
			<div class="clear_btn">
				<a class="go_contact_btn">
					OK
				</a>
			</div>
		</div>
		<div class="question_section_box">
			<div class="question_title">
			</div>
			<div class="answer_title">
				<p class="deco_round_section">
					<span class="deco_round first_question_deco"></span>
				</p>
				<p style=" position:relative; display: inline-block;">
					아닌데요?<br>
					저는 <input type="text" placeholder="팀쿡" class="op_input" id="custom_name" value="${user_name}">이고, 
					<input id="custom_company" class="op_input" type="text" placeholder="애플" value="${user_company}">에서 왔습니다.
					<img class="arrow" src="/resources/img/renewal/landing/arrow.png">
					<a class="submit_info">Next</a>
				</p>
				<div style="text-align: center;">
					<a class="submit_info mobile_next_btn">Next</a>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="message_box">
	<span><img style="width:11px;" src="/resources/img/renewal/landing/import.png"></span>
	<span>
		This website uses cookies to ensure you get the best experience.
	</span>
</div>
