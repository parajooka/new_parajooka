$(document).ready(function(){
	//허용아이피 뷰 테이블 높이 설정
	$('.not_found').css({
		"height" : $(window).height()/2
	});
	
	$('.acc_editor').dialog({
		autoOpen: false,
		appendTo : ".container",
		show: { effect: "blind", duration: 300 },
		hide: { effect: "blind", duration: 300 }
	});
	
	//등록 버튼
	$('.open_modal').on('click', function() {
		//hidden 전체 삭제
		var hidens = document.getElementsByClassName("id");
		$.each(hidens, function(index, value) {
			$(value).remove();
		});
		
		//수정버튼,삭제버튼 가리기
		$('.acc_edit_submit').css('display', 'none');
		$('.acc_delete_submit').css('display', 'none');
		//등록버튼 보이기
		$('.acc_insert_submit').css('display', 'inline-block');
		//입력창 초기화
		$('input').val('');
		//다이어로그 오픈
		$('.acc_editor').dialog("open");
	});
	
	//수정 버튼
	$(document).on('click', '.acc_edit', function() {
		//hidden 전체 삭제
		var hidens = document.getElementsByClassName("id");
		$.each(hidens, function(index, value) {
			$(value).remove();
		});
		
		var func = function(data) {
			var acc = data['object'];
			
			$('.acc_insert_form').append("<input type='hidden' class='id' name='id' id='id' value='"+ acc['id'] +"'>");
			$('#access_ip').val(acc['access_ip']);
			$('#authorizationer').val(acc['authorizationer']);
			
			//등록버튼 가리기
			$('.acc_insert_submit').css('display', 'none');
			
			//수정버튼,삭제버튼 보이기
			$('.acc_edit_submit').css('display', 'inline-block');
			$('.acc_delete_submit').css('display', 'inline-block');
			
			//다이어로그 오픈
			$('.acc_editor').dialog("open");
		}
		
		get_item_info("/jooka/admin/access_ip/get", func, {"id" : $(this).attr("data_id")}, $("body"));
	});
	
	$(document).on('click', '.acc_delete_submit', function() {
		if (confirm("허용 아이피를 삭제 하시겠습니까?\r\n삭제 후에는 복구할수 없습니다.")) {

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
			
			get_item_info("/jooka/admin/access_ip/delete", func, {"id" : id}, $("body"));
		}
	});
	
	$(document).on('click', '.acc_edit_submit', function() {
		if (confirm("허용 아이피를 수정 하시겠습니까?\r\n수정 후에는 복구할수 없습니다.")) {
			var frm = $('.acc_insert_form');
				frm.attr('action', '/jooka/admin/access_ip/edit');
			form_submit(frm);
		}
	});
	
	
	$(document).on('click', '.acc_insert_submit', function() {
		if (confirm("허용 아이피를 등록 하시겠습니까?")) {
			var frm = $('.acc_insert_form');
				frm.attr('action', '/jooka/admin/access_ip/insert');
				
			form_submit(frm);
		}
	});
	
	$('.show_more').on('click', function() {
		var func = function(data) {
			//서버에서 받아온 오브젝트
			var acc_list = data['object'];
			
			$.each(acc_list, function(index, value) {
				var acc = value;
				var numberic = document.getElementsByClassName("numberic");
				var number = parseInt($(numberic[numberic.length - 1]).text());
				
				$('.acc_view').append(
					"<tr>"+
						"<td class='numberic'>"+ (number + 1) +"</td>"+
						"<td><a class='acc_edit' data_id='"+ acc['id'] +"'>"+ acc['access_ip'] +"</a></td>"+
						"<td>"+ acc['authorizationer'] +"</td>"+
						"<td>"+ acc['auth_date'] +"</td>"+
					"</tr>"
				);
				
				if (acc_count <= number + 1) {
					$('.show_more').remove();
				}
			});
		}
		get_item_info("/jooka/admin/access_ip/more", func, {}, $('body'));
	});
	
	$(window).on("resize", function() {
		//허용아이피 뷰 테이블 높이 설정
		$('.not_found').css({
			"height" : $(window).height()/2
		});
	});
});