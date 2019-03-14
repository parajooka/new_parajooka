$(document).ready(function() {
	var flag = 1;
	var level = 0;
	var difficulty = 1;
	var nickname;
	 
	$('.next_btn2').on('mouseover', function() {
		var text = $('.field-long');
		if (text.val().length > 0) {
			$(this).attr('src', '/resources/img/contect/NextBlack.png');
		}
	}).on('mouseout', function() {
		$(this).attr('src', '/resources/img/contect/NextWhite.png');
	});
	
	$('.field-long').on('keyup', function() {
		if ($(this).val().length > 0) {
			$('.next_btn2').stop().animate({
				opacity : 1
			}, 300);
		} else {
			$('.next_btn2').stop().animate({
				opacity : '0.2'
			}, 300);
		}
		
	}).on('keydown', function(key) {
		if(key.keyCode == 13){//키가 13이면 실행 (엔터는 13)
			event.preventDefault();
			if (flag == 1 && $('.field-long').val().length == 0) {
				alert('닉네임을 입력해주세요.');
				return;
			} else {
				next_layout();
				$(this).blur();
			}
        }
	});
	
	if (his != null && his.length >= 1) {
		$('.field-long').val(his).attr('disabled', 'disabled');
		$('.field-long').keyup();
	}
	
	$('.next_btn3').on('mouseover', function() {
		$(this).attr('src', '/resources/img/contect/StartBlack.png');
	}).on('mouseout', function() {
		$(this).attr('src', '/resources/img/contect/StartWhite.png');
	});
	
	var next_layout = function() {
		if (flag == 1 && $('.field-long').val().length == 0) {
			alert('닉네임을 입력해주세요.');
			return;
		}
		
		$('.layout'+ flag).stop().animate({
			left : '-100%'
		}, 800);
		
		$('.layout'+ (flag + 1)).stop().animate({
			left : 0
		}, 800);
		
		flag++;
	}
	
	$('.btn_begin').on('click', function() {
		next_layout();
	});
	
	$('.next_btn').on('mouseover', function() {
		$(this).attr('src', '/resources/img/contect/NextBlack.png');
	}).on('mouseout', function() {
		$(this).attr('src', '/resources/img/contect/NextWhite.png');
	}).on('click', function() {
		$('.popup-container').stop().animate({
			left : 0
		}, 800);
		
		$('.nick-container').stop().animate({
			left : '-100%'
		}, 800);
		
		if (his == null || his.length < 1) {
			$('input').val('');
		} else  if (his != null && his.length >= 1) {
			$('.field-long').val(his).attr('disabled', 'disabled');
			$('.field-long').keyup();
		}
		
		setTimeout(function() {
			$('.field-long').focus();
		}, 800);
			
	});
	
	$('.close_btn').on('click', function() {
		if ($('.complete').css('display') != 'none') {
			location.reload();
		}
		
		//팝업창 원위치
		$('.popup-container').stop().animate({
			left : '100%'
		}, 800);
		
		//닉네임 입력창 원위치
		$('.nick-container').stop().animate({
			left : 0
		}, 800);
		
		//레이아웃 원위치
		flag = 1;
		level = 0;
		difficulty = 0;
		
		
		$('.layout').css('left', '100%');
		$('.layout1').stop().animate({
			left : 0
		}, 800);
		
		//버튼 비활성화
		$('.next_btn2').stop().animate({
			opacity : '0.2'
		}, 300);
		
		$('.complete_message_box').stop().fadeOut(500);
	});
	
	//포인트존 시작
	var change_timer = 3;
	
	$('.point_btn').on('mouseover', function(){ 
		$(this).css('background', 'black').css('color', 'white');
	}).on('mouseout', function() {
		$(this).css('background', 'none').css('color', 'black');
	}).on('click', function() {
		$('.randContect').attr('src', '');
		difficulty = $(this).attr('data');
	});
	
	$('.point_start').on('click', function() {
		if (level < total_level) {
			$('.point-zone').css('display', 'none');
			$('.img-zone').css('display', 'table-cell');
			
			var func = function(data) {
				var img = data['message'];
				level = parseInt(data["next_url"]);
				
				$('.progress').html(level + "/" + total_level);
				if (img == '등록된 이미지가 존재하지 않습니다.') {
					alert('등록된 이미지가 존재하지 않습니다.\r\n관리자에게 문의 하십시오.');
					location.reload();
					return;
				}
				
				$('.randContect').attr('src', '/res/img/contect/admin/'+ img);
				
				var timer = 0;
				var delay = 200;
				var count = (100/change_timer - 1)/delay;
				
				var count_timer = setInterval(function() {
					timer = timer + count;
					$("#progressbar").progressbar({
					      value: timer
				    });
					
					if (timer >= 100) {
						$('.point-zone').css('display', 'table-cell');
						$('.img-zone').css('display', 'none');
						clearInterval(count_timer);
					}
					
				}, 1000/delay);
			}
			
			get_item_info("/contect/rand", func, {'level':level, 'difficulty':difficulty}, $('.layout3'))
		} else {
			var func = function(data) {
				var message = data['message'];
				if (typeof message != "undefined" && message != null) {
					alert(message);
					location.replace();
				}
			}
			
			get_item_info("/contect/complete", func, {'nickname':$('.field-long').val(), 'level' : level, 'difficulty' : difficulty},  $('.layout3'));
			$('.point-zone').css('display', 'none');
			$('.complete').css('display', 'block');
			$('.congratulation_complete').css('padding-top', $(window).height()/100 * 40  +"px");
		}
	});
	
	$('.show_complete').on('click', function() {
		if (!$('.complete_message_box').is(":animated")) {
			$('.complete_message_box').stop().fadeIn(500);
			if ($('.random_code').html().length == 0) {
				$('.random_code').html(random_code());
			}
		}
	});
	
	var scrollTop;
	var last_logo = $('.last_logo');
	var win = $(window);
	var win_height = win.height();
	var before = 0;
	var complete_box = $('.complete_message_box');
	var scrollHeight = 0;
	var auto_before = 0;
	
	$(window).on('scroll', function() {
		scrollTop = win.scrollTop();
		
		scrollHeight = $('body').prop("scrollHeight") - win_height;
		if (scrollTop < last_logo.offset().top - win_height && scrollTop - before > 0 && scrollTop > auto_before) {
			complete_box.css('padding-bottom', parseInt(complete_box.css('padding-bottom')) + scrollTop - before);
			auto_before = scrollTop;
		}
		
		before = scrollTop;
	});
});