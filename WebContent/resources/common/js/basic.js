function setCookie(cName, cValue, cDay){
    var expire = new Date();
    expire.setDate(expire.getDate() + cDay);
    cookies = cName + '=' + escape(cValue) + '; path=/ '; // 한글 깨짐을 막기위해 escape(cValue)를 합니다.
    if(typeof cDay != 'undefined') cookies += ';expires=' + expire.toGMTString() + ';';
    document.cookie = cookies;
}

$(document).ready(function() {
	//start_loading($("body"));
/*	$('.navigation').stick_in_parent();
	
	$('.logo-section').jrumble({
		x: 2,
		y: 2,
		rotation: 0,
		speed: 80
	});
	
	$('.menu').jrumble({
		x: 2,
		y: 0,
		rotation: 0,
		speed: 80
	});*/
	
	$('.open_modal').on('click', function() {
		$('body').append("<div id='modal_mask' style='position:fixed; background-color:black; top:0; left:0; z-index:10; opacity:0.5; height:"+ $(window).height() +"px; width:100%;'></div>")
	});
	
	$(document).on('click', '.ui-dialog-titlebar-close', function() {
		$(document.getElementById("modal_mask")).remove();
	});
	
	
	$(".hire_hide").on("click", function() {
		if (confirm("공고 알림을 1일동안 감추시겠습니까?\r\n다시 보려면 브라우저 쿠키를 삭제 해야만 합니다.")) {
			setCookie("hire_hide", "hide", 1);
			$(".hire_megaphone").remove();
		}
	});
	
	$('.logo-section, .menu').hover(function(){
		$(this).trigger('startRumble');
	}, function(){
		$(this).trigger('stopRumble');
	});
});