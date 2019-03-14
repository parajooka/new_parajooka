<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="navigation">
<ul class="navi-container">
	<li class="navi-logo">
		<a><img src="/resources/common/img/white-logo.png"></a>
	</li>
	<li class="navi">
		<a href="/jooka/admin/homepage/index">홈페이지 기본 설정</a>
	</li>
	<li class="navi">
		<a href="/jooka/admin/access_ip/index">접근 허용 아이피</a>
	</li>
	<li class="navi">
		<a href="/jooka/admin/category/index">관리자 메뉴 관리</a>
	</li>
	<c:forEach items="${admin_Category_list.keySet()}" var="key">
		<c:if test="${key == 0}">
				<c:forEach items="${admin_Category_list.get(key)}" var="parent_menu">
					<li class="navi">
						<a class="parent_menu">${parent_menu.category_name} <img class="admin_menu_open" src="/resources/common/img/menu_open.png"></a>
						<c:if test="${admin_Category_list.get(parent_menu.category_idx) != null}">
							<c:forEach items="${admin_Category_list.get(parent_menu.category_idx)}" var="child_menu">
								<div class="child_menu">
									<a href="${child_menu.category_url}">${child_menu.category_name}</a>
								</div>
							</c:forEach>
						</c:if>
					</li>
				</c:forEach>
		</c:if>
	</c:forEach>
</ul>
</div>