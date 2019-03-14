$(function() {
	$('.only_number').spinner({min : 0, max:99999});
	$.each($('.only_number'), function(index, value) {
		if ($(value).val().length < 1) {
			$(value).val(0);
		}
	});
	$(document).on('keydown', '.only_number', function (e) {
	    $(this).val($(this).val().replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, ''));
    	var val = $(this).val();
		val = val.replace(/[^0-9]/g,"");
		$(this).val(val);
		
	    if(window.event)
	         key = window.event.keyCode;
	    else
	         key = e.which;

	    var event; 
	    if (key == 0 || key == 8 || key == 46 || key == 9){
	        event = e || window.event;
	        if (typeof event.stopPropagation != "undefined") {
	            event.stopPropagation();
	        } else {
	            event.cancelBubble = true;
	        }   
	        return ;
	    }
	    
	    if (key < 48 || (key > 57 && key < 96) || key > 105 || e.shiftKey) {
	        e.preventDefault ? e.preventDefault() : e.returnValue = false;
	    }
	    
	    if ($(this).val() > 99999) {
	    	$(this).val(99999);
	    }
	});
	
	$(document).on('focusout', '.only_number', function() {
		var val = $(this).val();
		val = val.replace(/[^0-9]/g,"");
		$(this).val(val);
		
		if ($(this).val().length < 1) {
			$(this).val(0);
		}
		
		if ($(this).val() > 99999) {
	    	$(this).val(99999);
	    }
	});
	
	//input number의 크기는 80으로 고정한다.
	//모바일에서 100%로 지정되기때문..
});