<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    
<link rel="stylesheet" type="text/css" href="/resources/css/admin/hire.css" />
<script>
	var hire_count = '${hire_count}';
	var now = '${now}';
	var tomorrw = '${tomorrow}';
</script>
<script type="text/javascript" src="/resources/common/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script src="/resources/js/admin/hire.js"></script>
<div class="hire_container">
	<span class="hire_count">등록된 공고 갯수 : ${hire_count}개</span>
	<span class="hire_count" style="float: right;">진행중인 공고 갯수 : ${pr_hire}개</span>
	<table class="hire_view">
		<c:if test="${hire_count <= 0}">
			<tr>
				<td class="not_found">
					등록된 채용공고가 존재하지 않습니다.
				</td>
			</tr>
		</c:if>
		<c:if test="${hire_count > 0}">
			<tr>
				<td>
					순번
				</td>
				<td>
					공고명
				</td>
				<td>
					본인인증 사용 여부
				</td>
				<td>
					면담 사용 여부
				</td>
				<td>
					공고 시작일
				</td>
				<td>
					공고 종료일
				</td>
			</tr>
			<c:forEach items="${hires}" var="hire" varStatus="status">
			<tr>
				<td class="numberic">${status.count}</td>
				<td>
					<a class="hire_edit open_modal" data_id="${hire.id}">${hire.title}</a>
				</td>
				<td>
					<c:choose>
						<c:when test="${hire.phone_certified == 0}">
							사용
						</c:when>
						<c:otherwise>
							미사용
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:choose>
						<c:when test="${hire.interview == 0}">
							사용
						</c:when>
						<c:otherwise>
							미사용
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					${hire.getKrDate(hire.start_date)}
				</td>
				<td>
					${hire.getKrDate(hire.end_date)}
				</td>
			</tr>
			</c:forEach>
		</c:if>
	</table>
	<p class="btn_section">
		<a class="open_modal">신규 등록</a>
	</p>
</div>
${paging_layout}
<div class="hire_editor" title="채용 공고 추가 및 수정">
	<form:form cssClass="hire_insert_form" modelAttribute="hire">
		<table class="modal_table">
			<tr>
				<td>
					공고 제목
				</td>
				<td>
					<form:input path="title" cssStyle="width:100%;"/>
				</td>
			</tr>
			<tr>
				<td>
					공고 정보
				</td>
				<td style="width: 650px;">
					<form:textarea path="info"/>
				</td>
			</tr>
			<tr>
				<td>
					채용 인원
				</td>
				<td>
					<form:input path="personnel" cssClass="only_number"/>
				</td>
			</tr>
			
			<tr>
				<td>
					채용 시작일
				</td>
				<td>
					<form:input path="start_date" cssClass="hire_date" readonly="true" placeholder="채용 시작일을 선택해주세요." cssStyle="font-size:0.8vw; width:12vw;"/>
				</td>
			</tr>
			
			<tr>
				<td>
					채용 종료일
				</td>
				<td>
					<form:input path="end_date" cssClass="hire_date" readonly="true" placeholder="시작일을 먼저 선택 해주세요." cssStyle="font-size:0.8vw; width:12vw;"/>
				</td>
			</tr>
			
			<tr>
				<td>
					본인인증 사용 여부
				</td>
				<td>
					<form:select path="phone_certified">
						<form:option value="0">
							사용
						</form:option>
						<form:option value="1">
							사용안함
						</form:option>
					</form:select>
				</td>
			</tr>
			<tr>
				<td>
					면담 사용 여부
				</td>
				<td>
					<form:select path="interview">
						<form:option value="0">
							사용
						</form:option>
						<form:option value="1">
							사용안함
						</form:option>
					</form:select>
				</td>
			</tr>
			
			<tr>
				<td>
					포트폴리오 여부
				</td>
				<td>
					<form:select path="portfolio">
						<form:option value="0">
							필수
						</form:option>
						<form:option value="1">
							선택
						</form:option>
						<form:option value="2">
							사용안함
						</form:option>
					</form:select>
				</td>
			</tr>
			<tr>
				<td>
					성별 수집 여부
				</td>
				<td>
					<form:select path="gender_use">
						<form:option value="0">
							필수
						</form:option>
						<form:option value="1">
							선택
						</form:option>
						<form:option value="2">
							사용안함
						</form:option>
					</form:select>
				</td>
			</tr>
			<tr>
				<td>
					이메일 수집 여부
				</td>
				<td>
					<form:select path="email_use">
						<form:option value="0">
							필수
						</form:option>
						<form:option value="1">
							선택
						</form:option>
						<form:option value="2">
							사용안함
						</form:option>
					</form:select>
				</td>
			</tr>
			<tr>
				<td>
					주소 수집 여부
				</td>
				<td>
					<form:select path="address_use">
						<form:option value="0">
							필수
						</form:option>
						<form:option value="1">
							선택
						</form:option>
						<form:option value="2">
							사용안함
						</form:option>
					</form:select>
				</td>
			</tr>
			<tr>
				<td>
					공고 활성화 여부
				</td>
				<td>
					<form:select path="activation">
						<form:option value="0">
							활성화
						</form:option>
						<form:option value="1">
							비활성화
						</form:option>
					</form:select>
					
					<span style="font-size: 0.8vw; display: inline-block; margin-left:1vw;">비활성화시 진행중인 공고이더라도 나타나지 않습니다.</span>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="dialog_submit">
					<a class="hire_insert_submit dialog_submit_btn">등록</a>
					<a class="hire_edit_submit dialog_submit_btn" style="display: none;">수정</a>
					<a class="hire_delete_submit dialog_submit_btn" style="display: none; margin-top: 1px;">삭제</a>
				</td>
			</tr>
		</table>
	</form:form>
</div>