<%@page import="org.apache.velocity.runtime.directive.Include"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
	.menu_board_container {width:90%; margin:auto; display: table; margin-top:50px;}
	.menu_board_section {width:100%; height:100%; display: table-cell;}
	.menu_board_section table {width:100%;}
	
	.dark_table tr td:first-child {width: 5%;}
	.dark_table tr td:nth-child(2) {width: 75%;}
	.dark_table tr td:nth-child(3) {width: 13%;}
	.dark_table tr td:nth-child(4) {width: 7%;}
	.btn-gray {display: inline-block; padding: 0.5vw 1.5vw; margin-top: 1vw;}
</style>
<c:choose>
	<c:when test="${menu_type == 2}">
		<div class="menu_board_container">
			<div class="menu_board_section">
				<table class="dark_table">
					<tr>
						<td>
							순번
						</td>
						<td>
							제목
						</td>
						<td>
							작성일
						</td>
						<td>
							조회수
						</td>
					</tr>
					<c:forEach items="${post_list}" var="post" varStatus="status">
					<tr>
						<td>
							${paging.numberic + status.index}
						</td>
						<td>
							<a href="/custom/board/view?menu_idx=${menu_idx}&post_idx=${post.post_id}">${post.title}</a>
						</td>
						<td>
							${post.write_date}
						</td>
						<td>
							${post.view_count}
						</td>
					</tr>
					</c:forEach>
				</table>
				<c:if test="${admin == true}">
					<div style="text-align: right">
						<a class="btn-gray" href="/custom/board/write?menu_idx=${menu_idx}">글 작성</a>
					</div>
				</c:if>
				${paging_layout}
				${search_layout}
			</div>
		</div>
	</c:when>
	<c:when test="${menu_type == 3}">
	
	</c:when>
	<c:when test="${menu_type == 4}">
	
	</c:when>
</c:choose>