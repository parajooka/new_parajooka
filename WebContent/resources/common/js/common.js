var location_href;
var logo_img = document.getElementsByClassName("etc");
//숫자를 원하는 자릿수만큼 표현해준다.
function pad(n, width) {
	  n = n + '';
	  return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
}

function random_code() {
	 var text1 = "";
	 var text2 = "";
	 var result="";
	 var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWRXYZ";
	 var num = "0123456789";
	
	 for( var i=0; i < 2; i++ )
	 {      
	     text1 += alphabet.charAt(Math.floor(Math.random() * alphabet.length));
	     text2 += num.charAt(Math.floor(Math.random() * num.length));
	 }
	 
	 result = text1+text2;
	 
	 var result_array = result.split("");
	 
	 function shuffle(a) {
	    var j, x, i;
	    for (i = a.length; i; i -= 1) {
	        j = Math.floor(Math.random() * i);
	        x = a[i - 1];
	        a[i - 1] = a[j];
	        a[j] = x;
	    }
	}
	 
	 shuffle(result_array);
	 
	 return result_array.toString().replace(/,/gi, "");
}

var check_ani = function(name) {
	return $('.' + name).is(':animated');
} ;

//텍스트 입력 함수
var text_input = function (ment, line_check, dom, input_size) {
	var coment = '';
	
	for (var i = 0; i < input_size; i++) {
		if (line_check + i <= ment.length) {
			text = ment[line_check + i];
			if (typeof text != "undefined") {
				text = (text == "：") ? "<br>" : text;
				text = (text == "！") ? "&#34;" : text;
				coment += text;
			}
		}
	}
	
	coment = dom.html() + coment;
	dom.html(coment);
	
	return (line_check + input_size <= ment.length) ? input_size : (ment.length - line_check);
}

//해당 layout의 위치를 구해주는 함수
var layout_top = function(name) {
	var top = $('.'+ name).offset().top
	var top = $('.'+ name).offset().top + "";
	
	top = top.substring(0, (top.indexOf('.') < 0) ? top.length : top.indexOf('.'));
	
	return parseInt(top);
}

//이지가 파일 형식 추출
var img_format = function(img) {
	var point = img.attr('src').lastIndexOf('.');
	return img.attr('src').substring(point + 1, img.attr('src').length);
}

//css구할때 숫자만 반환
var css_format = function(dom) {
	dom = dom.replace('px', '');
	dom = dom.replace('%', '');
	
	return parseInt(dom);
}

/**
 * 사용자 스크롤 컨트롤 함수
 * ref : 스크롤 허용여부
 * delay : 스크롤 중복 방지
 * 스크롤할 돔 : dom
 * 레이아웃이 들어있는 배열 : arr
 * 스크롤 속도 : scroll_speed
 */
var custom_scroll = function(state, dom, arr, scroll_speed, flags) {
	var flag = flags;
	var ref = true;
	var delay = true;
	var top = dom.scrollTop();
	
	if (state == "up") {
		for (var i = 1; i < arr.length; i++) {
			if (top >= arr[i] - 2) { //오차범위 2까지 허용
				continue;
			} else if (top < (arr[i] - 150)) {
				dom.stop().animate({
					scrollTop : top + 100
				}, scroll_speed);
				break;
			} else {
				delay = false; //스크롤하는동안 딜레이를 false로 지정해둠
				var minus = arr[i] - top;
				dom.scrollTop(arr[i]);
				
				ref = false; //스크롤 이동 허용 여부를 false로 해준다. 해당 레이아웃에서 수행해야할 작업을 완료한 후 다시 true로 허용할것.
				delay = true; //스크롤 이동이후에는 다시 타이머를 true로 셋팅한다.
				flag = i;
				
				break;
			}
		}
		
		return [flag, ref, delay];
	} else if (state == "down") {
		var pos = 0;
		
		var top = dom.scrollTop();
		pos = (flags - 1 < 0) ? 0 : arr[flags - 1];
		
		console.log("top : " + top + ", pos : " + pos);
		
		if (top >= arr[flags] - 5 && top <= arr[flags] + 5) {
			delay = false; //스크롤하는동안 타이머를 false로 지정해둠 
			
			timer_check = true; //스크롤 이동이후에는 다시 타이머를 true로 셋팅한다.
			ref = false; //스크롤 이동 허용 여부를 false로 해준다. 해당 레이아웃에서 수행해야할 작업을 완료한 후 다시 true로 허용할것.
		} else if (top > (pos + 100)) {
			dom.stop().animate({
				scrollTop : top - 100
			}, scroll_speed);
		} else {
			delay = false; //스크롤하는동안 타이머를 false로 지정해둠 
			
			dom.scrollTop(pos);
			timer_check = true; //스크롤 이동이후에는 다시 타이머를 true로 셋팅한다.
			ref = false; //스크롤 이동 허용 여부를 false로 해준다. 해당 레이아웃에서 수행해야할 작업을 완료한 후 다시 true로 허용할것.
			
			flag = flags - 1;
			flag = (flag < 0) ? 0 : flag;
		}
		
		return [flag, ref, delay, pos];
	}
}

var start_loading = function(popup) {
	var dom =   "<div class='loading-mask' style='width:100%; left:0; display:table; height:100vh; background-color: rgba( 0, 0, 0, 0.5); position:fixed; top:0; z-index:9999;'>"+
					"<div style='display:table-cell; text-align:center; vertical-align:middle;'>"+
						"<img style='width:2.5vw;' src='/resources/common/img/loading.gif'>"+
					"</div>"+
				"</div>";
	popup.append(dom);
}

var end_loading = function(callback) {
	$('.loading-mask').fadeOut(500, function() {
		$('.loading-mask').hide(function() {
			if (typeof callback === 'function') {
				callback();
				$('.loading-mask').remove();
			} else {
				$('.loading-mask').remove();
			}
		});
	});
}

//타임아웃 중복 사용방지용 함수
var check_timeout = true;
var c_Timeout = function (func, time) {
	if (check_timeout) {
		//중복 방지를위해 false로 지정
		check_timeout = false;
		
		//사용자가 요청한 타임아웃 실행
		setTimeout(function() {
			func();
		}, time);
		
		//사용자가 지정한 시간뒤에 다시 타임아웃 허용.
		setTimeout(function() {
			check_timeout = true;
		}, time);
	}
}

//이미지 미리 로딩
function preloading (imageArray) { 
	start_loading($('body'));
	
	let n = imageArray.length; 
	
	for (let i = 0; i < n; i++) { 
		let img = new Image(); 
		img.src = imageArray[i]; 
		
		if (i == n - 1) {
			end_loading();
			return true;
		}
	}
}

function white_logo() {
	$('.logo').attr('src', '/resources/img/landing/menu/white-logo.png');
	$(logo_img[0]).attr('src', '/resources/img/landing/menu/white-about.png');
	$(logo_img[1]).attr('src', '/resources/img/landing/menu/white-contact.png');
	$(logo_img[2]).attr('src', '/resources/img/landing/menu/white-work.png');

	if ($('.move-menu').css('opacity') == 0) {
		$('.menu_icon').attr('src', '/resources/common/img/white_mobile_menu.png');
	} else{
		$('.menu_icon').attr('src', '/resources/img/landing/menu/white-02.png');
	}
	
	logo = "white;"
}

function black_logo() {
	$('.logo').attr('src', '/resources/img/landing/menu/logo.png');
	$(logo_img[0]).attr('src', '/resources/img/landing/menu/about.png');
	$(logo_img[1]).attr('src', '/resources/img/landing/menu/contact.png');
	$(logo_img[2]).attr('src', '/resources/img/landing/menu/work.png');
	
	if ($('.move-menu').css('opacity') == 0) {
		$('.menu_icon').attr('src', '/resources/common/img/mobile_menu.png');
	} else{
		$('.menu_icon').attr('src', '/resources/img/landing/menu/02.png');
	}
	
	logo = "black;"
}


function wrapFile() {
	var file_target;
	$.each($("body").find("input[type=file]"), function(index, value) {
		if ($(value).parent().attr("class") != "file_wrapper") {
			$(value).wrap(
					"<div>"+
						"<a class='file_wrapper'></a>"+
						"<div class='upload_box'>"+
							"<img class='file_wrap_img' src='/resources/common/img/fileupload.png'/>"+
						"</div>"+
						"<div class='upload_file_name'>"+
							"파일을 업로드 해주세요."+
						"</div>"+
					"</div>"
			).css("display", "none");
		}
	});
}

function handleImgFileSelect(e) {
    if (ImgFileCheck($(e.target))) {
    	var files = e.target.files;
        var filesArr = Array.prototype.slice.call(files);
        filesArr.forEach(function(f) {
            if(!f.type.match("image.*")) {
                return;
            }

            sel_file = f;

            var reader = new FileReader();
            reader.onload = function(e) {
            	$(".img_preview").attr("src", e.target.result);
            }
            reader.readAsDataURL(f);
        });
    } else {
    	alert("이미지 파일만 업로드가 가능합니다.");
    	$(e.target).trigger("click");
    }
}

function ImgFileCheck(file) {
	var file_type = file.val().slice(file.val().indexOf(".") + 1).toLowerCase();
	
	if (file_type != "jpg" && file_type != "png" && file_type != "gif" && file_type != "bmp") {
		file.val("");
		file.parent().nextAll(".upload_file_name").html("파일을 업로드 해주세요.");
		$(".img_preview").attr("src", "/resources/common/img/no-image.png");
		return false;
	} else {
		return true;
	}
}


$(document).ready(function() {
	location_href = function location_href(url) {
		setTimeout(function() {
			$('body').fadeOut(200);
			setTimeout(function() {
				location.href = url;
			}, 600);
		}, 2500);
	}
	
	$('body').fadeIn(500);
	
	$.fn.showAni = function (ani_option, timer) {
		//예약 확인 시작
		var q = $(this).queue('fx');
		//예약시 종료
		if(q.length > 0) return;
		
		$(this).queue(function() {
			if (!$(this).is(':animated'))
				$(this).stop().animate(ani_option, timer).dequeue();
		});
	}
	
	$(document).on("click", ".upload_box", function(){
		$(this).prev().children("input[type=file]").trigger("click");
	});
	
	$(document).on("change", "input[type=file]", function(){
		if ($(this).val().length > 0) {
			$(this).parent().nextAll(".upload_file_name").html($(this).val());
		}
	});
	

	//ie, edge fixed요소 스크롤시 덜컹 거리는 현상 해결
	if(navigator.userAgent.match(/Trident\/7\./) || navigator.userAgent.toLowerCase().search("edge/") > -1 || navigator.appName == "Microsoft Internet Explorer") {
        $('html, body').on("mousewheel", function (event) {
        	if ($("html").css("overflow-y") == "auto" && $("body").css("overflow-y") == "auto") {
	            event.preventDefault();
	    
	            var wheelDelta = event.wheelDelta;
	    
	            var currentScrollPosition = window.pageYOffset;
	            
	            if (event.originalEvent.wheelDelta > 0) {
	            	window.scrollTo(0, currentScrollPosition - 50);
	            } else {
	            	window.scrollTo(0, currentScrollPosition + 50);
	            }
        	}
        });
    
        $('html, body').keydown(function (e) {
            var currentScrollPosition = window.pageYOffset;
            
            if ($("html").css("overflow-y") == "auto" && $("body").css("overflow-y") == "auto") {
	            switch (e.which) {
	                case 38: // up
	                	e.preventDefault(); // prevent the default action (scroll / move caret)
	                    window.scrollTo(0, currentScrollPosition - 120);
	                    break;
	    
	                case 40: // down
	                	e.preventDefault(); // prevent the default action (scroll / move caret)
	                    window.scrollTo(0, currentScrollPosition + 120);
	                    break;
	    
	                default: return; // exit this handler for other keys
	            }
            }
        });
    }
});