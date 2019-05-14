$(document).ready(function(){
	$('.addMenu').on('click', function() {
		if (sel_node == null) {
			alert('추가하려는 메뉴의 부모를 선택해주세요.');
		} else if (sel_node.id == 0) {
       		$('#menu_name').val("");
       		$('#print_seq').val("0");
       		
       		$('#view_yn').val(0).prop("selected", true);
			
        	$('.insert_submit').show();
        	$('.edit_submit').hide();

    		$('#menu_idx').val(0);
    		$('#group_idx').val(0);
    		$('#parent_menu_idx').val(sel_node.id);
    		
    		$('.html_text').hide();
		} else {
       		$('#menu_name').val("");
       		$('#print_seq').val("0");
       		
       		$('#view_yn').val(0).prop("selected", true);
       		
        	$('.insert_submit').show();
        	$('.edit_submit').hide();

    		$('#menu_idx').val(0);
    		$('#parent_menu_idx').val(sel_node.id);
    		
    		$('.html_text').hide();
		}
	});
	
	$('.insert_submit').on('click', function() {
		if (sel_node == null) {
			alert('잘못된 접근입니다.');
		} else if (confirm("'"+ sel_node.name +"'아래에 '"+ $("#menu_name").val() +"'메뉴를 추가 하시겠습니까?")) {
			var form = $('.menu_editor_form');
			form.attr('action', '/jooka/admin/workboard/category/insert');
			
			form_submit(form);
		}
	});
	
	$('.edit_submit').on('click', function() {
		if (sel_node == null) {
			alert('잘못된 접근입니다.');
		} else if (confirm("'"+ sel_node.name +"'메뉴를 수정 하시겠습니까?\r\n수정 후에는 복구가 불가능합니다.")) {
			var form = $('.menu_editor_form');
			form.attr('action', '/jooka/admin/workboard/category/edit');
			
			form_submit(form);
		}
	})
	
	$('.deleteMenu').on('click', function() {
		if (sel_node == null) {
			alert("삭제할 메뉴를 선택해주세요.");
		} else if (confirm("'"+ sel_node.name +"'을(를) 삭제 하시겠습니까?\r\n삭제시 하위메뉴도 전체 삭제됩니다.")) {
			if (confirm("정말 삭제 하시겠습니까?\r\n삭제 후에는 복구가 불가능합니다.")) {
				var func = function(data) {
			    	var message = data['message'];
			    	var nextUrl = data['next_url'];
			    	
			    	alert(message);
			    	location.replace(nextUrl);
				}
				
				get_item_info("/jooka/admin/workboard/category/delete", func, {"menu_idx" : sel_node.id}, $('body'));
			}
		}
	});
});