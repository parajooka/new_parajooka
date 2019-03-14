/**
 * 
 * Created by Para & Jooka http://para-jooka.online
 * 
 * Version: 1.0.0 Requires: jQuery 1.6+
 * 
 * Modifier Para & Jooka in Daegu, Korea On February 27, 2019 ~
 * 
 * Prohibits illegal distribution and can be held liable for any violation.
 * 
 */
var typing_interval;
var typing_del;
		
(function($) {
	$.fn.typing = function(options) {
		//Default Options Value
		var settings = $.extend({
			typing_speed: 5,
			del_speed : 3,
			text : 'Hello World!!',
			color : 'black',
			font_size : '10px',
			line_height : '25px',
			min_height : '0px',
			background : '#fff',
			cursor_width : '0.5',
			cursor_color : '#fff',
			typing_start_callback : null, 
			load_end_callback : null,
			typing_end_callback : null
        }, options);
		
		var self = $(this);
		if (self.find(".typing_container").length == 0) {
			self.html("<a class='typing_container'><span class='typing_area'></span><span class='typing_cursor'>&nbsp;</span></a>");
			
			setInterval(function() {
				if (typing_cursor.css("opacity") == 0) {
					typing_cursor.css("opacity", 1);
				} else {
					typing_cursor.css("opacity", 0);
				}
			}, 500);
		}
		
		var typing_container = self.find(".typing_container");
		var typing_cursor = self.find(".typing_cursor");
		var typing_area = self.find(".typing_area");
		
		typing_container.css({
			"color" : settings.color,
			"font-size" : settings.font_size,
			"background" : settings.background,
			"min-height": settings.min_height,
			"vertical-align" : "top",
			"display": "inline-block",
		});
		
		typing_cursor.css({
			"width" : "1px",
			"display" : "inline-block",
			"border-right" : settings.cursor_width + "px solid "+ settings.cursor_color,
			"margin-left" : "5px"
		});
		
		
		var typing_text = settings.text;
		var typing_text_array = typing_text.split("");
			typing_text = typing_text.replace(/\\/gi, "<br>"); 
		var typing_flag = 0;
		
		function WriteText() {
			if (!typing_del) {clearInterval(typing_del);}
			
			if (typeof settings.typing_start_callback == "function") {
				settings.typing_start_callback();
			}
			
			setTimeout(function() {
				typing_interval = setInterval(function() {
					if (typing_area.html() == typing_text) {
						typing_flag = 0;
						if (typeof settings.typing_end_callback === "function") {
							settings.typing_end_callback();
						}
						
						clearInterval(typing_interval);
					} else {
						if (typing_text_array[typing_flag] == "\\") {
							typing_area.append("<br>");
						} else {
							typing_area.append(typing_text_array[typing_flag]);
						}
						
						typing_flag ++;
					}
				}, settings.typing_speed * 100);
			}, 300);
		}
		
		if (typing_area.html().length > 0) {
			//현재 작동중인 인터벌 종료
			if (!typing_interval) {clearInterval(typing_interval);}
			typing_del = setInterval(function() {
				if (typing_area.html().length == 0) {
					WriteText();
					clearInterval(typing_del);
				} else {
					//한글자씩 삭제
					typing_area.html(typing_area.html().slice(0, -1));
				}
			}, typing_area.html().length == 0 ? settings.typing_speed * 100 : settings.del_speed * 100);
		} else {
			WriteText();
		}
		
		
		if (typeof settings.load_end_callback === "function") {
			settings.load_end_callback();
		}
			
	};
}(jQuery));