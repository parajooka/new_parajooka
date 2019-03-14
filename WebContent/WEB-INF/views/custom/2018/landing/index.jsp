<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- 해당페이지 스타일시트 로딩 -->
<link rel="stylesheet" type="text/css" href="/resources/css/custom/2018/landing/main.css" />
<link rel="stylesheet" type="text/css" href="/resources/common/css/animate.css" />
<!-- <link rel="stylesheet" type="text/css" href="/resources/common/css/style.css" /> -->
<!-- 해당페이지 전용 js -->
<script src="/resources/common/js/jquery.sticky.js"></script>
<script src="/resources/common/js/jquery-sequencer.js"></script>
<script src="/resources/common/js/plugin/jquery.fittext.js"></script>
<script src="/resources/common/js/plugin/jquery.lettering.js"></script>
<script src="/resources/common/js/plugin/jquery.textillate.js"></script>
<script src="/resources/common/js/jquery-animate-css-rotate-scale.js"></script>
<script src="/resources/js/custom/2018/landing/main.js"></script>
<img class="fixed_logo" style="position: fixed; top : 50%; left : 50%; width: 30vw;  z-index: 99999; margin-left:-15vw; margin-top:-3vw; display: none;" src="/resources/common/img/logo.png">
<%-- <spring:message code="jb.common.title"/>  --%>
<div class="bottom-section">
	<img class="money-back-img" alt="steven" src="">
	<!-- 위에서 돈이 떨어지는 레이아웃 -->
	<div class="steven-section">
		<div class="img-zone">
			<img class="steven-jobs" alt="steven" src="/resources/img/landing/01.png">
			<img class="logo2" alt="white_logo" src= "/resources/img/landing/logo2.png">
		</div>
	</div>
	<div class="money-section">
		<div class="money-mask"></div>
		<div class="money-back">
			 <img class="money-img" alt="dollar" src="/resources/img/landing/menu/01.png">
			<div class="money-content">
				<p class="money-title">
					우리는 끝내주는 웹을 만들고,<br>
					당신은 돈을 챙기면 됩니다.
				</p>
				<p class="money-view">
					정말로 끝내주는 웹사이트를 만들게 되면 소비자는 지갑을 열게 됩니다.<br>
					단순한 디자인이 아름답거나, 기술적으로 훌륭하다고해서 되는게 아니거든요.<br>
					온라인세상은 소비자와의 심리싸움입니다.
				</p>
			</div>
		</div>
	</div>


	<!-- 밑에서 파라앤주카가 올라오는 영역 -->
	<div class="info-section">
		<div class="info-container">
			<img class="MrParajooka" alt="MrParaJooka" src="/resources/img/landing/MrParajooka.gif">
			<div class="info-box">
				<p class="info-title">
					당신의 기업이 대기업이거나<br>
					소기업이나 아이돈케얼입니다.
				</p>
				<p class="info-view">
					당신의 높은 수준을 맞춰줄 에이전시를 찾고있겠죠.<br>
					장담컨데 더 찾아봤자 웨이스트포타임입니다.<br>
					명심하세요. 우리는 당신이 늘 생각하는 것 이상의 작업물을 내놓습니다.
				</p>
			</div>
		</div>
	</div>


	<!-- 인류가 움직이는 레이아웃 -->
	<div class="human-section">
		<div class="human-container">
			<img class="human-img" alt="human" src="/resources/img/landing/BLACK.png">
			<div class="human-box">
				<div class="human-sh">
					<p class="human-title">
						지금보고 계신 웹이 별로 마음에<br>안 든다면 큰일입니다.<br>당신이 뒤쳐져있다는 신호입니다.
					</p>
					<p class="human-view">
						설명하자면 꽤나 철학적인 이야기를 해야해요.. 하지만 써봤자 당신은 읽지 않을테고 또<br>
						얼마나 비싸게 받으려고 이렇게 골아픈 소리를 하는거지?하며 지레겁먹으실 수도 있으니<br>
						어린이가 어른을 이해못하듯, 당신이 모르는 뭔가 있다는 것 정도만 알아두세요.
					</p>
				</div>
			</div>
		</div>
	</div>
	<div class="choice-section">
		<div class="choice-container">
			<div class="choice-box">
				<p>
					자, 이제 선택의 시간이 왔습니다.<br> 저와 함께 하시겠습니까?
				</p>
			</div>
			<div class="YesNo">
				<p style="text-align: center;">
					<a href="/custom/menu/index?menu_idx=${about_idx}"><img class="yes" alt="steven"
						src="/resources/img/landing/yes.png"></a> 
					<a href="/custom/2018/landing/no"><img
						class="no" alt="steven" src="/resources/img/landing/no.png"></a>
				</p>
			</div>
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

$('.steven-section').sequencer({
    count: 12,
    remainder : 100,
    else_func : function() {
    	if (img_format($(".steven-jobs")) != "gif" && up) {
    		$(".steven-jobs").attr('src', '/resources/img/landing/main.gif');
    	}
    	
    	if (img_format($(".money-back-img")) != "gif" && up) {
    		if (timer) clearInterval(timer);
			timer = setTimeout(function (){$(".money-back-img").fadeOut(500);}, 2500);
    		$(".money-back-img").css("height", "105%").attr("src", "/resources/img/landing/rain.gif").show();
    	} else if (!up && img_format($(".money-back-img")) == "gif") {
    		$(".money-back-img").attr("src", "").hide();
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

var human_img = $('.human-img');

max_left = parseInt('-' + human_img.width()/100 * 75);

for (var i = 0; i >= max_left; i--) {
	human_img_position.push(i + '%');
}

$(".human-section").sequencer({
    count: human_img_position.length,
    remainder : 500,
    else_func : function() {
    	$(".human-img").css("left", human_img_position[human_img_position.length - 1]);
    	
    	if (!human_check && up) {
    		human_check = true;
    		$(".human-box").stop().animate({
    			opacity : 1
    		}, 500);
    	} else if (human_check && !up) {
    		human_check = false;
    		$(".human-box").stop().animate({
    			opacity : 0
    		}, 500);
    	}
    	
    },
    func : function() {
    	$(".human-img").css("left", human_img_position[index]);
    }
});
</script>