/**
 * 
 */
$(document).ready(function() {
	$('.search_my_history').dialog({
		autoOpen: false,
		appendTo : ".container",
		show: { effect: "blind", duration: 300 },
		hide: { effect: "blind", duration: 300 }
	});
	
	//등록 버튼
	$('.open_modal').on('click', function() {
		$('.search_my_history').dialog("open");
	});
	
	$(window).on("keydown",function(event) {
		if (event.keyCode == 27) {
			$(".ui-dialog-titlebar-close").trigger("click");
		}
	});
});