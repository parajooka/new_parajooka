<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    
<link rel="stylesheet" type="text/css" href="/resources/css/admin/homepage.css" />
<script src="/resources/js/admin/homepage.js"></script>
<form:form id="homepage_form" method="post" modelAttribute="homepage" enctype="multipart/form-data">
	<table class="info_table">
		<tr>
			<td>
				홈페이지 명
			</td>
			<td>
				<form:input path="name"/>
			</td>
			<td>
				<a class="homepage_input_info">홈페이지의 메인 이름을 입력해주세요.</a>
			</td>
		</tr>
		<tr>
			<td>
				파비콘(favicon)
			</td>
			<td>
				<input type="file" name="favicon_img" id="favicon_img"><br><br>${homepage.favicon}
			</td>
			<td>
				<a class="homepage_input_info">홈페이지를 대표하는 파비콘 이미지를 등록해주세요.</a>
			</td>
		</tr>
		<tr>
			<td>
				홈페이지 인덱스 경로
			</td>
			<td>
				<form:input path="main_url"/>
			</td>
			<td>
				<a class="homepage_input_info">홈페이지 도메인을 치고 들어올경우 이동될 페이지의 내부 경로를 입력해주세요.<br>ex)/custom/landing/index</a>
			</td>
		</tr>
		<tr>
			<td>
				홈페이지 정보
			</td>
			<td>
				<form:input path="info"/>
			</td>
			<td>
				<a class="homepage_input_info">모바일에서 홈페이지 링크 첨부시 나타나는 간단한 홈페이지 정보를 입력해주세요.</a>
			</td>
		</tr>
		<tr>
			<td>
				로고 이미지
			</td>
			<td>
				<input type="file" name="logo_img" id="logo_img"><br><br>${homepage.logo}
			</td>
			<td>
				<a class="homepage_input_info">홈페이지 대표 로고 이미지를 등록해주세요.</a>
			</td>
		</tr>
		<tr>
			<td>
				모바일 버전 사용 여부
			</td>
			<td>
				<form:select path="mobile_use">
					<form:option value="0">미사용</form:option>
					<form:option value="1">사용</form:option>
				</form:select>
			</td>
			<td>
				<a class="homepage_input_info">모바일버전을 사용시 사용, 반응형 사이트일경우 미사용을 선택해주세요.</a>
			</td>
		</tr>
		<tr>
			<td>
				대표 메일
			</td>
			<td>
				<form:input path="mail"/>
			</td>
			<td>
				<a class="homepage_input_info">회사 대표 메일을 입력해주세요.</a>
			</td>
		</tr>
		<tr>
			<td>
				대표 번호
			</td>
			<td>
				<form:input path="phone"/>
			</td>
			<td>
				<a class="homepage_input_info">회사 대표 번호를 입력해주세요.</a>
			</td>
		</tr>
		<tr>
			<td>
				팩스 번호
			</td>
			<td>
				<form:input path="fax"/>
			</td>
			<td>
				<a class="homepage_input_info">회사의 팩스번호를 입력해주세요.</a>
			</td>
		</tr>
		<tr>
			<td>
				주소지
			</td>
			<td>
				<form:input path="address"/>
			</td>
			<td>
				<a class="homepage_input_info">회사의 주소지를 입력해주세요.</a>
			</td>
		</tr>
		<tr>
			<td>
				대표자명
			</td>
			<td>
				<form:input path="representative"/>
			</td>
			<td>
				<a class="homepage_input_info">회사의 대표자명을 입력해주세요.</a>
			</td>
		</tr>
	</table>
</form:form>

<c:choose>
	<c:when test="${homepage.name == null}">
		<a class="homepage_insert submot_btn btn-darkblue">등록</a>
	</c:when>
	<c:otherwise>
		<a class="homepage_edit submot_btn btn-darkblue">수정</a>
	</c:otherwise>
</c:choose>
