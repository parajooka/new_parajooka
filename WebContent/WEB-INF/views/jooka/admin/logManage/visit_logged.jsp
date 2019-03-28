<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input type="text" id="logged_calendar" style="outline: none; cursor: pointer; font-size: 1.5vw; border: 3px solid black; border-right: none; border-left: none; width: 100%; margin-bottom: 1vw; padding: 0.5vw 0 0.5vw 0.5vw;" readonly="readonly" value="날짜 선택 (클릭)">
페이지 뷰 카운트 : <span class="view_count">${logged_line}</span>회
<iframe id="logged_frame" style="width:99.5%; height: 500px; box-shadow: inherit;" src="/res/logged/para-jooka/logged_${logged_today}.txt">
</iframe>
<script>
$(document).ready(function() {
	$( "#logged_calendar").datepicker({
		dateFormat: 'yy-mm-dd',
        prevText: '이전 달',
        nextText: '다음 달',
        monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNames: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        showMonthAfterYear: true,
        yearSuffix: '년',
        onSelect : function() {
        	var func = function(data) {
				//잘모된 페이지 요청시
				if (data['message'] != null) {
					alert(data['message']);
					if (data['next_url'] != null) {
						location.href = data['next_url'];
					}
					return;
				}
				$("#logged_frame").attr("src", "/res/logged/para-jooka/logged_"+ $("#logged_calendar").val() +".txt")
				$(".view_count").html(data['object']);
			}
        	
        	get_item_info("/jooka/admin/logManage/validLoggedDate", func, {"logged_date":$("#logged_calendar").val()}, $('.nob'));
        }
    });
});
</script>