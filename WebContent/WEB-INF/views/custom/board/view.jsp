<%@page import="org.apache.velocity.runtime.directive.Include"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
${thisPost.title}<br>
${thisPost.contents}<br><br>

<c:forEach items="${pre_post}" var="post" varStatus="status">
	<c:choose>
		<c:when test="${thisPost.post_id < post.post_id}">
			이전 글 : 
		</c:when>
		<c:otherwise>
			다음 글 : 
		</c:otherwise>
	</c:choose>
	<a href="/custom/board/view?menu_idx=${menu_idx}&post_idx=${post.post_id}">${post.title}</a><br>
</c:forEach>