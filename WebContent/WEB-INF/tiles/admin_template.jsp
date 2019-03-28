<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<tiles:insertAttribute name="header" />
</head>
<body onload="end_loading();" style="position: relative;">
	<tiles:insertAttribute name="left" />
	<div class="container">
		<div class="section">
			<div class="obj-title">
				${menu}
			</div>
			<!-- 본문 :: START -->
			<tiles:insertAttribute name="body" />
			<!-- 본문 :: END -->
			
			<!-- 검색 레이어 :: START -->
			<tiles:insertAttribute name="search" />
			<!-- 검색 레이어 :: END -->
			
			<!-- 페이징 레이어 :: START -->
			<tiles:insertAttribute name="pages" />
			<!-- 페이징 레이어 :: END -->
			
			<!-- 하단 :: START -->
			<tiles:insertAttribute name="footer" />
			<!-- 하단 :: END -->
		</div>
	</div>
</body>
</html>