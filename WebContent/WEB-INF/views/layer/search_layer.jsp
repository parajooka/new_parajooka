<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${search_layout != null}">
<style>
	.search_layer {width: 100%;  background: #f1f1f1; margin: 0 0 50px; border: 0.5px solid #a7a7a7; padding: 10px 15px; display: table; font-size:14px;}
	.search_layer_box {width:100%; height: 100%; display: table-cell; vertical-align: middle;}
	.search_keyword {vertical-align: top; height:35px; width: 50%;}
	.search_items_select {height:35px; width: 27%;}
	.btn-darkblue {display:inline-block; width: 4vw; text-align: center; vertical-align: top; padding: 7px 0; margin-left: 1vw;}
</style>
<script type="text/javascript">
	var before_items = '${search_item}';
	var uri = '${paging.paging_url}';
	
	if (before_items.length > 0) {
		$(".search_items_select").val(before_items); 
	}

	$(".btn_search").on("click", function() {
		var keyword = $(".search_keyword").val();
		var search_item = $(".search_items_select").val();
		
		var url = uri + '' +$(location).attr('search');
		var condition_array = ['keyword', 'search_item', 'page'];
		
		for (var i = 0; i < condition_array.length; i++) {
			if (url.indexOf('&'+ condition_array[i]) >= 0) {
				var page_index = url.indexOf('&'+ condition_array[i]);
				var next_condition = url.indexOf('&', page_index + 1);
				var next_condition_bool = url.indexOf('&', page_index + 1) < 0 ? false : true;
				
				if (next_condition_bool) {
					var total_page_condition = url.substring(page_index, next_condition);
					url = url.replace(total_page_condition, ""); 
				} else {
					url = url.substring(0, url.indexOf('&'+ condition_array[i]) >= 0 ? url.indexOf('&'+ condition_array[i]) : url.length);
				}
				
			} else if (url.indexOf('?'+ condition_array[i]) >= 0) {
				var page_index = url.indexOf('?'+ condition_array[i]);
				var next_condition = url.indexOf('&', page_index + 1);
				var next_condition_bool = url.indexOf('&', page_index + 1) < 0 ? false : true;
				
				if (next_condition_bool) {
					var total_page_condition = url.substring(page_index + 1, next_condition + 1);
					url = url.replace(total_page_condition, ""); 
				} else {
					url = url.substring(0, url.indexOf('?'+ condition_array[i]) >= 0 ? url.indexOf('?'+ condition_array[i]) : url.length);
				}
			}
		}
		
		var search = url.substring(url.indexOf('?') + 1, url.length).length;
		var search_bool = (url.indexOf('?') < 0 || search.length < 1) ? false : true;
		
		var search_url;
		
		if (search_bool) {
			search_url = url + '&keyword='+ keyword +'&search_item='+ search_item +'&page=1';
		} else {
			search_url = $(location).attr('pathname') + '?keyword='+ keyword +'&search_item='+ search_item +'&page=1';
		}
			
		location.href = encodeURI(search_url);
	});
	
	$(".search_keyword").keydown(function(key) {
		if (key.keyCode == 13) {
			$(".btn_search").trigger("click");
		}
	});
</script>
</c:if>