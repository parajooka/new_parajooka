// textArea에 이미지 첨부
var oEditors = [];
function pasteHTML(filepath){
    var sHTML = '<img src="'+ filepath +'">';
    oEditors.getById["example"].exec("PASTE_HTML", [sHTML]);
}

$(document).ready(function(){
	//전역변수선언
     
    nhn.husky.EZCreator.createInIFrame({
        oAppRef: oEditors,
        elPlaceHolder: "example",
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
    	oEditors.getById["example"].exec("UPDATE_CONTENTS_FIELD", []);
         
        // 이부분에 에디터 validation 검증
         
        //폼 submit
        $("#frm").submit();
    })

	//허용아이피 뷰 테이블 높이 설정
	$('.not_found').css({
		"height" : $(window).height()/2
	});
	
	$('.question_editor').dialog({
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
	
	//등록 버튼
	$('.open_modal').on('click', function() {
		//hidden 전체 삭제
		var hidens = document.getElementsByClassName("id");
		var blocks = document.getElementsByClassName("plust_answer_block");
		
		$(hidens).remove();
		$(blocks).remove();
		
		//수정버튼,삭제버튼 가리기
		$('.question_edit_submit').css('display', 'none');
		$('.question_delete_submit').css('display', 'none');
		//등록버튼 보이기
		$('.question_insert_submit').css('display', 'inline-block');
		//입력창 초기화
		$('input[type=text]').val('');
		$('input[id=answer_content]').attr("name", "answer_1_content");
		$('input[type=checkbox]').prop('checked', false).attr("name", "answer_1");
		$("#answer_count").val(1);
		
		answer_count = 1;
		
		//다이어로그 오픈
		$('.question_editor').dialog("open");
		

		oEditors.getById["example"].exec("CHANGE_EDITING_MODE", ["WYSIWYG"]);
		oEditors.getById["example"].exec("RESET_TOOLBAR"); 
		oEditors.getById["example"].exec("SET_IR", [""]); //내용초기화
	});
	
	//수정 버튼
	$(document).on('click', '.question_edit', function() {
		//hidden 전체 삭제
		
		answer_count = 0;
		$("#answer_count").val(answer_count);
		
		var hidens = document.getElementsByClassName("id");
		$.each(hidens, function(index, value) {
			$(value).remove();
		});
		
		var func = function(data) {
			var question = data['object'];
			var answers = question['answers'];
			
			$('.question_insert_form').append("<input type='hidden' class='id' name='id' id='id' value='"+ question['id'] +"'>");
			$('#content').val(question['content']);
			oEditors.getById["example"].exec("PASTE_HTML", [question['example']]);
			$('#type').val(question['type']);
			
			$.each(answers, function(index, answer) {
				if (index == 0) {
					var check_box = $("input[name=answer_1]");
					var content_box = $("input[name=answer_1_content]");
					
					check_box.attr("name", "answer_"+ answer["id"]);
					content_box.attr("name", "answer_"+ answer["id"] +"_content").val(answer["content"]);
					
					if (answer["reply"] == 0) {
						check_box.prop("checked", true);
					}
				} else {
					$(".submit_section").before(
						"<tr class='plust_answer_block'>"+
							"<td style='text-align: center;'>"+
								"<input type='checkbox' value='0' name='answer_"+ answer["id"] +"'/>"+
							"</td>"+
							"<td>"+
								"<input type='text' style='width: 90%;' name='answer_"+ answer["id"] +"_content' value='"+ answer["content"] +"'/>"+
								"<input type='button' value='삭제' data_id='"+ answer["id"] +"' class='db_delete_answer' style='font-size:0.6vw; margin-left:5px;'/>"+
							"</td>"+
						"<tr>"
					);
					
					if (answer["reply"] == 0) {
						$(document.getElementsByName("answer_"+ answer["id"])).prop("checked", true);
					}
				}
			});
			
			//등록버튼 가리기
			$('.question_insert_submit').css('display', 'none');
			
			//수정버튼,삭제버튼 보이기
			$('.question_edit_submit').css('display', 'inline-block');
			$('.question_delete_submit').css('display', 'inline-block');
			
			//다이어로그 오픈
			$('.question_editor').dialog("open");
		}
		
		get_item_info("/jooka/admin/question/get", func, {"id" : $(this).attr("data_id")}, $("body"));
	});
	
	$(document).on("click", '.db_delete_answer', function() {
		if (confirm("정말 해당 답안을 삭제 하시겠습니까?\r\n삭제 후에는 복원 할 수 없습니다.")) {
			
			$(this).parent().parent().remove();
			
			var func = function(data) {
				alert(data['message']);
			}
			
			get_item_info("/jooka/admin/question/delete_answer", func, {"id":$(this).attr("data_id")}, $("body"));
		}
	});
	
	$(document).on('click', '.question_delete_submit', function() {
		if (confirm("질문(문제)를 삭제 하시겠습니까?\r\n삭제 후에는 복구할수 없습니다.")) {
			if (confirm("질문(문제) 삭제시 해당 문제와 관련된 답안은 일괄 삭제처리됩니다.\r\n정말 삭제 하시겠습니까?")) {
				var id = $(document.getElementsByClassName("id")[0]).val();
				
				delete_item("/jooka/admin/question/delete", {"id" : id}, $("body"));
			}
		}
	});
	
	$(document).on('click', '.question_edit_submit', function() {
		oEditors.getById["example"].exec("UPDATE_CONTENTS_FIELD", [])
		if (confirm("질문(문제)를 수정 하시겠습니까?\r\n수정 후에는 복구할수 없습니다.")) {
			var frm = $('.question_insert_form');
				frm.attr('action', '/jooka/admin/question/update');
			form_submit(frm);
		}
	});
	
	
	$(document).on('click', '.question_insert_submit', function() {
		oEditors.getById["example"].exec("UPDATE_CONTENTS_FIELD", [])
		if (confirm("질문(문제)를 등록 하시겠습니까?")) {
			var frm = $('.question_insert_form');
				frm.attr('action', '/jooka/admin/question/insert');
				
			form_submit(frm);
		}
	});
	
	$('.show_more').on('click', function() {
		var func = function(data) {
			//서버에서 받아온 오브젝트
			var question_list = data['object'];
			
			$.each(question_list, function(index, value) {
				var question = value;
				var numberic = document.getElementsByClassName("numberic");
				var number = parseInt($(numberic[numberic.length - 1]).text());
				var types = '';
				
				if (question["type"] == 0) {
					types = '객관식';
				} else {
					types = '주관식';
				}
				
				$('.question_view').append(
					"<tr>"+
						"<td class='numberic'>"+ (number + 1) +"</td>"+
						"<td><a class='question_edit' data_id='"+ question['id'] +"'>"+ question['content'] +"</a></td>"+
						"<td>"+ types +"</td>"+
					"</tr>"
				);
				
				if (question_count <= number + 1) {
					$('.show_more').remove();
				}
			});
		}
		
		get_item_info("/jooka/admin/question/more", func, {}, $('body'));
	});
	
	var answer_count = 1;
	
	$(".plust_answer").on("click", function() {
		answer_count ++;
		$("#answer_count").val(answer_count);
		
		$(".submit_section").before(
			"<tr class='plust_answer_block'>"+
				"<td style='text-align: center;'>"+
					"<input type='checkbox' value='0' name='answer_"+ answer_count +"'/>"+
				"</td>"+
				"<td>"+
					"<input type='text' style='width: 90%;' name='answer_"+ answer_count +"_content'/>"+
					"<input type='button' value='삭제' class='delete_answer' style='font-size:0.6vw; margin-left:5px;'/>"+
				"</td>"+
			"<tr>"
		);
	});
	
	$(document).on('click', '.delete_answer', function() {
		answer_count --;
		$("#answer_count").val(answer_count);
		$(this).parent().parent().remove();
	});
	
	
	$(window).on("resize", function() {
		//허용아이피 뷰 테이블 높이 설정
		$('.not_found').css({
			"height" : $(window).height()/2
		});
	});
});