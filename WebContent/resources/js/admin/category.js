$(document).ready(function(){
	$('.addCategory').on('click', function() {
		if (sel_node == null) {
			alert('추가하려는 메뉴의 부모를 선택해주세요.');
		} else if (sel_node.id == 0) {
       		$('#category_name').val("");
       		$('#print_seq').val("0");
       		
       		$('#view_yn').val(0).prop("selected", true);
			
        	$('.insert_submit').show();
        	$('.edit_submit').hide();

    		$('#category_idx').val(0);
    		$('#group_idx').val(0);
    		$('#parent_category_idx').val(sel_node.id);
    		$("#category_url").val("");
    		
    		$('.html_text').hide();
		} else {
       		$('#category_name').val("");
       		$('#print_seq').val("0");
       		
       		$('#view_yn').val(0).prop("selected", true);
       		
        	$('.insert_submit').show();
        	$('.edit_submit').hide();

    		$('#category_idx').val(0);
    		$('#parent_category_idx').val(sel_node.id);
    		$("#category_url").val("");
    		
    		$('.html_text').hide();
		}
	});
	
	$('.insert_submit').on('click', function() {
		if (sel_node == null) {
			alert('잘못된 접근입니다.');
		} else if (confirm("'"+ sel_node.name +"'아래에 '"+ $("#category_name").val() +"'메뉴를 추가 하시겠습니까?")) {
			var form = $('.category_editor_form');
			form.attr('action', '/jooka/admin/category/insert');
			
			form_submit(form);
		}
	});
	
	$('.edit_submit').on('click', function() {
		if (sel_node == null) {
			alert('잘못된 접근입니다.');
		} else if (confirm("'"+ sel_node.name +"'메뉴를 수정 하시겠습니까?\r\n수정 후에는 복구가 불가능합니다.")) {
			var form = $('.category_editor_form');
			form.attr('action', '/jooka/admin/category/edit');
			
			form_submit(form);
		}
	})
	
	$('.deleteCategory').on('click', function() {
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
				
				get_item_info("/jooka/admin/category/delete", func, {"category_idx" : sel_node.id}, $('body'));
			}
		}
	});
	
	$('.html_save').on('click', function() {
		if (sel_node == null) {
			alert("잘못된 접근입니다.");
		} else if (confirm("html을 수정 하시겠습니까?\r\n수정후에는 복구할수 없습니다.")) {
			var func = function(data) {
		    	var message = data['message'];
		    	var nextUrl = data['next_url'];
		    	
		    	alert(message);
		    	location.replace(nextUrl);
			}
			
			get_item_info("/jooka/admin/category/html/edit", func, {"category_idx" : sel_node.id, "category_html" : editor.getValue()}, $('body'));
		}
	});
	
	$('.mobile_html_save').on('click', function() {
		if (sel_node == null) {
			alert("잘못된 접근입니다.");
		} else if (confirm("html을 수정 하시겠습니까?\r\n수정후에는 복구할수 없습니다.")) {
			var func = function(data) {
		    	var message = data['message'];
		    	var nextUrl = data['next_url'];
		    	
		    	alert(message);
		    	location.replace(nextUrl);
			}
			
			get_item_info("/jooka/admin/category/mobile_html/edit", func, {"category_idx" : sel_node.id, "mobile_category_html" : mobile_editor.getValue()}, $('body'));
		}
	});
});