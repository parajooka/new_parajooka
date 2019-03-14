<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 해당페이지 스타일시트 로딩 -->
<link rel="stylesheet" type="text/css" href="/resources/common/css/animate.css" />
<link rel="stylesheet" type="text/css" href="/resources/css/custom/2018/landing/mobile/main.css" />
<!-- 해당페이지 전용 js -->
<script>
</script>
<script src="/resources/common/js/plugin/t.min.js"></script>
<script src="/resources/common/js/jquery.sticky.js"></script>
<script src="/resources/common/js/jquery-sequencer.js"></script>
<script src="/resources/common/js/plugin/jquery.fittext.js"></script>
<script src="/resources/common/js/plugin/jquery.lettering.js"></script>
<script src="/resources/common/js/plugin/jquery.textillate.js"></script>
<script src="/resources/common/js/jquery-animate-css-rotate-scale.js"></script>
<script src="/resources/js/custom/2018/landing/mobile/main.js"></script>

<div class="landing_main">
<img class="money-back-img" alt="money_rain" src="" style="display: none;">
<div class="layout1">
	<div class="jobs-layout full-screen">
			<img class="steven-jobs" alt="steven" src="/resources/img/landing/01.png">
			<img class="logo2" alt="white_logo" src= "/resources/img/landing/mobile_logo2.png">
	</div>
</div>

<div class="layout2">
	<div class="dollar-layout full-screen">
		<div class="dollar-img-section">
			<img class="dollar-img" src="/resources/img/landing/menu/01.png">
		</div>
		<div class="dollar-title-section">
			<p></p>
		</div>
		<div class="dollar-content-section">
			<p>
				정말로 끝내주는 웹사이트를 만들게 되면 소비자는 지갑을 열게 됩니다.
				단순한 디자인이 아름답거나, 기술적으로 훌륭하다고해서 되는게 아니거든요.
				온라인세상은 소비자와의 심리싸움입니다.
			</p>
		</div>
	</div>
</div>

<div class="layout3">
	<div class="egg-layout full-screen">
		<div class="egg-img-section">
			<img class="egg-img" src="/resources/img/landing/MrParajooka.gif">
		</div>
		<div class="egg-title-section">
			<p></p>
		</div>
		<div class="egg-content-section">
			<p>
				당신의 높은 수준을 맞춰줄 에이전시를 찾고있겠죠.
				장담컨데 더 찾아봤자 웨이스트포타임입니다.
				명심하세요. 우리는 당신이 늘 생각하는 것 이상의 작업물을 내놓습니다.
			</p>
		</div>
	</div>
</div>
<div class="layout4">
	<div class="human-layout full-screen">
		<div class="human-text-section">
			<div class="human-text-box">
				<p class="human-title">
					지금보고 계신 웹이 별로 마음에<br>
					안 든다면 큰일입니다.<br>
					당신이 뒤쳐져있다는 신호입니다.
				</p>
				<p class="human-content">
					설명하자면 꽤나 철학적인 이야기를 해야해요.. 하지만 써봤자 당신은 읽지 않을테고 또
					얼마나 비싸게 받으려고 이렇게 골아픈 소리를 하는거지?하며 지레겁먹으실 수도 있으니
					어린이가 어른을 이해못하듯, 당신이 모르는 뭔가 있다는 것 정도만 알아두세요.
				</p>
			</div>
		</div>
		<div class="human-img-section">
			<p>
				<img class="human-img" src="/resources/img/landing/BLACK2.png">
			</p>
		</div>
	</div>
</div>
<div style="position: relative; width: 100%;">
	<div class="mask-test" style="width: 100%; position: absolute; bottom: 0; z-index: 99;"></div>
</div>
<div class="yes_no">
	<div class="yes_no_cell">
		<p>자, 이제 선택의 시간이 왔습니다.</p>
		<p>저와 함께 하시겠습니까?</p>
	</div>
</div>
<div class="yes_no_btn_section">
	<div class="yes_no_btn">
		<a href="/custom/menu/index?menu_idx=${about_idx}">YES</a>
		<a href="/custom/2018/landing/no"><span style="display: inline-block; position: absolute; width: 1vw; height: 1vw; background: #d87646; border-radius: 100%; left: 3vw; top: 2.7vw;"></span>NO</a>
	</div>
</div>
</div>
<p class="scroll_down" style="position: fixed; z-index: 9998; bottom: 0; margin-bottom: 10px; width: 100%; text-align: center;"><img src="/resources/common/img/scroll_down.png" style="width: 50px;"></p>
<script>
function move_scroll_down() {
	$(".scroll_down").stop().animate({
		marginBottom : "30px"
	}, 500, function() {
		$(".scroll_down").stop().animate({
			marginBottom : "10px"
		}, 500, function() {
			move_scroll_down();
		});
	});
}

move_scroll_down();

$('.layout1').sequencer({
    count: 12,
    remainder : 500,
    else_func : function() {
    	if (img_format($(".steven-jobs")) != "gif" && up) {
    		$(".steven-jobs").attr('src', '/resources/img/landing/main.gif');
    	}
    	
    	if (img_format($(".money-back-img")) != "gif" && up) {
    		if (money_timer) clearInterval(money_timer);
    		money_timer = setTimeout(function (){$(".money-back-img").fadeOut(500);}, 2500);
    		$(".money-back-img").attr("src", "/resources/img/landing/rain.gif").show();
    		$('.logo2').show();
    	} else if (!up && img_format($(".money-back-img")) == "gif") {
    		$(".money-back-img").attr("src", "").hide();
    		$('.logo2').hide();
    	}
    },
    func : function() {
    	$('.steven-jobs').attr("src", paths[index]);
    	if (index > 0) {
    		$(".scroll_down").hide();
    		if (!mask_checked) {
    			$(".last_message").stop().animate({
    				marginBottom : "10vw",
    				opacity : "0"
    			}, 500, function() {
    				$(".landing-mask").remove();
    			});
    			
    			mask_checked = true;
    		}
    	} else {
    		$(".scroll_down").show();
    	}
    		
    }
});

$('.mask-test').sequencer({
	count: b_color_array.length,
    remainder : 100,
    else_func : function() {
    	$('.mask-test').css({
			"background-color" : b_color_array[b_color_array.length - 1],
			"opacity" : b_op_array[b_op_array.length - 1]
		});
    	if (!show_check4) {
    		$("#logo_img").attr("src", "/resources/common/img/renewal/logo.png");
    		$('.yes_no').stop().animate({
    			opacity : 1
    		}, 500);
    		
    		$('.yes_no_btn_section').stop().animate({
    			bottom : 0,
    			opacity : 1
    		}, 800);
    		
    		show_check4 = true;
    	}
    	
    },
	func : function() {
		$('.mask-test').css({
			"background-color" : b_color_array[index],
			"opacity" : b_op_array[index]
		});
		
		if (!up && show_check4) {
			show_check4 = false;
			
			$('.yes_no').stop().animate({
    			opacity : 0
    		}, 500);
    		
    		$('.yes_no_btn_section').stop().animate({
    			bottom : "-100px",
    			opacity : 0
    		}, 800);
			
		}
		
	}
});

$('.layout4').sequencer({
    count: human_img_position.length,
    remainder : $(window).height() * 2,
    else_func : function() {
    	$('.human-img').css('left', human_img_position[human_img_position.length - 1]);
    	if (!show_check3) {
			$('.human-text-section').stop().animate({
				opacity : 1
			}, 800);

			show_check3 = true;
		}
    },
    func : function() {
   		$('.human-img').css('left', human_img_position[index]);
   		
   		$('.mask-test').css({
   			"opacity" : 0
   		});
   		
   		if (show_check3) {
			$('.human-text-section').stop().animate({opacity : 0}, 300);
   			show_check3 = false;
    	} else if (index == human_img_position.length - 1 && up) {
    		if (!show_check3) {
    			$('.human-text-section').stop().animate({
    				opacity : 1
    			}, 800);
    			show_check3 = true;
    		}
    	}
    }
});

$('.layout2').sequencer({
    count: dollar_title.length,
    remainder : 300,
    else_func : function() {
    	$('.dollar-title-section > p').html(dollar_title[dollar_title.length - 1]);
    	if (!show_check2) {
			$('.dollar-img-section > img').stop().animate({
				left : 0
			}, 800);
		}
    },
    func : function() {
    	$('.dollar-title-section > p').html(dollar_title[index]);
   		if (show_check2 && !up && index < dollar_title.length) {
			$('.dollar-content-section > p').stop().animate({
				left : '-150%',
				opacity : 0
			}, 800);
			if (show_time2) clearInterval(show_time2);
			show_time2 = setTimeout(function() {
   				$('.dollar-img-section > img').stop().animate({
       				left : '100%'
       			}, 800);
   			}, 300);
   			
   			show_check2 = false;
    	} else if (index >= dollar_title.length/3 && index < dollar_title.length && up) {
    		if (!show_check2) {
    			$('.dollar-img-section > img').stop().animate({
    				left : 0
    			}, 800);

				if (show_time2) clearInterval(show_time2);
				show_time2 = setTimeout(function() {
    				$('.dollar-content-section > p').stop().animate({
    					left : 0,
    					opacity : 1
    				}, 800);
    			}, 300);
    			
    			show_check2 = true;
    		}
    	}
    }
});

$('.layout3').sequencer({
    count: egg_title.length,
    remainder : 300,
    else_func : function() {
    	$('.egg-title-section > p').html(egg_title[egg_title.length - 1]);
    	if (!show_check) {
			$('.egg-img-section > img').stop().animate({
				top : 0
			}, 800);
		}
    },
    func : function() {
    	$('.egg-title-section > p').html(egg_title[index]);
   		if (show_check && !up && index < egg_title.length) {
			$('.egg-content-section > p').stop().animate({
				left : '-150%',
				opacity : 0
			}, 800);
			
			if (show_time) clearInterval(show_time);
			show_time = setTimeout(function() {
   				$('.egg-img-section > img').stop().animate({
       				top : '100%'
       			}, 800);
   			}, 300);
   			
   			show_check = false;
    	} else if (index >= egg_title.length/3 && index < egg_title.length && up) {
    		if (!show_check) {
    			$('.egg-img-section > img').stop().animate({
    				top : 0
    			}, 800);
    			
    			if (show_time) clearInterval(show_time);
    			show_time = setTimeout(function() {
    				$('.egg-content-section > p').stop().animate({
    					left : 0,
    					opacity : 1
    				}, 800);
    			}, 300);
    			
    			show_check = true;
    		}
    	}
    }
});
</script>