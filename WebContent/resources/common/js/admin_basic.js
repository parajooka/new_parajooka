$(document).ready(function() {
	$('.navigation, .container').css({
		"min-height" : $(window).height() - 1
	});
	
	$(window).on('resize', function() {
		$('.navigation, .container').css({
			"min-height" : $(window).height() - 1
		});
	});
	

	$('.open_modal').on('click', function() {
		$('body').append("<div id='modal_mask' style='position:fixed; background-color:black; z-index:10; opacity:0.5; height:"+ $(window).height() +"px; width:100%;'></div>")
	});
	
	$(document).on('click', '.ui-dialog-titlebar-close', function() {
		$(document.getElementById("modal_mask")).remove();
	});
	
	$(".admin_menu_open").on("click", function() {
		if ($(this).attr("src") == "/resources/common/img/menu_open.png") {
			$(this).parent().parent().find(".child_menu").show();
			$(this).attr("src", "/resources/common/img/menu_close.png")
		} else {
			$(this).parent().parent().find(".child_menu").hide();
			$(this).attr("src", "/resources/common/img/menu_open.png")
		}
	});
	
	$(".admin_menu_open").trigger("click");
	
	//관리자페이지 레이아웃 높이 지정
	var navigation_height = $(".navigation").height();
	
	if (navigation_height > $(window).height()) {
		$(".container").css("min-height", navigation_height +"px");
	}
});