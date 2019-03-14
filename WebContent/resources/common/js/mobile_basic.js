$(document).ready(function() {
	var menu_icon = $('.menu_icon');
	
	$('.menu_icon').attr('src', '/resources/common/img/mobile_menu.png');
	$('.move-menu').css('opacity', 0);
	$('.menu_icon').on('click', function(event) {
		var etc = $('.etc');
		if ($('.menu_icon').attr('src') == '/resources/common/img/mobile_menu.png') {
			$('.menu_icon').attr('src', '/resources/img/landing/menu/02.png');
			
			var menu = $('.move-menu');
			var time = 0;
			
			menu.css('opacity', 0);
			
			for (var i = 0; i < menu.length; i++) {
				time += 200;
				
				$(menu[i]).stop().animate({
					left : 0,
					marginLeft : '-3px',
					opacity : 1
				}, time);
			}
		} else if (menu_icon.attr('src') == '/resources/common/img/white_mobile_menu.png') {
			$('.menu_icon').attr('src', '/resources/img/landing/menu/white-02.png');
			
			var menu = $('.move-menu');
			var time = 0;
			
			menu.css('opacity', 0);
			
			for (var i = 0; i < menu.length; i++) {
				time += 200;
				
				$(menu[i]).stop().animate({
					left : 0,
					marginLeft : '-3px',
					opacity : 1
				}, time);
			}
		} else if (menu_icon.attr('src') == '/resources/img/landing/menu/white-02.png') {
			$('.menu_icon').attr('src', '/resources/common/img/white_mobile_menu.png');
			
			var menu = $('.move-menu');
			var time2 = 0;
			
			for(var i = menu.length - 1; i >= 0; i--){
				time2 += 200;
				
				$(menu[i]).stop().animate({
					left : '200%',
					marginLeft : '0px',
					opacity : 0
				}, time2);
			}
		} else {
			$('.menu_icon').attr('src', '/resources/common/img/mobile_menu.png');
			
			var menu = $('.move-menu');
			var time2 = 0;
			
			for(var i = menu.length - 1; i >= 0; i--){
				time2 += 200;
				
				$(menu[i]).stop().animate({
					left : '200%',
					marginLeft : '0px',
					opacity : 0
				}, time2);
			}
		}
		
		event.preventDefault();
		event.stopPropagation();
		
		return false;
	});
});