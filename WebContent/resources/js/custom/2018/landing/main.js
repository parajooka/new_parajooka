var page_load = false;
var up;
var timer;
var max_left = 0;

var mask_checked = true;


function swing() {
    $('.last_message').animate({marginBottom:'10vw'},600).animate({marginBottom:'6vw'},600, swing);
}

/* jobs turn section */
var path = "/resources/img/landing/"
var ext = "png"
var count = 12;
var self = this, paths = [], load = 0, sectionHeight, windowHeight, currentScroll, percentageScroll, index;

if (path.substr(-1) === "/") {
	path = path.substr(0, path.length - 1)
}

for (var i = 1; i <= count; i++) {
	paths.push(path + "/pc_" + pad(i, 2) + "." + ext);
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

/* human move section */
var human_img_position = [];
var human_check = false;


/* human move section */

var logo_left = 0;
var logo_top = 0;
var logo_width = 0;

$(document).ready(function () {
	var win = $(window);
	var win_height = win.height();
	var move_checked = false;
	
    //$('.section').css('overflow', 'hidden');
    $('.landing-mask').css('height', $(window).height());
    

    //$("html, body, .container, .section").css("overflow", "hidden");
    
    var drop_round = function(dom) {
    	if (!dom.is(":animated")) {
    		dom.stop().animate({
				opacity : 0,
				top : "100%"
			}, 300);
		}
    }
    

	var flag = 1;
	var start_round_show;
	var timer2 = 500;
	var x_count = 26;
	
	//$(".logo").css("opacity", 0);
	
	var color_count = 0;
	
    $(".mask_first").textillate({ 
    	in: { effect: 'flipInY' },
    	callback: function () {
	    	setTimeout(function(){

	    		$(".landing_typing").fadeOut(500);
	    		setTimeout(function() {
	    			$(".landing_point_section").show();
	    			
	    			var color = '#'; 
	    			var letters = ['50adf1', 'ffcc66', 'f59399', 'd07dcb', '5acfd1', 'fd8d5e'];
	    			
	    			var vertical_x = -8;
	    			var vertical_y = -3;
	    			
	    			var y_pattern_count = Math.round($(window).height()/($(window).width()/100 * 12)) + 8;
	    			var y_count = 0;
	    			
	    			var result;
	    			var result2;
	    			var result3;
	    			var result4;
	    			var result5;
	    			
	    			var rand_color;
	    			var paddings = "2vw";
	    			var point_array = [];
	    			
	    			for (var i = 0; i <= x_count * y_pattern_count; i ++) {
	    				result = Math.random() * 2 + 3; // 간격
	    				result2 = Math.random() * 5 + 11; // 크기
	    				result3 = Math.floor(Math.random() * 10) + 1;
	    				result4 = Math.floor(Math.random() * 26) + 50;
	    				result5 = Math.floor(Math.random() * 2) + 1;
	    				rand_color = color + letters[color_count];
	    				
	    				color_count ++;
	    				color_count = color_count > letters.length - 1 ? 0 : color_count;
	    				
	    				if (i/x_count >= 1 && i%x_count == 0) {
	    					vertical_y += result;
	    					vertical_x = -8;
	    					paddings = "0vw";
	    				} else {
	    					if (i == 0) {
	    						vertical_x = -8;
	    						paddings = result5 + "vw";
	    					} else {
	    						vertical_x += result;
	    						paddings = result5 + "vw";
	    					}
	    					
	    					if (i < x_count) {
	    						paddings = "0vw";
	    					}
	    					
	    					vertical_x = vertical_x >= 96 ? result4 : vertical_x;
	    				}
	    				
	    				$(".point-section").append(
	    					'<a class="rand_point round_'+ i +' touch_parent" style="display: none; position:absolute; border-radius :100%; top:'+ vertical_y +'vw; left:'+ vertical_x +'vw; padding:'+ paddings +'; z-index:'+ result3 +';">'+	
	    						'<span class="rand_point round_'+ i +' color_round" style="position:abosute; border-radius :100%; top:0; left:0; display: inline-block; width: '+ result2 +'vw; height: '+ result2 +'vw; background: black; z-index:'+ result3 +';"></span>'+
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
	    						
	    						setTimeout(function() {
		    						$.each(document.getElementsByClassName("color_round"), function(index, value) {
		    							$(value).stop().animate({
		    								backgroundColor: "white"
		    							}, 1500);
		    						});
		    						
									$(".fixed_logo").stop().fadeIn(2000, function() {
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
										

	    								$(".mask_last").css("display", "table-cell");
	    								$(".last_message").css({
	    										"opacity" : "1",
	    										"margin-left" : "2vw"
	    								});
	    								
							    		swing();
										
										$(".mask1").fadeOut(1500, function() {
		    								$(".landing_point_section").remove();
		    								
								    		$(window).scrollTop(0);
								    		mask_checked = false;
								    		
								    		$("html, body, .container").css("overflow-y", "auto");
								    		$(".fixed_logo").remove();
								    		$(".logo").css("opacity", 1);
		    							});
									});
	    						}, 1300);
								
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
    
    
    var flags = 0; 
    
    $(document).on("click touch touchstart", ".touch_parent", function(event) {

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
    	 
		$("body").append("<span class='"+ class_name +"' style='position:fixed; top:"+ y +"px; left: "+ x +"px; font-size: 1.5vw; display:inline-blcok; z-index:99999;'>"+ ment_array[flags] +"</sapn>");
    	

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
    
    //영역별 높이 설정
    var money_mask = $(".money-mask");
    var money_img = $(".money-img");
    var Mr_ParaJooka = $(".MrParajooka");
	var yes_no = $('.YesNo');
		yes_no.css('margin-bottom', '-50px');
    var Mr_checked = false;
    
    var section = [$('.steven-section'), $('.money-section'), $('.info-section'), $('.human-section'), $('.choice-section')];
    var section_height = [3000, win_height + money_mask.height(), win_height, 5000, win_height];
    
    $.each(section, function(index, value) {
    	value.height(section_height[index]);
    });
    
    //sticky 설정
    var sticky_section = ["img-zone", "human-container"];
    
    $.each(sticky_section, function(index, value) {
    	$("."+ value).height(win_height).stick_in_parent();
    });
    
    var diff = 0;
    var before = 0;
    var scrollTop = 0;
    
    $(window).on("scroll", function() {
    	scrollTop = $(this).scrollTop();
    	diff = scrollTop - before;
    	
    	if (diff > 0) {
    		up = true;
    	} else {
    		up = false;
    	}
    	
    	if (scrollTop < 1) {
    		$('.steven-jobs').attr("src", "/resources/img/landing/01.png");
    		$(".scroll_down").show();
    	} else {
    		$(".scroll_down").hide();
    	}
    	
    	before = scrollTop;
    	if (scrollTop > section[3].offset().top - 150 && scrollTop <= section[4].offset().top - 50) {
    		white_logo();
    	} else {
    		black_logo();
    	}
    	
    	if (scrollTop >= section[4].offset().top - 100 && yes_no.css('opacity') != 1 && !yes_no.is(":animated")) {
    		yes_no.stop().animate({
    			marginBottom : 0,
    			opacity : 1
    		});
    	} else if (scrollTop >= section[2].offset().top - 100 && !Mr_checked) {
    		$('.MrParajooka').showAni({
				marginTop : '-45vw'
			}, 500);
    		
    		Mr_checked = true;
    	} else if (scrollTop >= section[1].offset().top + money_mask.height() && parseInt(money_img.css("margin-left")) == 0) {
    		$('.money-img').showAni({
				marginLeft : '-' + ($('.money-img').width() + 150) + 'px'
			}, 500);
    	}
    	
    	
    	if (scrollTop >= $(".human-section").offset().top && scrollTop <= $(".human-section").offset().top + $(".human-section").height() - 50) {
    		$("#logo_img").attr("src", "/resources/common/img/renewal/white_logo.png");
    	} else {
    		$("#logo_img").attr("src", "/resources/common/img/renewal/logo.png");
    	}
    	
    });
});