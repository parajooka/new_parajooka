<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="/resources/css/admin/menu.css" />
<link rel="stylesheet" type="text/css" href="/resources/common/css/layout-tree.css" />
<link rel="stylesheet" type="text/css" href="/resources/common/css/jqtree.css" />
<script src="/resources/common/js/jqTreeContextMenu.js"></script>
<script src="/resources/common/js/tree.jquery.js"></script>
<script src="/resources/common/js/tree_controll.js"></script>
<script src="/resources/common/ace/src-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
var editor;
var mobile_editor;
var sel_node = null; //선택된 노드가 담길 변수
var g_id = '';	
var parent_node = null; //선택 메뉴의 최상위 부모가 담길 변수
var full_path = null; // 전체 경로

$(function() {
/*===============================트리 정보 ajax로 호출 및 트리 생성*/
$.ajax({
	    type:"GET",
	    cache : false,
	    url:'/jooka/admin/menu/bytree',
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
	        //jqtree생성함수 호출 종료
	        
	        /**
	         * 노드 드래그(이동) 이벤트
	         */	
	        	$tree.on('tree.move', function(event) {
	        		if (confirm(event.move_info.moved_node.name + ' 을(를)\n\n' + event.move_info.target_node.name + '의 하위메뉴로 이동 하시겠습니까?')) {
	        			if(confirm(event.move_info.moved_node.name + ' 의 모든 하위메뉴들도 함께 이동됩니다.\n\n이동 하시겠습니까?')) {
	        				var sel_id = event.move_info.moved_node.id;
	        				var parent_id = event.move_info.target_node.id;
	        				
	        				var func = function(data) {
	        					var message = data['message'];
	        			    	var nextUrl = data['next_url'];
	        			    	
	        			    	alert(message);
	        			    	location.replace(nextUrl);
	        				}
	        				
	        				get_item_info("/jooka/admin/menu/moveMenu", func, {"menu_idx":sel_id, "parent_menu_idx": parent_id}, $('body'));
	        			}
	        		}
	        		return false;
	        	});
	        	
	            
	        /**
	         * 노드 클릭(선택) 이벤트
	         */	
	        	$tree.on(
	        	    'tree.select',
	        	    function(event) {
	        	        if (event.node) {
	        	            //노드가 선택 되어있을경우.
	        	            var node = event.node;
	        	            sel_node = node;
	        	            
	        	            $('.white-mask').hide();
	        	            
	        	            if ((g_id.length < 1 || g_id != node.id) && node.id > 0)	{
	        	            	//location.href = '${context_path}/wbuilder/admin_menu/loadmenu.do?menu_idx='+ node.id;
	        	            	
	        	            	var func = function(data) {
	        	            		
	        	            		var menu = data['object'][0];
	        	            		var menu_html = data['object'][1];
	        	            		
	        	            		$('#menu_idx').val(menu["menu_idx"]);
	        	            		$('#group_idx').val(menu["group_idx"]);
	        	            		$('#menu_name').val(menu["menu_name"]);
	        	            		$('#parent_menu_idx').val(menu["parent_menu_idx"]);
	        	            		$('#print_seq').val(menu["print_seq"]);
	        	            		$('#menu_url').val(menu["menu_url"]);
	        	            		
	        	            		$('#menu_type').val(menu["menu_type"]);
	        	            		$('#view_yn').val(menu["view_yn"]);
	        	            		$("#use_yn").val(menu["use_yn"]);
	        	            		
	        	            		if (menu["menu_type"] == 0) {
	        	            			$('.editor_title').html(menu["menu_name"] +".html 편집");
	        	            			$('.html_text').show();
	        	            		} else {
	        	            			$('.html_text').hide();
	        	            		}
	        	            		
	        	            		if (typeof mobile_editor != "undefined" && mobile_editor != null) {
	        	            			$('.mobile_editor_title').html(menu["menu_name"] +".html 모바일버전 편집");
	        	            			mobile_editor.setValue(menu_html["mobile_menu_html"]);
	        	            		}
	        	            		
	        	            		editor.setValue(menu_html["menu_html"]);
	        	            		g_id = node.id;
	        	            	}
	        	            	
		        	        	$('.edit_submit').show();
		        	        	$('.insert_submit').hide();
	        	            	get_item_info("/jooka/admin/menu/getMenu", func, {"menu_idx" : node.id}, $('body'));
	        	            } else if (node.id == 0) {
		        	        	$('.insert_submit').show();
		        	        	$('.edit_submit').hide();
		        	    		$('.addMenu').trigger('click');
		        	    		
        	            		g_id = '';
	        	            }
	        	        }
        	        	//노드가 해제된 경우
	        	        else {
	        	        	$('.white-mask').show();
	        	        	$('.insert_submit').hide();
	        	        	$('.edit_submit').hide();
	        	        	
	        	    		$('#group_idx').val("-1");
	        	    		$('#parent_menu_idx').val("-1");
	        	        	//선택 노드 초기화
	        	        		sel_node = null;
	        	        	//입력값 초기화
	                    		g_id = '';
	        	        	

       	            		$('#menu_idx').val(0);
       	            		$('#group_idx').val(-1);
       	            		$('#menu_name').val("");
       	            		$('#parent_menu_idx').val(-1);
       	            		$('#print_seq').val("0");
       	            		$('#menu_url').val("");
       	            		
       	            		$('#menu_type').val(0).prop("selected", true);
       	            		$('#view_yn').val(0).prop("selected", true);
       	            		$('.html_text').hide();
	        	        }
	        	    });
        	}
	  });
});	
</script>
<script src="/resources/js/admin/menu.js"></script>

<div class="body">
	<div class="content-container">
		<div class="menu-tree">
			<div class="tree-search-result">
				<span class="result-count">
					조회결과 : <span class="result-counting">0</span>건
				</span>
			</div>
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
			<a class="addMenu btn-green menu_btn">메뉴 추가</a>
			<a class="deleteMenu btn-orange menu_btn">메뉴 삭제</a>
		</div>
		<div class="menu_editor">
			<form class="menu_editor_form">
				<div class="white-mask"></div>
				<input type="hidden" name="parent_menu_idx" id="parent_menu_idx" value="-1">
				<input type="hidden" name="menu_idx" id="menu_idx" value="0">
				<input type="hidden" name="group_idx"  id="group_idx" value="-1">
				
				<table class="menu_editor_table blue_table">
					<tr>
						<td>메뉴 명</td>
						<td>
							<input type="text" name="menu_name" id="menu_name"/>
							<a class="plus_info">최소 2글자 최대 10글자 </a>
						</td>
					</tr>
					<tr>
						<td>메뉴 타입</td>
						<td>
							<select name="menu_type" id="menu_type">
								<option value="0">html 직접 작성</option>
								<option value="1">내부 url 이동</option>
								<option value="2">게시판 레이아웃 사용</option>
								<option value="3">갤러리 레이아웃 사용</option>
								<option value="4">Q&A 레이아웃 사용</option>
							</select>
							<a class="plus_info">html작성 선택시에는 직접 작성한 html이 웹에 보여집니다.</a>
						</td>
					</tr>
					<tr>
						<td>메뉴 URL</td>
						<td>
							<input type="text" name="menu_url" id="menu_url"/>
							<a class="plus_info">메뉴 url은 메뉴의 타입이 html작성이 아닐경우 모두 적용됩니다. ex) /jooka/admin/access_ip</a>
						</td>
					</tr>
					<tr>
						<td>출력순서</td>
						<td>
							<input type="number" name="print_seq" id="print_seq" value="0"/>
							<a class="plus_info">숫자가 작을수록 먼저 나타납니다. </a>
						</td>
					</tr>
					<tr>
						<td>사용여부</td>
						<td>
							<select name="use_yn" id="use_yn">
								<option value="0">사용</option>
								<option value="1">미사용</option>
							</select>
							<a class="plus_info">미사용 설정시 해당 메뉴는 접근할수 없습니다.</a>
						</td>
					</tr>
					<tr>
						<td>공개여부</td>
						<td>
							<select name="view_yn" id="view_yn">
								<option value="0">공개</option>
								<option value="1">비공개</option>
							</select>
							<a class="plus_info">비공개 설정시 해당 메뉴는 나타나지 않습니다.</a>
						</td>
					</tr>
				</table>
				<a class="btn-darkblue menu_btn insert_submit">추가</a>
				<a class="btn-gray menu_btn edit_submit">수정</a>
			</form>
			<a class="info_title">* 메뉴 추가 : 먼저 좌측에서 추가하려는 메뉴의 부모메뉴를 선택후 메뉴추가를 클릭해주세요.</a><br>
			<a class="info_title">* 메뉴 수정 : 좌츽에서 '최상위'를 제외한 메뉴중 수정할 메뉴를 선택해주세요.</a>
			<a class="info_title">* HTML 작성 : 메뉴에 해당하는 HTML을 작성하기위해서는 좌측에서 메뉴를 선택해주세요.</a>
		</div>
	</div>
</div>
<div class="html_text">
	<p class="editor_title"></p>
	<div id="editor"></div>
	<p style="text-align: center; width: 100%; margin-top: 3vw;">
		<a class="btn-green html_save">저장</a>
	</p>
	<script>
	    editor = ace.edit("editor");
	    editor.setTheme("ace/theme/chrome");
	    editor.session.setMode("ace/mode/html");
	</script>
</div>
<c:if test="${homepage.mobile_use == 1}">
	<div class="html_text">
		<p class="mobile_editor_title"></p>
		<div id="mobile_editor"></div>
		<p style="text-align: center; width: 100%; margin-top: 3vw;">
			<a class="btn-green mobile_html_save">저장</a>
		</p>
		<script>
			mobile_editor = ace.edit("mobile_editor");
			mobile_editor.setTheme("ace/theme/chrome");
			mobile_editor.session.setMode("ace/mode/html");
		</script>
	</div>
</c:if>