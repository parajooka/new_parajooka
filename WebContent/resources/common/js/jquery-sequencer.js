/**
 * jQuery-Sequencer https://github.com/skruf/jQuery-sequencer
 * 
 * Created by Thomas Låver http://www.laaver.com
 * 
 * Version: 2.0.0 Requires: jQuery 1.6+
 * 
 * Modifier Para & Jooka in Daegu, Korea On October 18, 2018 ~
 * 
 */

(function($) {

	$.fn.sequencer = function(options) {
		var self = $(this);
		var custom_function = options.func;
		var custom_count = options.count;
		var custom_height = options.height;
		var custom_remainder = options.remainder;
		var custom_else_func = options.else_func;

		if (typeof custom_height != "undefined")
			$(this).height(custom_height);
		
		$(window).on('scroll',
				function() {
					sectionHeight = self.height();
					windowHeight = $(this).height();
					currentScroll = $(this).scrollTop();
					
				if (typeof custom_remainder != "undefined")
					sectionHeight -= custom_remainder;
					
					percentageScroll = 100 * (currentScroll - Math.round(self.offset().top)) / (sectionHeight - windowHeight);
					
					index = Math.round(percentageScroll / 100 * custom_count);
					
					if (index < custom_count) {
						custom_function();
					} else if (index >= custom_count && currentScroll <= self.offset().top + self.height() - windowHeight) {
						if (typeof custom_else_func === "function") custom_else_func();
					}
				});

		return this;

	};

}(jQuery));