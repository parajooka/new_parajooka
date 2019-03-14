/*=========================================================ajax관련*/

/**
 * 모달창전용 (로딩마스크 O)
 * 특정 선택한 아이템의 정보를 불러들일때 사용
 * url과 통신 후 함수는 각 페이지에서 구현후 arg로 줘야한다.
 */
function get_item_info(url, func, data_array, form) {
	start_loading(form);
	$.ajax({
	    type:"POST",
	    cache : false,
	    url:url,
	    data:data_array,
	    success : function(data) {
	    	end_loading();
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


/**
 * form submit담당 함수 // ajax로 submit하고 이동할 url과 결과값을 받아온다.
 * (새로운 정보 등록 // 기존 정보 수정)
 * @param form
 * @returns
 */
function form_submit(form) {
	start_loading(form);
	//arg로 넘어온 form의 데이터를 문자열로 변환
	var data = $(form).serialize() ;
	//폼에서 전송하려는 타겟을 url로 지정
	var url = $(form).attr('action');
	$.ajax({
	    type:"POST",
	    //Internet Explorer에서 캐시를 자기 마음대로 사용하는것때문에 캐시를 false로 지정
	    cache : false,
	    data : data,
	    url:url,
	    success : function(data) {
	    	end_loading();
	    	try {
	    		data = eval(data);
	    	} catch (e) {
	    		alert(e.message + ", Code : 500 \r\n관리자에게 문의 해주세요.");
	    	    return;
	    	}
	    	//요청한 작업이 정상처리되었는지 예외가 발생했는지를 확인한다.
	    	//true = 정상처리, false = 예외발생
		    if (data['processing_result']) {
		    	
		    	//서버에서 지정한 메세지
		    	var message = data['message'];
		    	//서버에서 지정한 다음 페이지 url (만약 400에러 등 객체 생성과정에서 에러가 발생했다면 값 = null) 
		    	var nextUrl = data['next_url'];
		    	
		    	//에러가 발생했든 정상처리 되었든 메세지를 뿌려준다.
		    	alert(message);
		    	
		    	//정상처리라면 다음 url로 화면을 이동시킨다.
		    	if (nextUrl != null && nextUrl != "N") {
		    		location.replace(nextUrl);
		    	}
		    } else {
		    	//서버에서 요청 처리과정 중 예외가 발생한 경우
		    	alert("Error Code : 500 \r\n관리자에게 문의 해주세요.");
		    	//페이지를 새로고침한다.
		    	location.reload();
		    }
        },
        //ajax통신 실패
        error: function(request,status,error) {
    			if (request.status == 0) {
    				alert('서버 통신에 실패하였습니다.\r\n관리자에게 문의해주세요.');
    			//서버 통신 에러가 아니라면 에러번호 메세지 띄우기
    			} else {
        			alert("Error Name : "+ error +", Error Code : "+ request.status +"\r\n관리자에게 문의 해주세요.");
    			}
		    	location.reload();
        }
	  });
}

function data_submit(form) {
	start_loading(form);
	form.ajaxForm({
		url: $(form).attr('action'),
		enctype: "multipart/form-data", // 여기에 url과 enctype은 꼭 지정해주어야 하는 부분이며 multipart로 지정해주지 않으면 controller로 파일을 보낼 수 없음
		success: function(data){
	    	end_loading();
			try {
	    		data = eval(data);
	    	} catch (e) {
	    		alert(e.message + ", Code : 500 \r\n관리자에게 문의 해주세요.");
	    	    return;
	    	}
	    	//요청한 작업이 정상처리되었는지 예외가 발생했는지를 확인한다.
	    	//true = 정상처리, false = 예외발생
		    if (data['processing_result']) {
		    	
		    	//서버에서 지정한 메세지
		    	var message = data['message'];
		    	//서버에서 지정한 다음 페이지 url (만약 400에러 등 객체 생성과정에서 에러가 발생했다면 값 = null) 
		    	var nextUrl = data['next_url'];
		    	
		    	//에러가 발생했든 정상처리 되었든 메세지를 뿌려준다.
		    	alert(message);
		    	//정상처리라면 다음 url로 화면을 이동시킨다.
		    	if (nextUrl != null && nextUrl != "N") {
		    		location.replace(nextUrl);
		    	}
		    } else {
		    	//서버에서 요청 처리과정 중 예외가 발생한 경우
		    	alert("Error Code : 500 \r\n관리자에게 문의 해주세요.");
		    	//페이지를 새로고침한다.
		    	location.reload();
		    }
        },
        //ajax통신 실패
        error: function(request,status,error) {
    			if (request.status == 0) {
    				alert('서버 통신에 실패하였습니다.\r\n관리자에게 문의해주세요.');
    			//서버 통신 에러가 아니라면 에러번호 메세지 띄우기
    			} else {
        			alert("Error Name : "+ error +", Error Code : "+ request.status +"\r\n관리자에게 문의 해주세요.");
    			}
		    	location.reload();
        }
	});
	
	form.submit();
}

/**
 * 삭제 전용 ajax 함수 // 이동할 url과 삭제 결과값을 받아온다.
 * @param url
 * @returns
 */
function delete_item(url, data_array, form) {
	start_loading(form);
	$.ajax({
	    type:"POST",
	    cache : false,
	    url:url,
	    data:data_array,
	    success : function(data) {
	    	end_loading();
	    	
	    	//삭제요청이 정상처리 되었는지 체크
	    	//true = 정상처리, false = 예외발생
	    	try {
	    		data = eval(data);
	    	} catch (e) {
	    		alert(e.message + ", Code : 500 \r\n관리자에게 문의 해주세요.");
	    	    return;
	    	}
	    	
	    	if (data['processing_result']) {		    	
		    	//서버에서 지정한 메세지와 다음페이지 url을 변수에 담는다.
		    	var message = data['message'];
		    	var nextUrl = data['next_url'];
		    	
		    	//메세지를 띄워준다.
		    	alert(message);
		    	//페이지를 서버에서 지정한 url로 덮어씌운다.
		    	var nextUrl = data['next_url'];
		    	
		    	if (nextUrl != null && nextUrl != "N") {
		    		location.replace(nextUrl);
		    	}
	    	} else {
	    		//삭제처리중 서버에서 예외가 발생한 경우.
	    		alert("Error Code : 500 \r\n관리자에게 문의 해주세요.");
		    	location.reload();
	    	}
        },
        //ajax 통신 실패시
        error: function(request,status,error){
        	//에러 내용과 코드를 사용자에게 보여주고 메세지를 띄워준다.
        	//서버 통신 에러인지 체크
			if (request.status == 0) {
				alert('서버 통신에 실패하였습니다.\r\n관리자에게 문의해주시기 바랍니다.');
			//서버 통신 에러가 아니라면 에러번호 메세지 띄우기
			} else {
    			alert("Error Name : "+ error +", Error Code : "+ request.status +"\r\n관리자에게 문의 해주세요.");
			}
		     location.reload();
        }
	});
}
/*========================================================= ajax관련*/