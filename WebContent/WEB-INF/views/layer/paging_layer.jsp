<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test='${paging_layout != null && paging_layout.length() > 0}'>
<script type='text/javascript'>
	var uri = '${paging.paging_url}';
	var now_page = ${paging.page};
	$(document).ready(function() {
		var url = uri + '' +$(location).attr('search');
		
		if (url.indexOf('&page') >= 0) {
			//&page다음에 조건문이 또 있는지 체크해야함
			var page_index = url.indexOf('&page');
			var next_condition = url.indexOf('&', page_index + 1);
			var next_condition_bool = url.indexOf('&', page_index + 1) < 0 ? false : true;
			
			if (next_condition_bool) {
				var total_page_condition = url.substring(page_index, next_condition);
				url = url.replace(total_page_condition, ""); 
			} else {
				url = url.substring(0, url.indexOf('&page') >= 0 ? url.indexOf('&page') : url.length);
			}
			
		} else if (url.indexOf('?page') >= 0) {
			var page_index = url.indexOf('?page');
			var next_condition = url.indexOf('&', page_index + 1);
			var next_condition_bool = url.indexOf('&', page_index + 1) < 0 ? false : true;
			
			if (next_condition_bool) {
				var total_page_condition = url.substring(page_index + 1, next_condition + 1);
				url = url.replace(total_page_condition, ""); 
			} else {
				url = url.substring(0, url.indexOf('?page') >= 0 ? url.indexOf('?page') : url.length);
			}
		}
		
		var search = url.substring(url.indexOf('?') + 1, url.length).length;
		var search_bool = (url.indexOf('?') < 0 || search.length < 1) ? false : true;
		var total_paging = '${paging.total_page}';
		
		$.each($('.paging_before_arrow'), function(index, value) {
			if (search_bool) {
				$(value).attr('href', url + '&page=1');
			} else {
				$(value).attr('href', $(location).attr('pathname') + '?page=1');
			}
			
		});

		$.each($('.paging_next_arrow'), function(index, value) {
			if (search_bool) {
				$(value).attr('href', url + '&page=' + total_paging);
			} else {
				$(value).attr('href', $(location).attr('pathname') + '?page=' + total_paging);
			}
			
		});
		
		$.each($('.paging_btn'), function(index, value) {
			if (search_bool) {
				$(value).attr('href', url + '&page=' + $(value).html());
			} else {
				$(value).attr('href', $(location).attr('pathname') + '?page=' + $(value).html());
			}
			
			if (now_page == parseInt($(value).html())) {
				$(value).css("background", "#4f4f4f");
			}
		});
	});
	
	var numberic = document.getElementsByClassName('numberic');
	var page = ${paging.page * paging.limit - (paging.limit - 1)};
	
	for (var i = 0; i < numberic.length; i++) {
		$(numberic[i]).html(page + i);
	}
	
	var pager = $('.page_number');
	var arrow = $('.page_arrow');
	var tmp_href;
	
	$.each(pager, function(index, value){
		tmp_href = $(value).attr('href');
		
		$(value).attr('href', encodeURI(tmp_href));
	});
	
	$.each(arrow, function(index, value){
		tmp_href = $(value).attr('href');
		
		$(value).attr('href', encodeURI(tmp_href));
	});
</script>
</c:if>
