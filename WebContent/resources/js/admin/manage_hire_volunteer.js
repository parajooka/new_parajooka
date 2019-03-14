$(document).ready(function() {
	 function getAgoDate(resultDate)
	 {
	        year = resultDate.getFullYear();
	        month = resultDate.getMonth() + 1;
	        day = resultDate.getDate();
	        
	        if (month < 10)
	            month = "0" + month;
	        if (day < 10)
	            day = "0" + day;

	        return year + "-" + month + "-" + day;
	 }

	$(".hire_list").on("change", function() {
		var hire_id = $(this).val();
		
		if (hire_id != 0) {
			var func = function(data) {
				$(document.getElementsByClassName('temp_tr')).remove();
				$(".first_page").hide();
				
				var volunteers = data["object"][0];
				var volunteer_result = data["object"][1];
				
				if (volunteers.length < 1) {
					$(document.getElementsByClassName('temp_tr')).remove();
					$(".first_page").show();
					$(".first_page > td").html("해당 공고에 지원자가 존재하지 않습니다.");
				}
				
				$.each(volunteers, function(index, value){
					var v_result = volunteer_result[value["id"]];
					var status = '';
						if (v_result['result'] == 0) {
							status = '처리 대기중';
						} else if (v_result['result'] == 1) {
							status = '합격';
						} else if (v_result['result'] == 2) {
							status = '불합격';
						}
						
					var insert_date = getAgoDate(new Date(v_result['insert_date']));
					var modify_date = getAgoDate(new Date(v_result['insert_date']));
						
					
					$(".volunteer_table").append(
						"<tr class='temp_tr'>"+
							"<td>"+
								value['name'] +
							"</td>"+
							"<td>"+
								value['phone'] +
							"</td>"+
							"<td>"+
								status +
							"</td>"+
							"<td>"+
								insert_date +
							"</td>"+
							"<td>"+
								modify_date +
							"</td>"+
							"<td>"+
								"<a class='btn-orange' target='_blank' href='/jooka/admin/manage_hire_volunteer/survey?hire_id="+ v_result["hire_id"] +"&volunteer_id="+ value['id'] +"'>관리</a>" +
							"</td>"+
						"</tr>"
					);
					
				});
			}
			get_item_info("/jooka/admin/manage_hire_volunteer/getVolunteer", func, {"hire_id":hire_id}, $("body"));
		} else {
			$(document.getElementsByClassName('temp_tr')).remove();
			$(".first_page > td").html("지원자를 관리할 공고를 선택 해주세요.");
			$(".first_page").show();
		}
	});
});