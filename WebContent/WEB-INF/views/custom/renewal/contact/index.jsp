<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<style>
	.container {margin-top:0;}
	.section {position: relative;}
	
	.contact_containter {width:70%; height: 100vh; display: table;}
	.contact_section {width:100%; height: 100%; display: table-cell; vertical-align: middle;}
	
	#calendar {width:100%; border-spacing: 0; border: 1px solid #a5a5a5; padding-bottom:3vh;}
	#calendar tr td{text-align: center; position: relative; width: 117px; height: 8vh;}
	#calendar tr:first-child td, #calendar tr:nth-child(2) td {width: auto; height: auto;}
	
	#calendar tr:first-child td {padding-bottom:6vh; padding-top:5vh; font-size:23px;}
	#calendar tr:nth-child(2) td {border-bottom: 1px solid #a5a5a5;}
	
	
	.today_round {position: absolute; display: inline-block; width: 8vh; height: 8vh; top: 0; left: 50%; z-index: 0; border-radius: 100%; margin-left: -4vh;}
	.today_square {position: absolute; display: inline-block; width: 8vh; height: 8vh; top: 0; left: 50%; z-index: 0; margin-left: -4vh; background-size: 60% !important; background-repeat: no-repeat !important; background-position: center !important;}
	.today_number {position: relative; z-index: 3; cursor: pointer;}
	
	.move_calendar {cursor: pointer; font-size:14px;}
	.contact_chart {width: 22%; padding:0 2%; border: 1px solid #a5a5a5; margin-top: 5vh; position: absolute; right:0; top:50%;}
	
	.chart_title {text-align: center; padding: 5%; font-size: 2.5vh;}
	.chart_hello {padding: 1vh 0; font-size: 1.3vh;}
	
	.injection_section {font-size:1.3vh;}
	#reservation_memo {width: 98%; background: none; font-size: 1.3vh; font-family: "Gothic NeoUL" !important; line-height: 2.2vh; resize:none; height: 24vh;}
	#reservation_pw, #edit_pw {width: 100%; font-size: 1.3vh; background: none; box-shadow: none; border: none; border-bottom: 1px solid #888;}
	.injection_submit_btn, .check_pw_btn {display: inline-block; text-align: center; width: 100%; padding: 1vh 0; color: #d87646; border: 1px solid #888; font-weight: bold; opacity: 0.5;}
	.contact_section_title {padding:1vh 0;}
	
	.trash_icon {width: 18px; display: inline-block; position: absolute; top: 15px; right: 15px; cursor:pointer;}
	.trash_icon img {width:100%;}
	
	.edit_contact_pw {height: 47vh;}
	
	.date_click_btn {position: absolute; top: 0; left: 0; width: 100%; height: 100%; z-index: 20; cursor: pointer;}
	
	select {
		-webkit-appearance: none;
	    -moz-appearance: none;
	    appearance: none;
	    background: url(/resources/common/img/select_arrow.png) no-repeat 98% 50%;
	    width: 100%;
	    padding: 0.5vh 1vh;
	    border: 1px solid #888888;
	    box-shadow: inherit;
	    outline: none;
	    font-family: 'Gothic NeoUL' !important;
	} 
	
	/* IE 10, 11의 네이티브 화살표 숨기기 */
	select::-ms-expand {
		display: none;
	}
	
	input, textarea {outline: none;}
	
	input::placeholder {
		color :#bbbab6;
	}
	
	@media (max-width:1200px) {
		#calendar tr:nth-child(2) td {font-size:3vw;}
		.chart_title {font-size:5vw; padding: 5vw 0 5vw;}
		.chart_hello, .injection_section, #reservation_memo, #reservation_pw, #edit_pw {font-size:3.5vw; padding:0;}
	
		.container {margin-top:120px;}
		.contact_containter {width:100%; height: auto; margin-bottom:15vw;}
		
		#calendar {font-size:4vw;}
		#calendar tr td {width:auto; height: 14vw;}
		#calendar tr:first-child td {padding:8vw 0;}
		.today_round {width:8vw; height: 8vw; top: 50%; margin-top:-4vw; margin-left:-4vw;}
		.today_square {width:8vw; height: 8vw; top: 50%; margin-top:-4vw; margin-left:-4vw;}
		.contact_chart {position: static; margin: 0 !important; width: 89%; padding: 0 5%; height: auto !important; margin-bottom:4vw !important; padding-bottom:5vw;}
		
		#reservation_memo {height:35vw; line-height:4.5vw;}
		.injection_submit_btn, .check_pw_btn {padding: 2vw 0;}
		
		.contact_section_title {padding: 2vw 0; margin-top: 3vw; font-size: 3.5vw;}
		.contact_pw {height: 20vw;}
		
		select {
			padding: 2vw 2vw;
		}
	}
</style>
<script type="text/javascript">
	var monthNames = [ "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" ];

	var fixed_today = new Date('${today}');
	var today = new Date('${today}');//오늘 날짜//내 컴퓨터 로컬을 기준으로 today에 Date 객체를 넣어줌
	var date = new Date('${today}');//today의 Date를 세어주는 역할
	
	function prevCalendar() {//이전 달
		$(".next_calendar").css("visibility", "visible");
	// 이전 달을 today에 값을 저장하고 달력에 today를 넣어줌
	//today.getFullYear() 현재 년도//today.getMonth() 월  //today.getDate() 일 
	//getMonth()는 현재 달을 받아 오므로 이전달을 출력하려면 -1을 해줘야함
		if (new Date(today.getFullYear(), today.getMonth() - 1, today.getDate()).getTime() < new Date(fixed_today.getFullYear(), fixed_today.getMonth(), fixed_today.getDate()).getTime()) {
			alert("현재달의 이전으로는 예약이 불가능합니다.");
			return
		}  else if (new Date(today.getFullYear(), today.getMonth() - 1, today.getDate()).getTime() == new Date(fixed_today.getFullYear(), fixed_today.getMonth(), fixed_today.getDate()).getTime()) {
			$(".before_calendar").css("visibility", "hidden");
		} else {
			$(".before_calendar").css("visibility", "visible");
		}
		
		date = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate());
		
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
			
			var obj = data['object'];
			
			today = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
			buildCalendar(function() {
				if (obj != null) {
					for (var i = 0; i < obj.length; i++) {
						var meeting = obj[i];
						var reserv_date = meeting['reservation_date'];
						console.log(reserv_date.split(" ")[0].split("-")[2]);
						initCalendar(reserv_date.split(" ")[0].split("-")[2]);
					}
				}
		    }); //달력 cell 만들어 출력 
		}
		
		get_item_info("/custom/renewal/contact/moveCalendar", func, {"moveDate":date.getFullYear() +"-"+ pad((date.getMonth() + 1), 2) + "-" + date.getDate()}, $('body'));
	}

	function nextCalendar() {//다음 달
		$(".before_calendar").css("visibility", "visible");
		// 다음 달을 today에 값을 저장하고 달력에 today 넣어줌
		//today.getFullYear() 현재 년도//today.getMonth() 월  //today.getDate() 일 
		//getMonth()는 현재 달을 받아 오므로 다음달을 출력하려면 +1을 해줘야함
		if (new Date(today.getFullYear(), today.getMonth() + 1, today.getDate()).getTime() > new Date(fixed_today.getFullYear(), fixed_today.getMonth() + 2, fixed_today.getDate()).getTime()) {
			alert("현재달의 기준 최대 2달 이후까지만 예약이 가능합니다.");
			return;
		}  else if (new Date(today.getFullYear(), today.getMonth() + 1, today.getDate()).getTime() == new Date(fixed_today.getFullYear(), fixed_today.getMonth() + 2, fixed_today.getDate()).getTime()) {
			$(".next_calendar").css("visibility", "hidden");
		} else {
			$(".next_calendar").css("visibility", "visible");
		}

		date = new Date(date.getFullYear(), date.getMonth() + 1, date.getDate());
		
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
			
			var obj = data['object'];
			
			today = new Date(today.getFullYear(), today.getMonth() + 1, today.getDate());
			
			buildCalendar(function() {
				if (obj != null) {
					for (var i = 0; i < obj.length; i++) {
						var meeting = obj[i];
						var reserv_date = meeting['reservation_date'];
						initCalendar(reserv_date.split(" ")[0].split("-")[2]);
					}
				}
		    }); //달력 cell 만들어 출력 
		}
		
		get_item_info("/custom/renewal/contact/moveCalendar", func, {"moveDate":date.getFullYear() +"-"+ pad((date.getMonth() + 1), 2) + "-" + date.getDate()}, $('body'));
	}
	
	function buildCalendar(callback) {//현재 달 달력 만들기
		var doMonth = new Date(today.getFullYear(), today.getMonth(), 1);
		//이번 달의 첫째 날,
		//new를 쓰는 이유 : new를 쓰면 이번달의 로컬 월을 정확하게 받아온다.     
		//new를 쓰지 않았을때 이번달을 받아오려면 +1을 해줘야한다. 
		//왜냐면 getMonth()는 0~11을 반환하기 때문
		var lastDate = new Date(today.getFullYear(), today.getMonth() + 1, 0);
		//이번 달의 마지막 날
		//new를 써주면 정확한 월을 가져옴, getMonth()+1을 해주면 다음달로 넘어가는데
		//day를 1부터 시작하는게 아니라 0부터 시작하기 때문에 
		//대로 된 다음달 시작일(1일)은 못가져오고 1 전인 0, 즉 전달 마지막일 을 가져오게 된다
		var tbCalendar = document.getElementById("calendar");
		//날짜를 찍을 테이블 변수 만듬, 일 까지 다 찍힘
		var tbCalendarYM = document.getElementById("tbCalendarYM");
		//테이블에 정확한 날짜 찍는 변수
		//innerHTML : js 언어를 HTML의 권장 표준 언어로 바꾼다
		//new를 찍지 않아서 month는 +1을 더해줘야 한다. 
		tbCalendarYM.innerHTML = (monthNames[today.getMonth()]) + ", "
				+ today.getFullYear();

		/*while은 이번달이 끝나면 다음달로 넘겨주는 역할*/
		while (tbCalendar.rows.length > 2) {
			//열을 지워줌
			//기본 열 크기는 body 부분에서 2로 고정되어 있다.
			tbCalendar.deleteRow(tbCalendar.rows.length - 1);
			//테이블의 tr 갯수 만큼의 열 묶음은 -1칸 해줘야지 
			//30일 이후로 담을달에 순서대로 열이 계속 이어진다.
		}
		
		var total_td_count = $("#calendar").find("td").length + 6 * 7;

		var row = null;
		row = tbCalendar.insertRow();
		
		var first_row_padding = 1;
		$(row).addClass("first_row");
		
		//테이블에 새로운 열 삽입//즉, 초기화
		var cnt = 0;// count, 셀의 갯수를 세어주는 역할
		// 1일이 시작되는 칸을 맞추어 줌
		for (i = 0; i < doMonth.getDay(); i++) {
			/*이번달의 day만큼 돌림*/
			cell = row.insertCell();//열 한칸한칸 계속 만들어주는 역할
			cnt = cnt + 1;//열의 갯수를 계속 다음으로 위치하게 해주는 역할
		}
		
		/*달력 출력*/
		var while_date = 1;
		
		while ($("#calendar").find("td").length < total_td_count) {
			if (while_date <= lastDate.getDate()) {
				//1일부터 마지막 일까지 돌림
				cell = row.insertCell();//열 한칸한칸 계속 만들어주는 역할
				$(cell).addClass("calendar_"+ pad(while_date, 2));
				cell.innerHTML = "<a class='today_number' data-date='"+ today.getFullYear() + "-" + (pad(today.getMonth() + 1, 2)) + "-" + pad(while_date,2) +"'>" + while_date + "</a><span class='today_round'></span><span class='today_square'></span><a class='date_click_btn'></a>";
				cnt = cnt + 1;//열의 갯수를 계속 다음으로 위치하게 해주는 역할
				if (cnt % 7 == 1) {/*일요일 계산*/
					//1주일이 7일 이므로 일요일 구하기
					//월화수목금토일을 7로 나눴을때 나머지가 1이면 cnt가 1번째에 위치함을 의미한다
					cell.innerHTML = "<a class='today_number' data-date='"+ today.getFullYear() + "-" + (pad(today.getMonth() + 1, 2)) + "-" + pad(while_date,2) +"'>" + while_date + "</a><span class='today_round'></span><span class='today_square'></span><a class='date_click_btn'></a>";
					//1번째의 cell에만 색칠
				}
	
				if (cnt % 7 == 0) {/* 1주일이 7일 이므로 토요일 구하기*/
					//월화수목금토일을 7로 나눴을때 나머지가 0이면 cnt가 7번째에 위치함을 의미한다
					cell.innerHTML = "<a class='today_number' data-date='"+ today.getFullYear() + "-" + (pad(today.getMonth() + 1, 2)) + "-" + pad(while_date,2) +"'>" + while_date + "</a><span class='today_round'></span><span class='today_square'></span><a class='date_click_btn'></a>";
					//7번째의 cell에만 색칠
					row = calendar.insertRow();
					//토요일 다음에 올 셀을 추가
				}
	
				/*오늘의 날짜에 노란색 칠하기*/
				
				/* if (today.getFullYear() == date.getFullYear()
						&& today.getMonth() == date.getMonth()
						&& while_date == date.getDate()) {
					$(cell).html(
							"<span class='today_number'>" + $(cell).html()
									+ "</span>").css("color", "white").append(
							"<span class='today'></span>");
				} */
			} else {
				//1일부터 마지막 일까지 돌림
				cell = row.insertCell();//열 한칸한칸 계속 만들어주는 역할
				cnt = cnt + 1;//열의 갯수를 계속 다음으로 위치하게 해주는 역할
	
				if (cnt % 7 == 0) {/* 1주일이 7일 이므로 토요일 구하기*/
					//월화수목금토일을 7로 나눴을때 나머지가 0이면 cnt가 7번째에 위치함을 의미한다
					//7번째의 cell에만 색칠
					row = calendar.insertRow();
					//토요일 다음에 올 셀을 추가
				}
			}
			
			while_date ++;
		}
		

		
		$.each($("#calendar").find(".first_row").find("td"), function(index, value) {
			if ($(window).width() > 1200) {
				$(value).css("padding-top", "10px");
				$(value).find(".today_round").css("margin-top", "10px");
			}
		});
		
		
		if (typeof callback === "function") {
			callback();
		}
	}
	
	var init_array = [];
	
	function initCalendar(date) {
		$(document.getElementsByClassName("calendar_"+ date)).attr("data-valid", "true").find(".today_square").css("background", "url('/resources/img/renewal/contact/contact_x.png')");
		$(document.getElementsByClassName("calendar_"+ date)).find(".today_round").css("border", "none");
	}
	
	function clearCalendar(date) {
		$(document.getElementsByClassName("calendar_"+ date)).removeAttr("data-valid").find(".today_square").css("background", "none");
	}
	
$(document).ready(function() {
    buildCalendar(function() {
    	$(".contact_chart").height($("#calendar").height() + parseInt($("#calendar").css("padding-bottom"))).css("margin-top", "-"+ ($(".contact_chart").height()/2 + 2) +"px");
    	<c:forEach items="${contact_list}" var="contact">
    		initCalendar('${contact.reservation_date.split(" ")[0].split("-")[2]}');
	    </c:forEach>
    });
    
    $('input[type="text"]').keydown(function() {
        if (event.keyCode === 13) {
            event.preventDefault();
        }
    });
    
    var round;
    
    $(document).on("mouseover", ".today_number", function() {
    	var valid = $(this).parent().attr("data-valid");
    	if (typeof valid == "undefined" || valid == null || valid.length == 0 || valid != "true") {
    		round = $(this).parent().find(".today_round");
        	round.css("border", "1px solid #d87646");
    	}
    });
    
    $(document).on("mouseleave", ".today_number", function() {
    	var valid = $(this).parent().attr("data-valid");
    	if (typeof valid == "undefined" || valid == null || valid.length == 0 || valid != "true") {
	    	round = $(this).parent().find(".today_round");
	    	round.css("border", "none");
    	}
    });
    
    $(document).on("mouseover", ".date_click_btn", function() {
    	var valid = $(this).parent().attr("data-valid");
    	if (typeof valid == "undefined" || valid == null || valid.length == 0 || valid != "true") {
    		round = $(this).parent().find(".today_round");
        	round.css("border", "1px solid #d87646");
    	}
  	});
    
    $(document).on("mouseleave", ".date_click_btn", function() {
    	var valid = $(this).parent().attr("data-valid");
    	if (typeof valid == "undefined" || valid == null || valid.length == 0 || valid != "true") {
	    	round = $(this).parent().find(".today_round");
	    	round.css("border", "none");
    	}
  	});
    
    var injection_layout = $(".injection_section").html();
    $(".injection_section").empty();
    var eidt_contact_chart = $(".eidt_contact_chart").html();
    $(".eidt_contact_chart").empty();
    
    
    var mode;
    var call_date;
    var orign_participant_id;
  	var orign_ment = $(".chart_hello").html();
  	
  	$(document).on("click", ".today_square", function() {
    	$(this).parent().find(".today_number").triggerHandler("click");
    });
  	

  	$(document).on("click", ".today_round", function() {
    	$(this).parent().find(".today_number").triggerHandler("click");
    });
  	
  	$(document).on("click", ".date_click_btn", function() {
  		$(this).parent().find(".today_number").trigger("click");
  	});
  	
    $(document).on("click", ".today_number", function() {
    	var self = $(this);
    	call_date = $(this).attr("data-date");
    	
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
			
			var obj = data['object'];
			
			$(document.getElementsByClassName("today_round")).css("background-color", "transparent");
			$(document.getElementsByClassName("today_number")).css("color", "black");
			
			//이날에 등록된 사용자가 없는경우
			if (obj == null) {
				clearCalendar(call_date.split("-")[2]);
				self.css("color", "white");
				self.parent().find(".today_round").css({
					"background-color" : "#d87646",
					"border" : "none"
				});
				
				$(".eidt_contact_chart").hide();
				$(".injection_section").empty().append(injection_layout).find("#reservation_date").val(call_date);
				$(".insert_contact_chart").find(".chart_hello").html(orign_ment);
				$(".insert_contact_chart").show();
				mode = "insert";
			} else {
				initCalendar(call_date.split("-")[2]);
				mode = "modify";
				
				orign_participant_id = obj['participant_id'];
				
				$(".insert_contact_chart").hide();
				$(".eidt_contact_chart").empty().append(eidt_contact_chart).find(".chart_hello").html(obj['name'] +"님 맞으신가요?<br>그렇다면 암호를 입력해주세요.");
				$(".eidt_contact_chart").show();
			}
		}
		
		get_item_info("/custom/renewal/contact/call", func, {"call_date":call_date}, $('body'));
    });
    
    $(document).on("keyup", "#reservation_pw", function() {
    	var self = $(this);
    	if (self.val().length >= 1) {
    		$(".insert_contact_chart").find(".injection_submit_btn").css({
    			"opacity" : 1,
    			"cursor" : "pointer"
    		});
    	} else {
    		$(".insert_contact_chart").find(".injection_submit_btn").css({
    			"opacity" : 0.5,
    			"cursor" : "default"
    		});
    	}
    });
    
    $(document).on("keyup", "#edit_pw", function() {
    	var self = $(this);
    	
    	if (self.val().length >= 1) {
    		$(".edit_section").find(".check_pw_btn").css({
    			"opacity" : 1,
    			"cursor" : "pointer"
    		});
    	} else {
    		$(".edit_section").find(".check_pw_btn").css({
    			"opacity" : 0.5,
    			"cursor" : "default"
    		});
    	}
    });
    
    $(document).on("focusout", "#reservation_pw", function() {
    	var self = $(this);
    	if (self.val().length >= 1) {
    		$(".insert_contact_chart").find(".injection_submit_btn").css({
    			"opacity" : 1,
    			"cursor" : "pointer"
    		});
    	} else {
    		$(".insert_contact_chart").find(".injection_submit_btn").css({
    			"opacity" : 0.5,
    			"cursor" : "default"
    		});
    	}
    });
    
    $(document).on("focusout", "#edit_pw", function() {
    	var self = $(this);
    	
    	if (self.val().length >= 1) {
    		$(".edit_section").find(".check_pw_btn").css({
    			"opacity" : 1,
    			"cursor" : "pointer"
    		});
    	} else {
    		$(".edit_section").find(".check_pw_btn").css({
    			"opacity" : 0.5,
    			"cursor" : "default"
    		});
    	}
    });
    
    $(document).on("click", ".injection_submit_btn", function() {
    	var frm = $(".injection_section");
    	if (mode == "modify") {
    		$(".injection_section").find("#reservation_date").val(call_date + " "+ $(".injection_section").find("#meeting_time").val());
    		
    		frm.attr("action", "/custom/renewal/contact/update");
    		form_submit(frm);
    	} else {
    		$(".injection_section").find("#reservation_date").val(call_date + " "+ $(".injection_section").find("#meeting_time").val());
    		
    		frm.attr("action", "/custom/renewal/contact/insert");
    		form_submit(frm);
    	}
    });
    
    $(document).on("click", ".check_pw_btn", function() {
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
			
			var obj = data['object'];
			
			if (obj == null) {
				alert("암호가 일치하지 않습니다.");
			} else {
				$(".eidt_contact_chart").hide();
				$(".injection_section").empty().append(injection_layout).find("#reservation_date").val(call_date);
				$(".insert_contact_chart").show();
				
				var form = $(".insert_contact_chart");
				var participant = obj['participant'];
				var target_contact = obj['contact'];
				
				form.find("#participant_id").val(participant['participant_id']);
				form.find(".chart_hello").html(participant['name'] + "님의 예약내용 수정페이지 입니다.");
				form.find(".contact_time").html("시간을 몇시로 변경 하시겠습니까?");
				form.find(".contact_memo").html("메모를 수정 하시겠습니까?");
				form.find("#meeting_time").val(target_contact['reservation_date'].split(" ")[1]);
				form.find("#reservation_type").val(target_contact['reservation_type']);
				form.find("#reservation_memo").val(target_contact['reservation_memo']);
				form.find(".chart_title").append("<a class='trash_icon' data-contact-id='"+ target_contact['contact_id'] +"'><img src='/resources/common/img/trash.png'></a>");
			}
		}
		
		get_item_info("/custom/renewal/contact/checkPW", func, {"participant_id":orign_participant_id, "password":$("#edit_pw").val()}, $('body'));
    });
    
    $(document).on("click", ".trash_icon", function() {
    	if (confirm("정말 예약을 취소하시겠습니까?\r\n취소 후에는 새로운 예약을 등록 할 수 있습니다.")) {
    		var contact_id = $(this).attr("data-contact-id");
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
    			
    		}
    		
       		get_item_info("/custom/renewal/contact/delete", func, {"contact_id":contact_id, "password":$(".insert_contact_chart").find("#reservation_pw").val()}, $('body'));
    	}
    });
    
    if ($(window).width() < 1200) {
    	$(".eidt_contact_chart").insertBefore(".contact_containter");
    	$(".insert_contact_chart").insertBefore(".contact_containter");
    }
});

$(window).on("resize", function() {
	$(".contact_chart").height($("#calendar").height() + parseInt($("#calendar").css("padding-bottom"))).css("margin-top", "-"+ ($(".contact_chart").height()/2 + 2) +"px");
	
	if ($(window).width() < 1200) {
		if ($(".eidt_contact_chart").index() != 2) {
	    	$(".eidt_contact_chart").insertBefore(".contact_containter");
	    	$(".insert_contact_chart").insertBefore(".contact_containter");
		}
    } else {
    	if ($(".eidt_contact_chart").index() != 4) {
        	$(".eidt_contact_chart").insertAfter(".contact_containter");
        	$(".insert_contact_chart").insertAfter(".contact_containter");
    	}
    }
	
	$.each($("#calendar").find(".first_row").find("td"), function(index, value) {
		if ($(window).width() > 1200) {
			$(value).css("padding-top", "10px");
			$(value).find(".today_round").css("margin-top", "10px");
		}
	});
});
</script>
<div class="contact_containter">
	<div class="contact_section">
		<table id="calendar">
		    <tr><!-- label은 마우스로 클릭을 편하게 해줌 -->
		        <td>
		        	<a class="move_calendar before_calendar" onclick="prevCalendar()" style="visibility: hidden;">Prev</a>
		        </td>
		        <td align="center" id="tbCalendarYM" colspan="5">
		        </td>
		        <td>
		        	<a class="move_calendar next_calendar" onclick="nextCalendar()">Next</a>
		       	</td>
		    </tr>
		    <tr>
		        <td align="center">Sun</td>
		        <td align="center">Mon</td>
		        <td align="center">Tue</td>
		        <td align="center">Wed</td>
		        <td align="center">Thu</td>
		        <td align="center">Fri</td>
		        <td align="center">Sat</td>
		    </tr> 
		</table>
	</div>
</div>
<div class="contact_chart eidt_contact_chart" style="display: none;">
	<div class="chart_title">
		Contact
	</div>
	<div class="chart_hello">
		${participant.name}님 맞으시간요?<br>
		그렇다면 암호를 입력 해주세요.
	</div>
	<form class="edit_section" method="post">
		<div class="contact_pw edit_contact_pw">
			<p class="contact_section_title">본인확인 암호</p>
			<input type="text" id="edit_pw" name="edit_pw" placeholder="암호를 입력해주세요.">
			<input type="text" style="display:none;">
		</div>
		<div class="submit_section" style="margin-top:3vh;">
			<a class="check_pw_btn">Edit</a>
		</div>
	</form>
</div>
<div class="contact_chart insert_contact_chart">
	<div class="chart_title">
		Contact
	</div>
	<div class="chart_hello">
		${participant.name}님 안녕하세요? 최첨단 A.I 실히라고 합니다.<br>
		우선 날짜를 선택해주세요.
	</div>
	<form class="injection_section" method="post">
		<input type="hidden" value="${participant.participant_id}" id="participant_id" name="participant_id">
		<input type="hidden" value="" id="reservation_date" name="reservation_date">
		<div class="choose_time">
			<p class="contact_section_title contact_time">그럼 이제 시간을 정해볼까요?</p>
			<select id="meeting_time">
				<c:forEach begin="10" end="19" varStatus="status">
					<fmt:formatNumber var="no" minIntegerDigits="2" value="${status.index}" type="number"/>
					<option value="${no}:00">${no}:00</option>
					<option value="${no}:30">${no}:30</option>
				</c:forEach>
			</select>
		</div>
		<div class="choose_type">
			<p class="contact_section_title contact_meeting">미팅은 어떤 방식이 편하세요?</p>
			<select id="reservation_type" name="reservation_type">
				<option value="0">방문</option>
				<option value="1">전화</option>
				<option value="2">메일</option>
			</select>
		</div>
		<div class="last_memo">
			<p class="contact_section_title contact_memo">${participant.name}님 마지막으로 더 하실 말씀 있으신가요?</p>
			<textarea id="reservation_memo" name="reservation_memo" placeholder="우리와 연락할 수단을 남겨주세요.&#13;&#10; ex)연락처, 메일 등"></textarea>
		</div>
		<div class="contact_pw">
			<p class="contact_section_title">본인확인 암호</p>
			<input type="text" id="reservation_pw" name="reservation_pw" placeholder="암호를 입력해주세요.">
			<input type="text" style="display:none;">
		</div>
		<div class="submit_section" style="margin-top:3vh;">
			<a class="injection_submit_btn">Confirm</a>
		</div>
	</form>
</div>