// textArea에 이미지 첨부
var oEditors = [];
function pasteHTML(filepath){
    var sHTML = '<img src="'+ filepath +'">';
    oEditors.getById["info"].exec("PASTE_HTML", [sHTML]);
}

$(document).ready(function(){
	//전역변수선언
     
    nhn.husky.EZCreator.createInIFrame({
        oAppRef: oEditors,
        elPlaceHolder: "info",
        sSkinURI: "/resources/common/smarteditor/SmartEditor2Skin.html",
        htParams : {
            // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseToolbar : true,            
            // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseVerticalResizer : true,    
            // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
            bUseModeChanger : true,
        },
        fOnAppLoad: function () {    
            $("iframe").css("width", "100%").css("height", "254px");
        }
    });
    
    //전송버튼 클릭이벤트
    $("#savebutton").click(function(){
        //id가 smarteditor인 textarea에 에디터에서 대입
    	oEditors.getById["info"].exec("UPDATE_CONTENTS_FIELD", []);
         
        // 이부분에 에디터 validation 검증
         
        //폼 submit
        $("#frm").submit();
    })

	//허용아이피 뷰 테이블 높이 설정
	$('.not_found').css({
		"height" : $(window).height()/2
	});
	
	$('.hire_editor').dialog({
		autoOpen: false,
		appendTo : ".container",
		show: { effect: "blind", duration: 300 },
		hide: { effect: "blind", duration: 300 }
	});
	
	$(window).on("keydown",function(event) {
		if (event.keyCode == 27) {
			$(".ui-dialog-titlebar-close").trigger("click");
		}
	});
	
	$(".hire_date").datepicker({
		  altFormat: "yy-mm-dd",
		  dateFormat: "yy-mm-dd",
		  prevText: '이전 달',
	      nextText: '다음 달',
	      monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	      monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	      dayNames: ['일', '월', '화', '수', '목', '금', '토'],
	      dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
	      dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
	      showMonthAfterYear: true,
	      yearSuffix: '년'
	});
	
	$("#start_date").datepicker( "option", "minDate", new Date(now));
	
	
	$("#start_date").on("change", function() {
		var start_date = new Date($(this).val());
		start_date.setDate(start_date.getDate() + 1);
		$("#end_date").datepicker( "option", "minDate", start_date);
	});
	
	$("#end_date").on("click", function() {
		if ($("#start_date").val() < 1 || $("#start_date").val().length < 1) {
			$("#end_date").datepicker("option", "closeText", "Close");
		}
	});
	
	//등록 버튼
	$('.open_modal').on('click', function() {
		//hidden 전체 삭제
		var hidens = document.getElementsByClassName("id");
		$.each(hidens, function(index, value) {
			$(value).remove();
		});
		
		//수정버튼,삭제버튼 가리기
		$('.hire_edit_submit').css('display', 'none');
		$('.hire_delete_submit').css('display', 'none');
		//등록버튼 보이기
		$('.hire_insert_submit').css('display', 'inline-block');
		//입력창 초기화
		$('input').val('');
		$('textarea').val('');
		$("select").val("0");
		
		$(".only_number").val(0);
		
		$("#start_date").val(now).change();
		$("#end_date").val(tomorrw);
		
		//다이어로그 오픈
		$('.hire_editor').dialog("open");
		
		oEditors.getById["info"].exec("CHANGE_EDITING_MODE", ["WYSIWYG"]);
		oEditors.getById["info"].exec("RESET_TOOLBAR"); 
		oEditors.getById["info"].exec("SET_IR", [""]); //내용초기화
	});
	
	//수정 버튼
	$(document).on('click', '.hire_edit', function() {
		//hidden 전체 삭제
		var hidens = document.getElementsByClassName("id");
		$.each(hidens, function(index, value) {
			$(value).remove();
		});
		
		var func = function(data) {
			var hire = data['object'];
			
			$('.hire_insert_form').append("<input type='hidden' class='id' name='id' id='id' value='"+ hire['id'] +"'>");
			$('#title').val(hire['title']);
			oEditors.getById["info"].exec("PASTE_HTML", [hire['info']]);
			$('#personnel').val(hire['personnel']);
			$('#start_date').val(data['message']);
			$('#end_date').val(data['next_url']);
			$('#phone_certified').val(hire['phone_certified']);
			$('#interview').val(hire['interview']);
			$('#portfolio').val(hire['portfolio']);
			$('#gender_use').val(hire['gender_use']);
			$('#email_use').val(hire['email_use']);
			$('#address_use').val(hire['address_use']);
			$('#activation').val(hire['activation']);
			
			//등록버튼 가리기
			$('.hire_insert_submit').css('display', 'none');
			
			//수정버튼,삭제버튼 보이기
			$('.hire_edit_submit').css('display', 'inline-block');
			$('.hire_delete_submit').css('display', 'inline-block');
			
			//다이어로그 오픈
			$('.hire_editor').dialog("open");
		}
		
		get_item_info("/jooka/admin/hire/get", func, {"id" : $(this).attr("data_id")}, $("body"));
	});
	
	$(document).on('click', '.hire_delete_submit', function() {
		if (confirm("공고를 삭제 하시겠습니까?\r\n삭제 후에는 복구할수 없습니다.")) {
			if (confirm("공고 삭제시 해당 공고와 관련된 정보는 일괄 삭제처리됩니다.\r\n정말 삭제 하시겠습니까?")) {
				var func = function(data) {
			    	var message = data['message'];
			    	var nextUrl = data['next_url'];
			    	
			    	alert(message);
			    	
			    	if (nextUrl != null) {
			    		location.replace(nextUrl);
			    	} else {
			    		location.reload();
			    	}
				}
				
				var id = $(document.getElementsByClassName("id")[0]).val();
				
				delete_item("/jooka/admin/hire/delete", {"id" : id}, $("body"));
			}
		}
	});
	
	$(document).on('click', '.hire_edit_submit', function() {
		oEditors.getById["info"].exec("UPDATE_CONTENTS_FIELD", [])
		if (confirm("공고를 수정 하시겠습니까?\r\n수정 후에는 복구할수 없습니다.")) {
			var frm = $('.hire_insert_form');
				frm.attr('action', '/jooka/admin/hire/update');
			form_submit(frm);
		}
	});
	
	
	$(document).on('click', '.hire_insert_submit', function() {
		oEditors.getById["info"].exec("UPDATE_CONTENTS_FIELD", [])
		if (confirm("공고를 등록 하시겠습니까?")) {
			var frm = $('.hire_insert_form');
				frm.attr('action', '/jooka/admin/hire/insert');
				
			form_submit(frm);
		}
	});
	
	$('.show_more').on('click', function() {
		var func = function(data) {
			//서버에서 받아온 오브젝트
			var hire_list = data['object'];
			
			$.each(hire_list, function(index, value) {
				var hire = value;
				var numberic = document.getElementsByClassName("numberic");
				var number = parseInt($(numberic[numberic.length - 1]).text());
				var phone;
				var i;
				
				if (hire['phone_certified'] == 0) {
					phone = '사용';
				} else {
					phone = '미사용';
				}
				
				if (hire['interview'] == 0) {
					i = '사용';
				} else {
					i = '미사용';
				}
				
				
				$('.hire_view').append(
					"<tr>"+
						"<td class='numberic'>"+ (number + 1) +"</td>"+
						"<td><a class='hire_edit' data_id='"+ hire['id'] +"'>"+ hire['title'] +"</a></td>"+
						"<td>"+ phone +"</td>"+
						"<td>"+ i +"</td>"+
						"<td>"+ new Date(hire['start_date']).toISOString().slice(0,10) +"</td>"+
						"<td>"+ new Date(hire['end_date']).toISOString().slice(0,10) +"</td>"+
					"</tr>"
				);
				
				if (hire_count <= number + 1) {
					$('.show_more').remove();
				}
			});
		}
		
		get_item_info("/jooka/admin/hire/more", func, {}, $('body'));
	});
	
	$(window).on("resize", function() {
		//허용아이피 뷰 테이블 높이 설정
		$('.not_found').css({
			"height" : $(window).height()/2
		});
	});
});