var page = 1;
$(document).ready(function() {
	$('.menubar').remove();
	
	$('.create').click(function() {
		$('input[type=file]').val("")
		$(".preview").attr("src", '/resources/common/img/no-image.png');
		$('input[type=text]').val('1');
		$("#difficulty").val(3).prop("selected", true);
		
		$(window).scrollTop(0);
		$('.save').show();
		$('.save-edit').hide();
		$('.message').html('원하는 이미지를 첨부해주세요.');
		$('.inject-form').animate({
			left:0
		}, 800);
		
		$('.board-section').stop().animate({
			left : '-100%'
		},800);
	});
	
	$('.close_form').on('click', function() {
		//팝업창 원위치
		$('.inject-form').stop().animate({
			left : '100%'
		}, 800);
		
		$('.board-section').stop().animate({
			left : '0'
		}, 800);
	});
	
	$('.close_more').click(function() {
		$('.save-inject').hide();
		$('.save-show').show();
	});
	
	$('.show_more').click(function() {
		$('.save-inject').show();
		$('.save-show').hide();
	});
	
	$("#main_img").on("change", handleImgFileSelect);
	
	function handleImgFileSelect(e) {
        var files = e.target.files;
        var filesArr = Array.prototype.slice.call(files);

        filesArr.forEach(function(f) {
            if(!f.type.match("image.*")) {
                alert("확장자는 이미지 확장자만 가능합니다.");
                return;
            }

            sel_file = f;

            var reader = new FileReader();
            reader.onload = function(e) {
                $(".preview").attr("src", e.target.result);
            }
            reader.readAsDataURL(f);
        });
    }
	
	$('.more').click(function() {
		page ++;
		
		var func = function(data) {
			var data_list = data['object'];
			page = (data['message'] != null) ? parseInt(data['message']) : page;
			
			for (var i = 0; i < data_list.length; i++) {
				var obj = data_list[i];
				var query = '';
				var img = obj['img'];
				var diff = obj['difficulty'];
				var level = obj['level'];
				var id = obj['id'];
				
				if (i == data_list.length - 1) {
					query += '<div class="product-zone" style="display:inline-block; width: 15%; margin-left:1%;">';
				} else {
					query += '<div class="product-zone" style="display:inline-block; margin-right: 3%; margin-left:1%; width: 15%;">';
				}
				
					query += ''+
						'<div>'+
						'<img src="/res/img/contect/admin/'+ img +'" style="height: 180px; width: 100%;">'+
						'</div>'+
						'<div style="margin-bottom:40px;">'+
						'<a class="edit_btn" data="/contect/manage/call" data-type="'+ id +'">수정</a>&nbsp;'+
						'<a class="delete_btn" data="/contect/manage/delete?id='+ id +'">삭제</a>'+
						'<a style="float: right;">등급 : '+ diff +', 레벨 : '+ level +'</a>'+
						'</div>';
					
					$('.board-section').append(query);
					
			}
		}
		
		get_item_info('/contect/manage/more', func, {'page':page}, $('.board-section'));
	});
	
	$('.save').click(function() {
		$('.input_data').attr('action', '/contect/manage/data/save');
		data_submit($('.input_data'));
	});
	
	$(document).on("click",".edit_btn",function() { 
		var url = $(this).attr('data');
		var id = $(this).attr('data-type');
		
		var func = function(data) {
			var obj = data['object'];
			$('#id').val(obj['id']);
			$(".preview").attr("src", "/res/img/contect/admin/"+ obj['img']);
			$("#difficulty").val(obj['difficulty']).prop("selected", true);
			$('#level').val(obj['level']);
			$('.message').html('이미지를 수정하시려면 파일을 첨부해주세요.');
			$('.save').hide();
			$('.save-edit').show();
			
			$(window).scrollTop(0);
			
			$('.inject-form').animate({
				left:0
			}, 800);
			
			$('.board-section').stop().animate({
				left : '-100%'
			},800);
		}
		
		get_item_info(url, func, {'id':id}, $('.inject-form'));
	});
	
	$('.save-edit').click(function() {
		$('.input_data').attr('action', '/contect/manage/data/edit');
		data_submit($('.input_data'));
	});

	$(document).on("click",".delete_btn",function() { 
		var url = $(this).attr('data');
		if (confirm("정말로 삭제 하시겠습니까?")) {
			if (confirm("삭제 후 복구할 수 없습니다.\r\n삭제 하시겠습니까?")) {
				delete_item(url);
			}
		}
	});
});