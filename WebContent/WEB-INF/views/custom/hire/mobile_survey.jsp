<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<link rel="stylesheet" type="text/css" href="/resources/common/css/mobile_basic.css" />
<link rel="stylesheet" type="text/css" href="/resources/common/css/mailtip.css" />
<link rel="stylesheet" type="text/css" href="/resources/css/custom/hire/survey.css" />
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script  src="/resources/common/js/jquery.mailtip.js"></script>
<script type="text/javascript" src="/resources/common/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script src="/resources/common/js/mobile_basic.js"></script>
<script  src="/resources/js/custom/hire/survey.js"></script>
<p class="survey_title">${hire.title}</p>
<form class="survey_detail_layout" method="post" enctype="multipart/form-data">
	<input type="hidden" name="hire_id" id="hire_id" value="${hire.id}">
	<table class="survey_detail_table">
		<tr>
			<td>
				성명
			</td>
			<td>
				<input style="width: 15vw;" type="text" name="name" id="name" placeholder="본인의 이름을 입력 해주세요.">
				<a class="example_info">필수사항 (반드시 입력 해야합니다.)</a>
			</td>
		</tr>
		<c:if test="${hire.phone_certified == 0}">	
			<tr>
				<td>
					본인인증
				</td>
				<td>
					<input type="text" style="width: 14vw;" placeholder="연락처를 입력 해주세요." id="phone" name="phone">
					<input type="hidden" name="auth_checked" id="auth_checked" value="0">
					<a class="btn-orange auth_send_btn" style="font-size: 0.8vw; padding:0.3vw 0.5vw;">인증요청</a>
					<a class="example_info">필수사항 (반드시 입력 해야합니다.)</a>
					<p style="padding-top: 10px;" class="auth_section">
						<input type="text" name="auth_number" id="auth_number" placeholder="인증번호를 입력 해주세요.">
						<a class="btn-orange check_auth" style="font-size: 0.8vw; padding:0.3vw 0.5vw;">인증</a>
					</p>
				</td>
			</tr>
		</c:if>
		<c:if test="${hire.phone_certified != 0}">
			<tr>
				<td>
					연락처
				</td>
				<td>
					<input type="text" style="width: 14vw;" placeholder="연락처를 입력 해주세요." id="phone" name="phone">
					<a class="example_info">필수사항 (반드시 입력 해야합니다.)</a>
				</td>
			</tr>
		</c:if>
		<c:if test="${hire.gender_use == 0 || hire.gender_use == 1}">
			<tr>
				<td>
					성별
				</td>
				<td>
					<select name="gender" id="gender">
						<option value="-1">성별을 선택 해주세요.</option>
						<option value="0">남</option>
						<option value="1">여</option>
					</select>
					
					<c:choose>
						<c:when test="${hire.gender_use == 0}">
							<a class="example_info">필수사항 (반드시 선택 해야합니다.)</a>
						</c:when>
						<c:otherwise>
							<a class="example_info">선택사항 (선택하지 않아도 무관합니다.)</a>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:if>
		<c:if test="${hire.email_use == 0 || hire.email_use == 1}">
			<tr>
				<td>
					이메일
				</td>
				<td>
					<input type="text" style="width: 15vw;" name="email" id="email" placeholder="이메일 주소">
					
					<c:choose>
						<c:when test="${hire.email_use == 0}">
							<a class="example_info">필수사항 (반드시 입력 해야합니다.)</a>
						</c:when>
						<c:otherwise>
							<a class="example_info">선택사항 (입력하지 않아도 무관합니다.)</a>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:if>
		<c:if test="${hire.address_use == 0 || hire.address_use == 1}">
			<tr>
				<td>
					주소
				</td>
				<td>
					<input type="text" name="post" id="post" readonly="readonly" onclick="sample6_execDaumPostcode()" placeholder="우편번호"> <input class="btn_add" type="button" onclick="sample6_execDaumPostcode()" value="우편번호 찾기">
					<p style="padding-top: 5px;">
						<input type="text" id="add1" style="width: 16vw;" placeholder="주소"> <input type="text" id="add2" style="width: 16vw;" placeholder="상세주소">
						<input type="hidden" id="address" name="address">
						<c:choose>
							<c:when test="${hire.address_use == 0}">
								<a class="example_info">필수사항 (반드시 입력 해야합니다.)</a>
							</c:when>
							<c:otherwise>
								<a class="example_info">선택사항 (입력하지 않아도 무관합니다.)</a>
							</c:otherwise>
						</c:choose>
					</p>
				</td>
			</tr>
		</c:if>
		<c:if test="${hire.portfolio == 0 || hire.portfolio == 1}">
			<tr>
				<td>
					포트폴리오
				</td>
				<td>
					<input type="file" name="portfolio_files" id="portfolio_files">
					<c:choose>
						<c:when test="${hire.portfolio == 0}">
							<a class="example_info">필수사항 (반드시 첨부 해야합니다.)</a>
						</c:when>
						<c:otherwise>
							<a class="example_info">선택사항 (첨부하지 않아도 무관합니다.)</a>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:if>
	</table>
	<div class="question_section">
		<c:forEach items="${questions}" var="question">
			<div class="question_box">
				<div class="question_content">
					${question.content}
				</div>
				<c:if test="${question.example != null && question.example.length() > 0}">
					<div class="question_example">
						${question.example}
					</div>
				</c:if>
				<c:if test="${question.type == 0}">
					<c:forEach items="${question.answers}" var="answer">
						<div class="answer_section">
							<input type="checkbox" id="answer_${answer.id}" name="answer_${question.id}" style="cursor: pointer;" value="${answer.id}"> <label style="cursor: pointer; vertical-align: top;" for="answer_${answer.id}">${answer.content}</label>
						</div>
					</c:forEach>
				</c:if>
				<c:if test="${question.type == 1}">
					<c:forEach items="${question.answers}" var="answer">
						<div class="answer_section">
							<textarea id="${question.id}_open_answer" name="${question.id}_open_answer" style="font-size:4vw; background-color: white; width: 100%; min-height: 200px;"></textarea>
						</div>
					</c:forEach>
				</c:if>
			</div>
		</c:forEach>
	</div>
	<div style="margin-top: 1vw;">
		개인정보 수집 및 이용 동의
		<textarea class="user_info_acc" readonly="readonly" >
■ 수집하는 개인정보 항목
회사는 회원가입, 상담, 서비스 신청 등등을 위해 아래와 같은 개인정보를 수집하고 있습니다.
ο 수집항목 : 이름 ,  성별 ,  자택 주소 , 휴대전화번호 , 이메일 , 서비스 이용기록 , 접속 로그 , 접속 IP 정보
ο 개인정보 수집방법 : 홈페이지(채용 공고 지원) , 서면양식

■ 개인정보의 수집 및 이용목적

회사는 수집한 개인정보를 다음의 목적을 위해 활용합니다.

ο 채용 공고에 해당하는 정보를 수집하여 회사에 적합한 인재를 찾는 목적

■ 개인정보의 보유 및 이용기간

회사는 개인정보 수집 및 이용목적이 달성된 후에는 예외 없이 해당 정보를 지체 없이 파기합니다.



■ 개인정보의 위탁처리

"(주)파라앤주카"는 서비스 향상을 위해 관계법령에 따라 회원의 동의를 얻거나 관련 사항을 공개 또는 고지 후 회원의 개인정보를 외부에 위탁하여 처리하고있습니다.

"(주)파라앤주카"는 개인정보처리 수탁자와 그업무의 내용은 다음과 같습니다.

- 수탁자 : (주)누리고

- 위탁 업무 내용 :  문자메세지 발송


# 개인정보의 위탁처리

"(주)파라앤주카"는 서비스 향상을 위해 관계법령에 따라 회원의 동의를 얻거나 관련 사항을 공개 또는 고지 후 회원의 개인정보를 외부에 위탁하여 처리하고 있습니다.

"(주)파라앤주카"의 개인정보 처리 수탁자와 그 업무 내용은 다음과 같습니다.

- 수탁자 : (주)누리고

- 위탁업무내용 : 본인인증 문자 메세지 발송 및 채용 공고 합격 문자 발송

- 수집항목 : 휴대폰번호

- 보유기간 : 채용 공고 종료 혹은 삭제시 혹은 법정 보유기간
		</textarea>
	<p class="acc_question">
		개인정보 수집 및 이용에 동의하십니까? <input type="checkbox" name="info_acc" value="1"> 이용동의
	</p>
	</div>
	<p class="survey_btn">
		<a class="btn-darkblue survey_submit">제출하기</a>
	</p>
</form>
