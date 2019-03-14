$(document).ready(function(){
	function handleImgFileSelect(e) {
        var files = e.target.files;
        var filesArr = Array.prototype.slice.call(files);

        filesArr.forEach(function(f) {
            if(!f.type.match("image.*")) {
                alert("확장자는 이미지 확장자만 가능합니다.");
                return;
            }
            /*
            sel_file = f;

            var reader = new FileReader();
            reader.onload = function(e) {
                $(".preview").attr("src", e.target.result);
            }
            reader.readAsDataURL(f);*/
        });
    }
	
	$("#main_img").on("change", handleImgFileSelect);
	
	$('.homepage_insert').on('click',function() {
		if (confirm("허용 아이피를 등록 하시겠습니까?")) {
			var frm = $('#homepage_form');
				frm.attr('action', '/jooka/admin/homepage/insert');
				
			data_submit(frm);
		}
	});
	
	$('.homepage_edit').on('click',function() {
		if (confirm("허용 아이피를 수정 하시겠습니까?")) {
			var frm = $('#homepage_form');
				frm.attr('action', '/jooka/admin/homepage/edit');
				
			data_submit(frm);
		}
	});
});