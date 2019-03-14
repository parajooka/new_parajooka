package com.paraframework.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paraframework.common.BaseController;
import com.paraframework.object.board.PostComment;
import com.paraframework.service.board.PostCommentService;

@Controller
@RequestMapping(value="/jooka/admin/board")
public class BoardController extends BaseController {
	@Autowired
	private PostCommentService service;
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String Test(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	
	public List<PostComment> getTreeComment(int post_id) {
		List<PostComment> comment_list = service.getCommentByPostId(post_id);
		List<PostComment> comment_parent = service.getParentcommentByPostId(post_id);
		
		List<PostComment> tree = new ArrayList<>();
		
		for (PostComment parent : comment_parent) {
			tree.addAll(getTreeCommentLoop(parent, comment_list));
		}
		
		return tree;
	}
	
	public List<PostComment> getTreeCommentLoop(PostComment parent, List<PostComment> childs) {
		List<PostComment> posts = new ArrayList<>();
		
		for (PostComment p : childs) {
			if (p.getComment_id() == parent.getComment_id() && parent.getReply_to() == 0) {
				posts.add(p);
			} else if (p.getReply_to() == parent.getComment_id()) {
				posts.add(p);
				posts.addAll(getTreeCommentLoop(p, childs));
			}
		}
		
		return posts;
	}
}
