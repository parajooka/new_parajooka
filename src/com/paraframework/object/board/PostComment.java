package com.paraframework.object.board;

import java.sql.Date;

public class PostComment {
	private int comment_id;
	private int post_id;
	private String writter_ip_add;
	private String modify_ip_add;
	private int reply_to = 0;
	private String contents;
	private Date write_date = new Date(new java.util.Date().getTime());
	private Date modify_date = new Date(new java.util.Date().getTime());
	
	
	public String getModify_ip_add() {
		return modify_ip_add;
	}
	public void setModify_ip_add(String modify_ip_add) {
		this.modify_ip_add = modify_ip_add;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public String getWritter_ip_add() {
		return writter_ip_add;
	}
	public void setWritter_ip_add(String writter_ip_add) {
		this.writter_ip_add = writter_ip_add;
	}
	public int getReply_to() {
		return reply_to;
	}
	public void setReply_to(int reply_to) {
		this.reply_to = reply_to;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Date getWrite_date() {
		return write_date;
	}
	public void setWrite_date(Date write_date) {
		this.write_date = write_date;
	}
	public Date getModify_date() {
		return modify_date;
	}
	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
	@Override
	public String toString() {
		return "PostComment [comment_id=" + comment_id + ", post_id=" + post_id + ", writter_ip_add=" + writter_ip_add
				+ ", modify_ip_add=" + modify_ip_add + ", reply_to=" + reply_to + ", contents=" + contents
				+ ", write_date=" + write_date + ", modify_date=" + modify_date + "]";
	}
	
	
	
	
}
