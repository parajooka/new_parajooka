/**
 * 
 * Created by Para & Jooka http://para-jooka.online
 * 
 * Version: 1.0.0 Requires: jQuery 1.6+
 * 
 * Modifier Para & Jooka in Daegu, Korea On October 18, 2018 ~
 * 
 * Prohibits illegal distribution and can be held liable for any violation.
 * 
 */

(function($) {
	$.fn.textslider = function(options) {
		//Default Options Value
		var settings = $.extend({
			slider_speed: 5,
			move_px: 1,
			slider_margin : 50,
			text_direction : "left",
			text : 'Hello World!!',
			color : 'black',
			font_size : '10px',
			background : "#fff",
			width : $(window).width(),
			load_end_callback : null
        }, options);
		
		//Customer options -- start
		var slider_speed = settings.speed; //text move speed
		var move_area = settings.move_px; //text move pixel
		var text_margin = settings.slider_margin; //text margin
		var slider_text = settings.text; //text content
		var color = settings.color; //text color
		var font_size = settings.font_size; //text font size
		var background = settings.background; //container background
		var slider_width = settings.width; //section width
		var load_callback = settings.load_end_callback;
		var text_direction = settings.text_direction;
		//Customer options -- end
		
		//fixed variable
		var interval_array = [];
		var slider_stage = 1;
		var self = $(this);
		
		//Plugin action start
		
		//-- 1. input layout to target
		var $main_container = $('<div class="slider_text_container"  style="width:100%; position:relative;"></div>');
		var $main_section = $('<div class="slider_text_section" style="position:relative;"></div>');
		var $main_span = $('<span class="slider_text_box">'+ slider_text +'</span>');
		
		$main_span.css({
			"color" : color,
			"font-size" : font_size,
			"position" : "absolute",
			"top" : 0,
			"white-space" : "nowrap"
		});
		
		$main_section.append($main_span);
		$main_container.append($main_section);
		
		self.append($main_container);
		
		$main_section.css({
			"height" : $main_span.height() +"px",
			"width" : slider_width + "px"
		});
		
		$main_container.css("background", background);
		
		var slider_init_left = "-" + $main_span.width() + "px";
		
		$main_span.css(text_direction, slider_init_left);
		
		function sliderCreate(dom, stage) {
			interval_array[stage + ""] = setInterval(function() {
				dom.css(text_direction, "+="+ move_area +"px");
				
				if (parseInt(dom.css(text_direction)) >= dom.width() + text_margin 
						&& $main_section.find(".slider_stage_" + stage).length == 0) {
					$span = $('<span class="slider_text_box slider_stage_'+ stage +'">'+ slider_text +'</span>');
					console.log(slider_init_left);
					$span.css({
						text_direction : slider_init_left,
						"color" : color,
						"font-size" : font_size,
						"position" : "absolute",
						"top" : 0,
						"white-space" : "nowrap"
					});
					
					$main_section.append($span);
					slider_stage ++;
					sliderCreate($span, slider_stage);
				}
				
				if (parseInt(dom.css(text_direction)) >= dom.parent().width() + dom.width()) {
					clearInterval(interval_array[stage + ""]);
					dom.remove();
				}
			}, slider_speed);
		}
		
		
		sliderCreate($main_span, slider_stage);
		
		//load callback start
		if (typeof load_callback === "function") {
			load_callback();
		}
		//load callback end
		
		//Plugin action end
		return this;
	};

}(jQuery));