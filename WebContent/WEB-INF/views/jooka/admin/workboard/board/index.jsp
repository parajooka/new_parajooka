<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link rel="stylesheet" type="text/css" href="/resources/css/admin/menu.css" />
<link rel="stylesheet" type="text/css" href="/resources/common/css/layout-tree.css" />
<link rel="stylesheet" type="text/css" href="/resources/common/css/jqtree.css" />
<style>
#search_box {width:53% !important; margin-left: 21% !important;}
.select_post_contents img {max-width:100%;}
</style>
<script src="/resources/common/js/jqTreeContextMenu.js"></script>
<script src="/resources/common/js/tree.jquery.js"></script>
<script src="/resources/common/js/tree_controll.js"></script>
<script src="/resources/common/ace/src-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/resources/common/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript">
var editor;
var mobile_editor;
var sel_node = null; //선택된 노드가 담길 변수
var g_id = '';	
var parent_node = null; //선택 메뉴의 최상위 부모가 담길 변수
var full_path = null; // 전체 경로

var before_node = '${before_menu_idx}';
$(function() {
/*===============================트리 정보 ajax로 호출 및 트리 생성*/
$.ajax({
	    type:"GET",
	    cache : false,
	    url:'${admin_root_path}/workboard/board/bytree',
	    success : function(data) {
	    	var menu_list = data['object'];
			var source = []; //전체 트리를 담을 변수
	        var items = [];  //노드 덩어리를 담을 변수
	        
	        search_result = menu_list.length - 1;
	        
	        for (var i = 0; i < menu_list.length; i++) { //json데이터 for
	            var menu = menu_list[i];
	            var parent_menu_id = menu['parent_menu_idx'];
	            var id = menu['menu_idx'];
	            var title = menu['menu_name'];

	            if (items[parent_menu_id]) { //현재 메뉴의 부모idx를 key로 가지는 item이 있다면
	                var item = //아이템을 생성하기 시작한다.
	                {
	                	id: id,
	                	name: title
	                };

	                if (!items[parent_menu_id].children) { //item의 자식이 비어있다면
	                    items[parent_menu_id].children = [];//현재 메뉴가 들어갈 자리를 만들어준다. (배열, 다음 자식들도 들어 올거임 최초 리셋?)
	                }

	                items[parent_menu_id].children[items[parent_menu_id].children.length] = item; //자식으로 자기 자신을 집어놓는다.
	                items[id] = item;
	            } else { //현재 메뉴의 부모idx를 key로 가지는 item이 없다면 (자기의 상위가 없다는 말이된다.)
	                items[id] = //자신의 아이디를 key값으로 하고
	                {
	                	id: id, //자신의 정보를 트리에 넣는다.
	                    name: title //자신의 정보를 트리에 넣는다.
	                };
	              	//전체 트리의 일부로 추가.
	                source[0] = items[id];
	            }
	        }
	        
	        //jqtree생성함수 호출 시작
	        	var $tree = setjQtree(source);
	        		$tree.tree({
	        			dragAndDrop : false
	        		});
	        //jqtree생성함수 호출 종료
	        
	            
	        /**
	         * 노드 클릭(선택) 이벤트
	         */	
	        	$tree.on(
	        	    'tree.select',
	        	    function(event) {
	        	        if (event.node) {
	        	            //노드가 선택 되어있을경우.
	        	            var node = event.node;
	        	            if (event.node.id > 0) {
		        	            sel_node = node;
	        	            	location.href = "${admin_root_path}/workboard/board/index?menu_idx="+ sel_node.id;
	        	            } else {
	        	            	$tree.tree('selectNode', null);
				            	$tree.tree('selectNode', sel_node);
				    			$("#menu_idx").val(sel_node.id);
	        	            }
	        	        }
        	        	//노드가 해제된 경우
	        	        else {
	        	        	location.href = "${admin_root_path}/workboard/board/index";
	        	        }
	        	    });
	        
	        	if (typeof before_node != "undefined" && before_node.length > 0) {
	        		sel_node = $tree.tree('getNodeById', parseInt(before_node));
	        		
	        		$('#tree1').tree('addToSelection', sel_node);
	        	}
        	}
	  });
});	
</script>
<script src="/resources/js/admin/workmenu.js"></script>
<script>
var oEditors = [];
var post_mode = "insert";
function pasteHTML(filepath){
    var sHTML = '<img src="'+ filepath +'">';
    oEditors.getById["post_contents"].exec("PASTE_HTML", [sHTML]);
}

$(document).ready(function() {
	nhn.husky.EZCreator.createInIFrame({
        oAppRef: oEditors,
        elPlaceHolder: "post_contents",
        sSkinURI: "/resources/common/smarteditor/SmartEditor2Skin.html",
        htParams : {
            // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseToolbar : true,            
            // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseVerticalResizer : true,    
            // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
            bUseModeChanger : true,
        },
        fOnAppLoad: function () {    
            $("iframe").css("width", "100%").css("height", "254px");
        }
    });
	
	$('.post_editor').dialog({
		autoOpen: false,
		appendTo : ".container",
		resizable : false
	});
	
	$(".post_password").dialog({
		autoOpen: false,
		appendTo : ".container",
		resizable : false
	});
	
	$(".post_view").dialog({
		autoOpen: false,
		appendTo : ".container",
		resizable : false
	});
	
	function viewPost(post) {
		$(".post_password").dialog("close");
		$('.post_view').dialog("open");
		$(".select_post_title").html(post['post_title']);
		$(".select_post_contents").html(post['post_contents']);
	}
	
	function editPost(post) {
		$(".post_password").dialog("close");
		$(".post_view").dialog("close");
		
		post_mode = "modify";
		
		$("#post_title").val(post['post_title']);
		$("#writter").val(post['writter']);
		$("#post_id").val(post['post_id']);
		$("#post_password").val("");
		$("#menu_idx").val(post['menu_idx']);
		
		$('.post_editor').dialog("open");
		
		oEditors.getById["post_contents"].exec("CHANGE_EDITING_MODE", ["WYSIWYG"]);
		oEditors.getById["post_contents"].exec("RESET_TOOLBAR"); 
		oEditors.getById["post_contents"].exec("SET_IR", [""]); //내용초기화
		oEditors.getById["post_contents"].exec("PASTE_HTML", [post['post_contents']]);
		
	}
	
	$(".new_post").on("click", function() {
		post_mode = "insert";

		$(".post_password").dialog("close");
		$(".post_view").dialog("close");
		$('.post_editor').dialog('open');
		
		oEditors.getById["post_contents"].exec("CHANGE_EDITING_MODE", ["WYSIWYG"]);
		oEditors.getById["post_contents"].exec("RESET_TOOLBAR"); 
		oEditors.getById["post_contents"].exec("SET_IR", [""]); //내용초기화
		
		$("#post_title").val("");
		$("#writter").val("");
		$("#post_password").val("");
	});
	
	$(".submit_post").on("click", function() {
		if (post_mode == "insert") {
			if (confirm("게시글을 등록 하시겠습니까?")) {
				oEditors.getById["post_contents"].exec("UPDATE_CONTENTS_FIELD", []);
				
				$("#post_editor_form").attr("action", "${admin_root_path}/workboard/board/insert");
				
				form_submit($("#post_editor_form"));
			}
		} else if (post_mode == "modify") {
			if (confirm("게시글을 수정 하시겠습니까?")) {
				oEditors.getById["post_contents"].exec("UPDATE_CONTENTS_FIELD", []);
				
				$("#post_editor_form").attr("action", "${admin_root_path}/workboard/board/update");
				
				form_submit($("#post_editor_form"));
			}
		}
	});
	
	var sel_post_id;
	var btn_type;
	
	$(".show_post, .edit_post").on("click", function() {
		$('.post_editor').dialog("close");
		$(".post_view").dialog("close");
		
		var post_id = $(this).attr("data-post-id");
		var menu_idx = $(this).attr("data-menu-id");
		btn_type = $(this).attr("data-btn-type");
		
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null) {
				alert(data['message']);
				if (data['next_url'] != null) {
					location.href = data['next_url'];
				}
				return;
			}
			
			var post = data['object'];
			var post_pw = post['post_password'];
			sel_post_id = post['post_id'];
			
			if (post_pw == "tempPassword") {
				$("#post_view_password").val("");
				$(".post_password").dialog("open");
				
				return;
			}
			
			if (btn_type == "view") {
				viewPost(post);
			} else if (btn_type= "modify") {
				editPost(post);
			}
			
		}
		
		
		get_item_info("${admin_root_path}/workboard/board/getpost", func, {"post_id":post_id, "menu_idx":menu_idx}, $('body'));
	});
	
	$(".submit_pw").on("click", function() {
		$('.post_editor').dialog("close");
		$(".post_view").dialog("close");
		
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null) {
				alert(data['message']);
				if (data['next_url'] != null) {
					location.href = data['next_url'];
				}
				return;
			}
			
			var post = data['object'];

			if (btn_type == "view") {
				viewPost(post);
			} else if (btn_type= "modify") {
				editPost(post);
			}
		}

		get_item_info("${admin_root_path}/workboard/board/validPW", func, {"post_id":sel_post_id, "post_password":$("#post_view_password").val()}, $('body'));
	});
	
	$(".select_all").on("change", function() {
		if ($(this).prop("checked")) {
			$(".del_post").prop("checked", true);
		} else {
			$(".del_post").prop("checked", false);
		}
	});
	
	$(".del_post_btn").on("click", function() {
		var target_array = [];
		var func = function(data) {
			//잘모된 페이지 요청시
			if (data['message'] != null) {
				alert(data['message']);
				if (data['next_url'] != null) {
					location.href = encodeURI(data['next_url']);
				}
				return;
			}
		}
		
		$.each($(".del_post"), function(index, value) {
			if ($(value).prop("checked") == true) {
				target_array.push($(value).val());
			}
		});
		
		if (confirm("선택한 게시글을 삭제 하시겠습니까?")) {
			get_item_info("${admin_root_path}/workboard/board/delete", func, {"target_array":target_array}, $('body'));			
		}
	});
});
</script>
<div class="body">
	<div class="content-container">
		<div class="menu-tree">
			<div class="search-section">
				<form class="search-form" onsubmit="return false;">
					<div class="search-layout">
						<input type="text" class="node-search" placeholder="찾고자하는 메뉴의 명칭을 입력해주세요."/>
						<a class="btn-gray btn-node-search">검색</a>
					</div>
				</form>
			</div>
			<div class="tree-section tree-type2">
				<div id="tree1"></div>
			</div>
		</div>
		<div class="board_continer" style="display: inline-block; width: 70%;">
			<table class="dark_table" style="font-size: 1vw;">
				<tr>
					<td><input type="checkbox" class="select_all"/></td>
					<td>순번</td>
					<td>제목</td>
					<td>작성자</td>
					<td>작성일</td>
					<td>수정일</td>
					<td>기능</td>
				</tr>
				<c:forEach items="${post_list}" var="post" varStatus="status">
				<tr>
					<td>
						<input type="checkbox" class="del_post" value="${post.post_id}"/>
					</td>
					<td>
						${paging.numberic + status.index}
					</td>
					<td style="width:30%; text-align: center;">
						<a data-btn-type="view" class="show_post" data-post-id="${post.post_id}" data-menu-id="${post.menu_idx}" style="cursor:pointer; width: 90%; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; display: inline-block;">
							${post.post_title}
						</a>
					</td>
					<td>
						${post.writter}
					</td>
					<td>
						${post.write_date}
					</td>
					<td>
						${post.modify_date}
					</td>
					<td>
						<a data-btn-type="modify" data-post-id="${post.post_id}" data-menu-id="${post.menu_idx}" class="btn-darkblue edit_post" style="width: auto; padding: 0.35vw 0.5vw; font-size: 0.8vw; border-radius: inherit;">수정</a>
					</td>
				</tr>
				</c:forEach>
				<c:if test="${post_list == null || post_list.size() < 1}">
					<tr>
						<td colspan="7">
							등록된 업무글이 존재하지 않습니다.
						</td>
					</tr>
				</c:if>
			</table>
			<div style="margin-top:10px;">
				<a class="btn-gray del_post_btn" style="font-size: 1vw; padding: 0.5vw 1vw; display: inline-block;">삭제</a>
				<a class="btn-gray new_post" style="font-size: 1vw; padding: 0.5vw 1vw; display: inline-block; float: right;">글 작성</a>
			</div>
			${paging_layout}
		</div>
	</div>
</div>
<div class="post_editor" title="업무 보고 ">
<form id="post_editor_form" action="${admin_root_path}/workboard/board/insert" method="post" style="margin-bottom:50px;">
	<input type="hidden" name="post_id" id="post_id" value="0">
	<div style="border-bottom: 1px solid black; padding-bottom: 20px; margin-bottom: 20px;">
		<p style="margin-bottom: 10px; font-weight: bold;">
			카테고리 선택
		</p>
		<select style="width: 100%; font-size: 13px; padding: 3px 0;" id="menu_idx" name="menu_idx">
			<c:forEach items="${TreeMenu}" var="menu" varStatus="status">
				<option ${menu.parent_menu_idx == 0 ? 'disabled="disabled"':''} value="${menu.menu_idx}">${menu.menu_name}</option>
			</c:forEach>
		</select>
	</div>
	<table class="modal_table">
		<tr>
			<td>
				글 제목 (필수)
			</td>
			<td>
				<input type="text" style="width:630px;" name="post_title" id="post_title" placeholder="글의 제목을 입력 해주세요.">
			</td>
		</tr>
		<tr>
			<td>
				글 내용 (필수)
			</td>
			<td style="width: 650px;">
				<textarea rows="" cols="" name="post_contents" id="post_contents"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				작성자 (필수)
			</td>
			<td>
				<input type="text" name="writter" id="writter" style="width:100px;" placeholder="작성자 명">
			</td>
		</tr>
		<tr>
			<td>
				비밀번호 (선택)
			</td>
			<td>
				<input type="password" name="post_password" id="post_password" placeholder="숫자만 입력 해주세요.">
				<a class="btn-gray submit_post" style="color: white; font-size: 0.7vw; padding: 0.3vw 1vw; float: right; margin-right:17px;">저장</a>
			</td>
		</tr>
	</table>
</form>
</div>
<div class="post_password" title="암호를 입력 해주세요.">
	<div style="text-align: center; font-size:1vw;">
		<p style="margin-top:10px;">
			<input type="password" id="post_view_password" style="width:200px; text-align: center;">
		</p>
		<a class="btn-gray submit_pw" style="display: inline-block; margin-top: 10px; padding: 0.3vw 0.5vw; color: white;">확인</a>
	</div>
</div>
<div class="post_view" title="게시글 상세보기">
	<div style="width:50vw; font-size:1vw;">
		<div class="select_post_title" style="padding: 10px 0; border-bottom: 1px solid black; margin-bottom: 15px;">
			
		</div>
		<div class="select_post_contents" style="height: 50vh; overflow-y:auto; overflow-x:hidden;">
		
		</div>
	</div>
</div>