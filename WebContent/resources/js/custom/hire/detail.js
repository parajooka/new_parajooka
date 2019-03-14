$(document).ready(function() {
	var minute = $(".minute");
	var end_date = minute.attr("data_value");
	
	var timers = function() {
		var func = function(data) {
			minute.html(data["message"]);
		}
		
		$.ajax({
		    type:"POST",
		    cache : false,
		    url:"/custom/hire/getTime",
		    data:{"end_date" : end_date},
		    success : function(data) {
		       	func(data);
	        },
	        error: function(request,status,error){
	        	if (request.status == 0) {
					alert('서버 통신에 실패하였습니다.\r\n관리자에게 문의해주세요.');
				//서버 통신 에러가 아니라면 에러번호 메세지 띄우기
				} else {
	    			alert("Error Name : "+ request.responseText +", Error Code : "+ request.status +"\r\n관리자에게 문의 해주세요.");
				}
	        }
		});
	}
	
	timers();
	
	setInterval(function() {
		timers();
	}, 1000);
});