<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
html, body {overflow: hidden;}
html, body, .container, .section {height: 100% !important;}
.container {width:100%; margin:0;}
</style>
<script>
$(document).ready(function() {
	$('.menubar').remove();
	function play_ment() {
		setTimeout(function() {
			var text = 'NOOOOOOOOOOO NO GOD PLEASE ';
			
			$('.wave').html($('.wave').html() + text);
			
			var height = $('.wave').height();
			//$('.wave').css('margin-top', -(height/2));
			
			var move_text = function() {
				var left = parseInt($('.wave').css('left').replace('px', ''));
				$('.wave').css('left', left - 2);
			} 
			
			$(".fuck_you").stop().animate({
				bottom:"16vh"
			}, 13000);
			
			setInterval(function() {
				var left = parseInt($('.wave').css('left').replace('px', ''));
				
				if (left > -$('.wave').width()) {
					move_text();
				} else {
					$('.wave').css('left', '100%');
					move_text();
				}
			}, 5);
		}, 1000);
	}
	
	var text = "휴.. 조금 전에 보셨던 디자인을 만들고서 되게 뿌듯해 했는데.. 우리가 마음에 안 드시나보군요.："+
				"이렇게 된 마당에 마지막으로 한 페이지만 더 말하겠습니다. 만약 우리의 사이트를 보고 별로라고 느낀다면, 자신있게 말하건데,："+
				"(꽤나 단호한 표정으로)："+
				"당신이 틀린 거에요. 그럼 누군가 이러겠죠. ！아니！ 이런 건 틀린게 아니라 ！다른 거！라고 해야지~："+
				"어떻게 이런 개인의 취향이 요구되는 부분에서 맞고 틀린 게 있겠어?！："+
				"풉 개인의 취향이라는 건 겁쟁이들이 쓰는 말입니다! 자신의 허접함을 포장하는 말이죠..："+
				"음원차트 13299위의 가수가 어깨를 으쓱하며, 대중이 자신을 이해못한다고："+
				"이야기하는 꼴이죠. 만약 그 가수가 CM송을 부르는 비상업가수라면 옳습니다. 하지만 상업을 추구하는 상업가수라면 그래선 안되죠.："+
				"："+
				"또 이런 경우도 있습니다.："+
				"！특별함！을 ！멍청함！으로 이해하고 있는 사람들 말입니다.："+
				"이게 무슨 말이냐면 다들 두발로 걸어다니고 있으니까, 남들과 차별화하기 위해 네발로 걸어다니는 것이죠.："+
				"분명 남들이랑 다르긴 하겠지만..저건 뭐랄까...어....그냥 병신이잖아요... ："+
				"우리는 디자인에 목숨걸지도 않고, 기술에 목숨걸지도 않습니다.："+
				"자 이걸보세요 뭐가 보이시나요? 두 사람이 얼굴을 맞대고 있는 모습이요?："+
				"(잡아 먹을 듯이 노려보며)："+
				"아뇨? 제 눈엔 모래시계가 보이는 걸요? 이걸 어쩌죠? 네?："+
				"흥분했군요. 사과드리겠습니다. 제가 말씀드리고자 한 것은 보고싶은 것만 본다는 거죠.："+
				"기술과 디자인이 필요한 것은 소비자의 지갑을 열기 위해서입니다.："+
				"하지만 기술자들과 디자이너는 각각 기술을 위한 기술, 디자인을 위한 디자인을 하고있죠.："+
				"우리는 기술, 디자인, 심리학을 이용해서 끝내주는 웹 사이트를 만들고, 당신의 목표를 달성시켜 드립니다. ";
	var text_arr = text.split('');
	var counting = 0;
	
	var typing = setInterval(function() {
 		var box = $('.text-box');
		counting += text_input(text_arr, counting, box, 2);
		
		if (counting == text_arr.length) {
			clearInterval(typing);
			
			$('.yellow-bg').fadeIn(500, function() {
				$(".fuck_you").css("opacity", 1);
				play_ment();
			});
		}
		
	}, 1);
	
	$(document).on('click', '.img_show', function() {
		var img = $('.hide_img');
		
		if (img.css('display') == 'none') {
			img.fadeIn(300);
		} else {
			img.fadeOut(300);
		}
	});
});
</script>
		<img src="/resources/img/landing/human.jpg" style="position: fixed; top:0; right:0; display: none;"/>
		<div class="blue-bg" style="background: #0027f5; width: 100%; height: 100%;">
			<a class="text-box" style="display: inline-block; margin-left:10px; margin-top:10px; color: white;"></a>
		</div>
		<div class="yellow-bg" style="background: #ffd80a; color: black; width: 100%; height: 25%; position: absolute; bottom: 0; display: none; z-index: 2;">
			<div style="width: 100%; height: 100%; position: relative">
				<a class="wave" style="position: absolute; font-size:22vh; font-family:'Gothic NeoEB' !important; display:inline-block; left:100%; white-space: nowrap; margin-top:-2vh;">
				</a>
			</div>
		</div>
		<p class="fuck_you" style="position: fixed; bottom: -51vh; text-align: center; width: 100%; z-index: 0; opacity: 0;">
			<img style="height: 75vh;" src="/resources/common/img/fuck-you.gif">
		</p>