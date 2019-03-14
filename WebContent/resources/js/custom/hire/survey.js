function sample6_execDaumPostcode() {
	var daum_opener = new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var fullAddr = ''; // 최종 주소 변수
            var extraAddr = ''; // 조합형 주소 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                fullAddr = data.roadAddress;

            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                fullAddr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
            if(data.userSelectedType === 'R'){
                //법정동명이 있을 경우 추가한다.
                if(data.bname !== ''){
                    extraAddr += data.bname;
                }
                // 건물명이 있을 경우 추가한다.
                if(data.buildingName !== ''){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('post').value = data.zonecode; //5자리 새우편번호 사용
            document.getElementById('add1').value = fullAddr;

            // 커서를 상세주소 필드로 이동한다.
            document.getElementById('add2').focus();
        }
    }).open();
}
$(document).ready(function() {
	$(".auth_send_btn").on("click", function() {
		var func = function(data) {
			if (data['next_url'] == "fail") {
				alert(data["message"]);
			} else if (data['next_url'] == "already") {
				alert(data["message"]);
				$("#phone").attr("readonly", true);
				$(".auth_section").show();
			} else {
				alert(data["message"]);
				$("#phone").attr("readonly", true);
				$(".auth_section").show();
			}
		}
		
		get_item_info("/custom/hire/send_sms", func, {"phone" : $("#phone").val(), "hire_id" : $("#hire_id").val()}, $("body"));
	});
	
	$(".check_auth").on("click", function() {
		var func = function(data) {
			if (data['next_url'] != null) {
				alert(data["message"]);
				$("#auth_checked").val(data['next_url']);
				$(".auth_send_btn").remove();
				$(".auth_section").remove();
				$("#phone").attr("readonly", true);
			} else {
				alert(data["message"]);
			}
		}
		
		get_item_info("/custom/hire/check_auth", func, {"auth_number" : $("#auth_number").val(), "hire_id" : $("#hire_id").val()}, $("body"));
	});
	
	$('#email').mailtip({
		mails: ['naver.com', 'gmail.com', 'daum.net', 'nate.com', 'yahoo.com', 'msn.com']
	});
	
	$(".survey_submit").on("click", function() {
		if (confirm("공고 지원서를 제출 하시겠습니까?")) {
			var address = $("#add1").val() + " " + $("#add2").val();
			$("#address").val(address);
			
			var form = $(".survey_detail_layout");
			form.attr("action", "/custom/hire/send_survey");
			
			data_submit(form);
		}
	});
});