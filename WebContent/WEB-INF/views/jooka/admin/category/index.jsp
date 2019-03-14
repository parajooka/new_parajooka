<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="/resources/css/admin/category.css" />
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
var parent_node = null; //선택 카테고리의 최상위 부모가 담길 변수
var full_path = null; // 전체 경로

$(function() {
/*===============================트리 정보 ajax로 호출 및 트리 생성*/
$.ajax({
	    type:"GET",
	    cache : false,
	    url:'/jooka/admin/category/bytree',
	    success : function(data) {
	    	var category_list = data['object'];
			var source = []; //전체 트리를 담을 변수
	        var items = [];  //노드 덩어리를 담을 변수
	        
	        search_result = category_list.length - 1;
	        
	        for (var i = 0; i < category_list.length; i++) { //json데이터 for
	            var category = category_list[i];
	            var parent_category_id = category['parent_category_idx'];
	            var id = category['category_idx'];
	            var title = category['category_name'];

	            if (items[parent_category_id]) { //현재 카테고리의 부모idx를 key로 가지는 item이 있다면
	                var item = //아이템을 생성하기 시작한다.
	                {
	                	id: id,
	                	name: title
	                };

	                if (!items[parent_category_id].children) { //item의 자식이 비어있다면
	                    items[parent_category_id].children = [];//현재 카테고리가 들어갈 자리를 만들어준다. (배열, 다음 자식들도 들어 올거임 최초 리셋?)
	                }

	                items[parent_category_id].children[items[parent_category_id].children.length] = item; //자식으로 자기 자신을 집어놓는다.
	                items[id] = item;
	            } else { //현재 카테고리의 부모idx를 key로 가지는 item이 없다면 (자기의 상위가 없다는 말이된다.)
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
	        		if (confirm(event.move_info.moved_node.name + ' 을(를)\n\n' + event.move_info.target_node.name + '의 하위카테고리로 이동 하시겠습니까?')) {
	        			if(confirm(event.move_info.moved_node.name + ' 의 모든 하위카테고리들도 함께 이동됩니다.\n\n이동 하시겠습니까?')) {
	        				var sel_id = event.move_info.moved_node.id;
	        				var parent_id = event.move_info.target_node.id;
	        				
	        				var func = function(data) {
	        					var message = data['message'];
	        			    	var nextUrl = data['next_url'];
	        			    	
	        			    	alert(message);
	        			    	location.replace(nextUrl);
	        				}
	        				
	        				get_item_info("/jooka/admin/category/moveCategory", func, {"category_idx":sel_id, "parent_category_idx": parent_id}, $('body'));
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
	        	            	//location.href = '${context_path}/wbuilder/admin_category/loadcategory.do?category_idx='+ node.id;
	        	            	
	        	            	var func = function(data) {
	        	            		
	        	            		var category = data['object'][0];
	        	            		var category_html = data['object'][1];
	        	            		
	        	            		$('#category_idx').val(category["category_idx"]);
	        	            		$('#group_idx').val(category["group_idx"]);
	        	            		$('#category_name').val(category["category_name"]);
	        	            		$('#parent_category_idx').val(category["parent_category_idx"]);
	        	            		$('#print_seq').val(category["print_seq"]);
	        	            		
	        	            		$('#view_yn').val(category["view_yn"]);
	        	            		$("#use_yn").val(category["use_yn"]);
	        	            		$("#category_url").val(category["category_url"]);

	        	            		g_id = node.id;
	        	            	}
	        	            	
		        	        	$('.edit_submit').show();
		        	        	$('.insert_submit').hide();
	        	            	get_item_info("/jooka/admin/category/getCategory", func, {"category_idx" : node.id}, $('body'));
	        	            } else if (node.id == 0) {
		        	        	$('.insert_submit').show();
		        	        	$('.edit_submit').hide();
		        	    		$('.addCategory').trigger('click');
		        	    		
        	            		g_id = '';
	        	            }
	        	        }
        	        	//노드가 해제된 경우
	        	        else {
	        	        	$('.white-mask').show();
	        	        	$('.insert_submit').hide();
	        	        	$('.edit_submit').hide();
	        	        	
	        	    		$('#group_idx').val("-1");
	        	    		$('#parent_category_idx').val("-1");
	        	        	//선택 노드 초기화
	        	        		sel_node = null;
	        	        	//입력값 초기화
	                    		g_id = '';
	        	        	

       	            		$('#category_idx').val(0);
       	            		$('#group_idx').val(-1);
       	            		$('#category_name').val("");
       	            		$('#parent_category_idx').val(-1);
       	            		$('#print_seq').val("0");
       	            		
       	            		$('#view_yn').val(0).prop("selected", true);
       	            		$('.html_text').hide();
    	            		$("#category_url").val("");
	        	        }
	        	    });
        	}
	  });
});	
</script>
<script src="/resources/js/admin/category.js"></script>

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
						<input type="text" class="node-search" placeholder="찾고자하는 카테고리의 명칭을 입력해주세요."/>
						<a class="btn-gray btn-node-search">검색</a>
					</div>
				</form>
			</div>
			<div class="tree-section tree-type2">
				<div id="tree1"></div>
			</div>
			<a class="addCategory btn-green category_btn">카테고리 추가</a>
			<a class="deleteCategory btn-orange category_btn">카테고리 삭제</a>
		</div>
		<div class="category_editor">
			<form class="category_editor_form">
				<div class="white-mask"></div>
				<input type="hidden" name="parent_category_idx" id="parent_category_idx" value="-1">
				<input type="hidden" name="category_idx" id="category_idx" value="0">
				<input type="hidden" name="group_idx"  id="group_idx" value="-1">
				
				<table class="category_editor_table blue_table">
					<tr>
						<td>카테고리 명</td>
						<td>
							<input type="text" name="category_name" id="category_name"/>
							<a class="plus_info">최소 2글자 최대 10글자 </a>
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
							<a class="plus_info">미사용 설정시 해당 카테고리는 접근할수 없습니다.</a>
						</td>
					</tr>
					<tr>
						<td>공개여부</td>
						<td>
							<select name="view_yn" id="view_yn">
								<option value="0">공개</option>
								<option value="1">비공개</option>
							</select>
							<a class="plus_info">비공개 설정시 해당 카테고리는 나타나지 않습니다.</a>
						</td>
					</tr>
					<tr>
						<td>URL</td>
						<td>
							<input type="text" name="category_url" id="category_url"/>
							<a class="plus_info">관리자 메뉴와 연결될 url을 입력해주세요.</a>
						</td>
					</tr>
				</table>
				<a class="btn-darkblue category_btn insert_submit">추가</a>
				<a class="btn-gray category_btn edit_submit">수정</a>
			</form>
			<a class="info_title">* 카테고리 추가 : 먼저 좌측에서 추가하려는 카테고리의 부모카테고리를 선택후 카테고리추가를 클릭해주세요.</a><br>
			<a class="info_title">* 카테고리 수정 : 좌츽에서 '최상위'를 제외한 카테고리중 수정할 카테고리를 선택해주세요.</a><br>
			<a class="info_title">* 카테고리는 최대 ${max_depth}차 카테고리 까지만 표시됩니다.</a>
		</div>
	</div>
</div>