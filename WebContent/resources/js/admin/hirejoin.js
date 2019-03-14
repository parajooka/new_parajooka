/**
 * 
 */

var edit_btn;

$(document).ready(function() {
	$(".paging_section").hide();
	
	/* 추가 버튼 활성화 및 비활성화 함수 */
	edit_btn = function() {
		var edit_layout = document.getElementsByClassName("question_status");
		
		$.each(edit_layout, function(index, value) {
			var q_id = $(value).attr("data_id");
			
			if (document.getElementsByClassName(q_id).length > 0) {
				$(value).html("<span style='font-size:0.8vw;'>이미 등록됨</span");
			} else {
				$(value).html("<input type='button' class='add_qnh' data_id='"+ q_id +"' value='추가'>")
			}
		});
	}
	
	/* 공고에 문항을 추가하는 버튼을 클릭한 경우 */
	$(document).on("click", ".add_qnh", function() {
		var question_id = $(this).attr("data_id");
		var hire_id = $(".hire_list").val();
		
		var func = function(data) {
			var message = data['message'];
			alert(message);
			
			if (data['next_url'] == null) {
				$(".hire_list").change();
			}
		}
		
		get_item_info("/jooka/admin/hirejoin/insert", func, {"hire_id":hire_id, "question_id":question_id}, $("body"));
	});
	
	$(".more_btn").on("click", function() {
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
				
				$('.question_table').append(
					"<tr>"+
						"<td>"+ question['content'] +"</td>"+
						"<td data_id='"+ question['id'] +"' class='question_status'></td>"+
					"</tr>"
				);
				
				if (question_count <= $(document.getElementsByClassName("question_status")).length) {
					$('.more_btn').remove();
				}
				
				$(".hire_list").change();
			});
		}
		
		get_item_info("/jooka/admin/question/more", func, {}, $('body'));
	});
	
	/* 공고를 선택한 경우 */
	$(".hire_list").on("change", function() {
		if ($(this).val() != 0) {
			var func = function(data) {
				var message = data['message'];
				
				if (message != null) {
					alert(message);
				} else {
					$(".paging_section").show();
					$.each($(".page_number"), function(index, value) {
						
						var h = $(value).attr("href");
						
						if (h.indexOf("&hire_id") >= 0) {
							var hire_id_index = h.indexOf("&hire_id");
							h = h.substring(0, hire_id_index);
						}
						
						$(value).attr("href", h + "&hire_id=" + $('.hire_list').val());
					});
					
					$(".select_question_section").show();
					var questions = data['object'];
					
					$(document.getElementsByClassName("question_td")).remove();
					
					if (questions == null || typeof questions == "undefined" || questions.length == 0) {
						$('.hire_question_table').append(
							"<tr class='question_td'>"+
								"<td style='text-align:center; border-bottom:none !important;' colspan='2' >"+
									"<span style='display:inline-block; padding:1vw 0; width:100%; margin-bottom:0.5vw; border-top:3px solid #6799FF; border-bottom:3px solid #6799FF;'>해당 공고에는 등록된 문제가 존재하지 않습니다.</span>"+
								"</td>"+
							"</tr>"
						);
					} else {
						$('.hire_question_table').append(
								"<tr class='question_td'>"+
									"<td style='text-align:center;' colspan='2' >"+
										"<span style='display:inline-block; padding:1vw 0; width:100%; margin-bottom:0.5vw; border-top:3px solid #6799FF; border-bottom:3px solid #6799FF;'>선택한 공고에 등록된 문제 리스트</span>"+
									"</td>"+
								"</tr>"
						);
					}
					
					$.each(questions, function(index, question) {
						$('.hire_question_table').append(
							"<tr class='question_td'>"+
								"<td style='padding:0.5vw 0; border-bottom:1px solid #BDBDBD;'>"+
									"<span class='question_title'>" + question["content"] + "</span>"+
								"</td>"+
								"<td style='border-bottom:1px solid #BDBDBD;'>"+
									"<input type='button' data_id='"+ question["id"] +"' class='question_remove "+ question["id"] +"' value='삭제'>"+
								"</td>"+
							"</tr>"
						);
					});
					
					edit_btn();
				}
			}
			
			get_item_info("/jooka/admin/hirejoin/get_qeustion", func, {"id":$(this).val()}, $("body"));
		} else {
			$(".paging_section").hide();
			$(".select_question_section").hide();
			$(document.getElementsByClassName("question_td")).remove();
		}
	});
	
	if (typeof hire_idx != "undefined" && hire_idx != null && hire_idx.length > 0) {
		$(".hire_list").val(hire_idx);
		$(".hire_list").change();
		
		var this_url = location.href; 
		
		if (this_url.indexOf("&hire_id") >= 0) {
			var hire_id_index = this_url.indexOf("&hire_id");
			this_url = this_url.substring(0, hire_id_index);
			history.pushState(null, null, this_url);
		}
	}
	
	/* 공고에 등록된 문항을 삭제하는 경우 */
	$(document).on("click", ".question_remove", function() {
		if (confirm("해당 문항을 선택한 공고에서 제외 하시겠습니까?")) {
			var question_id = $(this).attr("data_id");
			var hire_id = $(".hire_list").val();
			
			var func = function(data) {
				var message = data['message'];
				alert(message);
				
				$(".hire_list").change();
			}
			
			get_item_info("/jooka/admin/hirejoin/delete", func, {"hire_id":hire_id, "question_id":question_id}, $("body"));
		}
	});
});