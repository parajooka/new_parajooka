/* jobs turn section */
var mask_checked = true;
var money_timer;
try {
	
function swing() {
    $('.last_message').animate({marginBottom:'10vw'},600).animate({marginBottom:'6vw'},600, swing);
}
var up;
var path = "/resources/img/landing/"
var ext = "png"
var count = 12;
var self = this, paths = [], load = 0, sectionHeight, windowHeight, currentScroll, percentageScroll, index;

if (path.substr(-1) === "/") {
	path = path.substr(0, path.length - 1)
}

for (var i = 1; i <= count; i++) {
	paths.push(path + "/" + pad(i, 2) + "." + ext);
}

$("<div class='jquery-sequencer-preload'></div>").appendTo("body");

$(paths).each(function() {
	$("<img>").attr("src", this).load(function() {
		$(this).appendTo("div.jquery-sequencer-preload");
		load++;

		if (load === paths.length) {
			$('.jquery-sequencer-preload').hide();
		}
	});
});
/* jobs turn section */

/* dollar section */
var show_check2 = false;
var show_time2;
var dollar_ment = " 우리는 끝내주는 웹을 만들고,：당신은 돈을 챙기면 됩니다.";
var dollar_ment_array = dollar_ment.split("");
var dollar_title = [];// 우리는 끝내주는 웹을 만들고,：당신은 돈을 챙기면 됩니다.
var before_ment;
var now_ment;

for (var i = 0; i < dollar_ment_array.length; i++) {
	if (i > 0) {
		before_ment = dollar_title[i - 1];
		now_ment = dollar_ment_array[i];
		now_ment = (now_ment == '：') ? '<br>' : now_ment;
		
		dollar_title.push(before_ment + now_ment);
	} else {
		dollar_title.push(dollar_ment_array[i]);
	}
}

/* dollar section */

/* egg section */
var show_check = false;
var show_time;
var egg_ment = " 당신의 기업이 대기업이거나：소기업이거나 아이돈케얼입니다.";
var egg_ment_array = egg_ment.split("");
var egg_title = [];// 우리는 끝내주는 웹을 만들고,：당신은 돈을 챙기면 됩니다.
var before_ment;
var now_ment;

for (var i = 0; i < egg_ment_array.length; i++) {
	if (i > 0) {
		before_ment = egg_title[i - 1];
		now_ment = egg_ment_array[i];
		now_ment = (now_ment == '：') ? '<br>' : now_ment;
		
		egg_title.push(before_ment + now_ment);
	} else {
		egg_title.push(egg_ment_array[i]);
	}
}

/* egg section */

/* human section */
var show_check3 = false;
var human_img_position = [];

for (var i = 90; i >= -630; i--) {
	human_img_position.push(i + '%');
}

/* human section */

/* background-animate */
var b_color = 0;
var b_op = 0;

var b_color_array = [];
var b_op_array = [];

for (var i = 0; i <= 234; i++) {
	b_color += i;
	b_op += 1/234;
	
	b_color = b_color > 234 ? 234 :  b_color
	b_op = b_op > 1 ? 1 :  b_op
	
	b_color_array.push("rgb("+ b_color +", "+ b_color +", "+ b_color +")");
	b_op_array.push(b_op);
}

var show_check4 = false;

/* background-animate */

var logo_left = 0;
var logo_top = 0;
var logo_width = 0;

var img_preloading_list = [
	"01.png", "02.png", "03.png", "04.png", "05.png", "06.png", "07.png", "08.png", "09.png", "10.png", "11.png", "12.png",
	"MrParajooka.gif", "menu/01.png", "BLACK2.png", "rain.gif", "main.gif"
];

var img_pre_array = [];

for (var i = 0; i < img_preloading_list.length; i++) {
	img_pre_array.push("/resources/img/landing/" + img_preloading_list[i]);
}

preloading(img_pre_array);

$(document).ready(function() {
	$('.landing-mask').css('height', $(window).height());
	//$('.landing_main').hide();
	$('.money-back-img').css({
		'width' : '400vw',
		'left' : '-65%'
	});
	$('.yes_no').height($(window).height());
	
	var flag = 1;
	var start_round_show;
	var timer2 = 500;
	
	//$(".logo").css("opacity", 0);
	$(".mask_first").textillate({ 
    	in: { effect: 'flipInY' },
    	callback: function () {
	    	setTimeout(function(){

	    		$(".landing_typing").fadeOut(500);
	    		setTimeout(function() {
	    			$(".landing_point_section").css("display", "table-cell");
	    			var color = '#'; 
	    			var letters = ['FFD8D8', 'BCE55C', 'B5B2FF', 'FFB2D9', '3DB7CC', '980000', 'FFC19E', 'B7F0B1', 'f6c9cc', 'a8c0c0', 'FEBF36', 'FF7238', '6475A0', 'acc7bf', '5e5f67', 'c37070', 'eae160', 'bf7aa3', 'd7d967'];
	    			
	    			var vertical_x = 0;
	    			var vertical_y = 0;
	    			
	    			var y_pattern_count = Math.round($(window).height()/($(window).width()/100 * 8)) + 8;
	    			var y_count = 0;
	    			
	    			var result;
	    			var result2;
	    			var result3;
	    			var result4;
	    			var result5;
	    			var color_count;
	    			var rand_color;
	    			var point_array = [];
	    			var paddings = "5vw";
	    			
	    			for (var i = 0; i <= 40 * y_pattern_count; i ++) {
	    				result = Math.random() * 2 + 6;
	    				result2 = Math.random() * 4 + 8;
	    				result3 = Math.floor(Math.random() * 10) + 1;
	    				result4 = Math.floor(Math.random() * 60) + 10;
	    				result5 = Math.floor(Math.random() * 4) + 1;
	    				color_count = Math.floor(Math.random() * (letters.length + 1));
	    				rand_color = color + letters[color_count];
	    				
	    				if (i/40 >= 1 && i%40 == 0) {
	    					vertical_y += result;
	    					vertical_x = 0;
	    					paddings = "0vw";
	    				} else {
	    					if (i == 0) {
	    						vertical_x = 0;
	    						paddings = result5 + "vw";
	    					} else {
	    						vertical_x += result;
	    						paddings = result5 + "vw";
	    					}
	    					
	    					if (i < 40) {
	    						paddings = "0vw";
	    					}
	    					
	    					vertical_x = vertical_x >= 96 ? result4 : vertical_x;
	    				}
	    				
	    				$(".point-section").append(
	    					'<a class="rand_point round_'+ i +' touch_parent" style="display: none; position:absolute; top:'+ vertical_y +'vw; left:'+ vertical_x +'vw; padding:'+ paddings +'; border-radius:100%; z-index:'+ result3 +';">'+	
	    						'<span class="rand_point round_'+ i +' color_round" style="display: inline-block; border-radius:100%; width: '+ result2 +'vw; height: '+ result2 +'vw; background: '+ rand_color +'; z-index:'+ result3 +';"></span>'+
	    					'</a>'
	    				);
	    				
	    				if (parseInt($(document.getElementsByClassName("round_" + i)).css("top")) > $(window).height() - $(document.getElementsByClassName("round_" + i)).height()) {
	    					var rounds = $(document.getElementsByClassName("round_" + i));
	    					rounds.css("top", $(window).height() - rounds.height());
	    				}
	
	    				point_array.push(document.getElementsByClassName("round_" + i));
	    			}
	    			
	    			var rand_round;
	    			
	    			var first_count = 5; //최초로 보여줄 점의 갯수
	    			var last_flag;
	    			
	    			start_round_show = function() {
	    				
		    			if (logo_left == 0 || logo_top == 0 || logo_width == 0) {
		    				logo_left = $(".logo").offset().left;
		    				logo_top = $(".logo").offset().top;
		    				logo_width = $(".logo").width();
		    			}	
	    				
	    				setTimeout(function() {
	    					if (flag > 7) {
	    						rand_round = Math.floor(Math.random() * point_array.length + 1);
	    						
	    						$(point_array[rand_round]).show();
	    						point_array.splice(rand_round, 1);
		    					timer2 /= 3;
	    					} else if (flag > 4) {
	    						rand_round = Math.floor(Math.random() * point_array.length + 1);
	    						
	    						$(point_array[rand_round]).show();
	    						point_array.splice(rand_round, 1);
		    					timer2 /= 1.2;
	    					} else {
	    						$(".round" + flag).show();
		    					timer2 /= 1;
		    					
		    					flag ++;
		    					return;
	    					}
	    					
	    					if (point_array.length > 1) {
	    						start_round_show();
	    					} else {
	    						//점 뿌리기 종료.
	    						$(point_array[point_array.length - 1]).show();
	    						
	    						$.each(document.getElementsByClassName("color_round"), function(index, value) {
	    							$(value).stop().animate({
	    								backgroundColor: "white"
	    							}, 1500);
	    						});
	    						
	    						$(".fixed_logo").fadeIn(2000, function() {
									$(".temp_logo").css("display", "none");
									$.each(document.getElementsByClassName("color_round"), function(index, value) {
		    							$(value).stop().remove();
		    						});
									
									$(".fixed_logo").stop().animate({
										top : logo_top,
										left : logo_left,
										width : logo_width,
										marginTop : "0px",
										marginLeft : "0px"
									}, 1500);

						    		$('.landing_main').show();
    								$(".mask_last").css("display", "table-cell");
    								$(".last_message").css({
										"opacity" : "1",
										"margin-left" : "2vw"
    								});
			  			  		  	black_logo();
			  			  		  	swing();
			  			  		  	
						    		$(window).scrollTop(0);
						    		mask_checked = false;
						    		
			    					$(window).scrollTop(5);
			  			  		  	
									$(".mask1").css("position", "fixed").stop().fadeOut(1500, function() {
	    								$(".landing_point_section").remove();
				  					  	$('html, body').css('overflow-y', 'auto');
				  					  	
				  					    $(".fixed_logo").remove();
							    		$(".logo").css("opacity", 1);
	    							});
								});
	    						
	    						return;
	    					}
	    					
	    					flag ++;
	    				}, timer2);
	    			}
	    			
	    			setTimeout(function() {
	    				start_round_show();
	    			}, 500);
	    		}, 550);
	    	}, 600);
    	}
    });
	
	var drop_round = function(dom) {
    	if (!dom.is(":animated")) {
    		dom.stop().animate({
				opacity : 0,
				top : "100%"
			}, 300);
		}
    }
	
	var flags = 0; 
    
    $(document).on("click touch touchstart", ".touch_parent", function() {
    	
    	if (flag <= 5) {
    		if (flag == 5) {
    			timer2 = 1000;
    		}
    		start_round_show();
    	}
    	
    	var rounds = $(this).children();
    	rounds.stop().animate({scale: '1.5', opacity:"0.5"}, 300, function() {
    		rounds.stop().animate({scale: '1', opacity:"1"}, 300);
    	});
    	if (rounds.children().length != 0) {    	
    		rounds.children().remove();
    	}
    	
    	x = event.pageX;
   	    y = event.pageY; 
   	    
	   	 var result = Math.floor(Math.random() * 100) + 1;
		 var class_name = "rounds_"+ result;
		 var ment_array = ["Excellent!!", "Great!!", "Perfect!!"];
    	
   	    $("body").append("<span class='"+ class_name +" speak' style='position:fixed; top:"+ y +"px; left: "+ x +"px; font-size: 8vw; display:inline-blcok; z-index:9999;'>"+ ment_array[flags] +"</sapn>");
    	
	   	 flags ++;
	   	 
	   	 if (flags > 2) {
	   		 flags = 0;
	   	 }
   	    
   	    var my_child = $(document.getElementsByClassName(class_name)[0]);
	 	
	 	my_child.stop().animate({
	 		top : "-=15px",
	 		right : "-=10px",
	 		opacity : 0
	 	}, 500, function() {
	 		my_child.remove();
	 	});
	 	
	 	if (flag < 10) {
	 		drop_round($(this));
	 	}
    });
    
 /*   $(".point-section > .rand_point").on("click", function() {
    });*/
    
	
	var win = $(window);
	var win_height = win.height();

	var layout1 = $('.layout1');
	var layout2 = $('.layout2');
	var layout3 = $('.layout3');
	var layout4 = $('.layout4');
	var layout5 = $('.layout5');
	
	layout5.height(win_height * 1.3);
	
	var money_rain = $('.money-back-img');
	var jobs_layout = $('.jobs-layout');
	var jobs_img = $('.steven-jobs');
	var full_screen = $('.full-screen');
	
	var egg_content_box = $('.egg-content-section > p');
	var egg_text_box = $('.egg-title-section > p');
	var egg_img = $('.egg-img-section > img');
	
	var human_text = $('.human-text-section');
	var human_img = $('.human-img');
	
	var dollar_content_box = $('.dollar-content-section > p');
	var dollar_text_box = $('.dollar-title-section > p');
	var dollar_img = $('.dollar-img-section > img');
	
	var yes_no = $('.yes_no');
	var show_check4 = false;
	
	var logo2 = $('.logo2')
	var img_name;
	var scrollTop;
	var before = 0;
	var menu_icon = $('.menu_icon');
	
	var last_flag = false;
	
	full_screen.each(function() {
		$(this).height(win_height).stick_in_parent();
	});
	
	$('.mask-test').height($(window).height() * 2.5).css({
		'margin-top': '-' + $(window).height() + 'px',
		'width' : '100%',
		'position' : 'absolute',
		'bottom' : '0'
	});
	
	window.onresize = function() {
		win_height = window.innerHeight;
	}
	
	win.on('scroll', function() {
		scrollTop = $(this).scrollTop();
		up = scrollTop - before > 0 ? true : false;
		before = scrollTop;
		
		if (scrollTop < 1) {
    		$('.steven-jobs').attr("src", "/resources/img/landing/01.png");
    		$(".scroll_down").show();
    	} else {
    		$(".scroll_down").hide();
    	}
		
		if (scrollTop < layout2.offset().top && !up) {
			show_check2 = false;
			dollar_text_box.html('');
			dollar_content_box.stop().css({
				"opacity" : "0",
				"left" : "-150%"
			});
			dollar_img.stop().css('left', '100%');
		} else if (scrollTop < layout3.offset().top && !up) {
			show_check = false;
			egg_text_box.html('');
			egg_content_box.stop().css({
				"opacity" : "0",
				"left" : "-150%"
			});
			egg_img.stop().css('top', '100%');
		} else if (scrollTop < layout4.offset().top && !up) {
			show_check3 = false;
			human_text.stop().css('opacity', 0);
			human_img.stop().css('left', '90%');
		}
		
		
		if (scrollTop >= layout4.offset().top - 50 && scrollTop < $(".mask-test").offset().top - 50) {
    		$("#logo_img").attr("src", "/resources/common/img/renewal/white_logo.png");
		} else {
    		$("#logo_img").attr("src", "/resources/common/img/renewal/logo.png");
		}
	});
});
} catch (e) {
	alert(e);
}