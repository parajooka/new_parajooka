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
	        	            sel_node = node;
	        	            
	        	            location.href = "${admin_root_path}/workboard/board/index?menu_idx="+ sel_node.id;
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
	</div>
</div>