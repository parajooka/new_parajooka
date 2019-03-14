<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<link rel="stylesheet" type="text/css" href="/resources/css/custom/hire/hire_list.css" />
<script  src="/resources/js/custom/hire/hire_list.js"></script>
<p class="hire_title">${homepage.name} 채용 공고</p>
<table class="hire_list_table">
  <tr>
    <td>순번</td>
    <td>공고명</td>
    <td>채용 인원</td>
    <td>지원자</td>
    <td>종료일</td>
    <td></td>
  </tr>
  <c:forEach items="${hires}" var="hire" varStatus="status">
  <tr>
  	<td class="numberic">${status.count}</td>
  	<td class="title"><a href="/custom/hire/detail?id=${hire.id}">${hire.title}</a></td>
  	<td class="hire_personnel">
  		<c:if test="${hire.personnel == 0}">
  			상시 모집
  		</c:if>
  		<c:if test="${hire.personnel != 0}">
  			${hire.personnel}명
  		</c:if>
  	</td>
  	<td class="survey">${counts.get(hire.id)}명</td>
  	<td class="hire_end_date">${hire.getKrDate(hire.end_date)}</td>
  	<td><a class="btn-darkblue hire-survey" href="/custom/hire/detail?id=${hire.id}">상세보기</a></td>
  </tr>
  </c:forEach>
</table>
<!-- 
<div class="my_hire_show_section">
	<a class="btn-orange open_modal">내 지원기록 보기</a>
</div>

<div class="search_my_history" title="공고 지원기록 조회">
	<div class="seacrh_section">
		<input type="text" name="phone" id="phone" placeholder="당신의 연락처를입력 해주세요.">
		<p style="text-align: center;"><a class="btn-orange">조회</a></p>
	</div>
</div> -->
    