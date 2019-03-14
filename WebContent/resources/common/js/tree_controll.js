/*========================================================= jqtree관련*/

/**
 * jQtree생성 함수
 * @param data
 * @returns
 */
function setjQtree(data) {
	$('.body').append('<div style="position: absolute; left:-9999px;">'+
					  '<img class="openicon" src="/resources/common/img/openfolder.png" style="width: 0.8vw;">'+
					  '<img class="closeicon" src="/resources/common/img/closefolder.png" style="width: 0.8vw;">'+
					  '</div>');
	
	var $tree = $('#tree1').tree({
        data: data,
        autoOpen: true,
        dragAndDrop: true,
        openedIcon: $('.closeicon'),
        closedIcon: $('.openicon'),
        onCreateLi: function(node, $li, is_selected) {
        	if (node.id != 0) {
	        	$li.find('.jqtree-title').before(
	        			 '<div style="float:left;">'+
					 	 '<span style="display:inline-block; width:13px; height:13px; margin-top:2px; margin-rigth:3px;">'+
					 	 	'<hr style="width:100%; color:black; border:0.5px dotted gray;">'+
					 	 '</span>'+
						 '</div>');
	        } else {
	        	$li.find('.jqtree-title').before(
	        			 '<div style="float:left;">'+
					 	 '<span style="display:inline-block; width:13px; height:13px; margin-top:2px; margin-rigth:3px;">'+
					 	 '</span>'+
						 '</div>');
	        }
	        
	 		if (node.children.length == 0) {            
	 			$li.find('.jqtree-title').before('<div style="float:left;">'+
	 											 	'<img style="width:0.7vw; margin-top:0.1vw;" src="/resources/common/img/default.png">'+
	 											 '</div>'+
	 											 '');
	 		}
        }
    });
	
	return $tree;
}

$(function() {
	/**
	 * 노드 검색 함수들
	 */
	function searchNode() {
		var val = $('.node-search').val();
		var group = document.getElementsByClassName("jqtree-title");
		
		if (val.length <= 0) {
			alert('검색어를 입력해주세요.');
			return;
		}
		var count = 0;
		for (var i = 0; i < group.length; i++) {
			var text = group[i].innerHTML;
			if (text.indexOf(val) >= 0) {
				count++;
				group[i].style.backgroundColor = "yellow";
			} else {
				group[i].style.backgroundColor = "white";
			}
		}
		
		$('.result-counting').text(count);
	}
	
	/**
	 * 노드 검색창이 비워질경우 검색 내용 초기화 이벤트
	 */		
	var search_result;
	$('.node-search').keyup(function() {
		var value = $(this).val();
		if (value.length == 0) {
			var group = document.getElementsByClassName("jqtree-title");
			for (var i = 0; i < group.length; i++) {
				group[i].style.backgroundColor = "white";
			}
			
			$('.result-counting').text(0);
		}
	});

	/**
	 * 검색창 엔터키 이벤트
	 */	
	$('.node-search').keydown(function (key) {
	       if(key.keyCode == 13){//키가 13이면 실행 (엔터는 13)
	       	searchNode();
	       }
	});


	/**
	 * 검색버튼 클릭 이벤트 
	 */
	$('.btn-node-search').click(function() {
		searchNode();
	});
});

/*========================================================= jqtree관련*/