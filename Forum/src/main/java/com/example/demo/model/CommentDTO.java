package com.example.demo.model;

import javax.validation.constraints.NotBlank;

import com.example.demo.config.utils.CommentReplies;

public class CommentDTO {
	private long discussionId;
	
	@CommentReplies
	private String repliedComments;
	
	@NotBlank(message = "message must not be blank")
	private String msg;
	
	public CommentDTO() {}

	public CommentDTO(long discussionId, String repliedComments, String msg) {
		this.discussionId = discussionId;
		this.repliedComments = repliedComments;
		this.msg = msg;
	}

	public long getDiscussionId() {
		return discussionId;
	}

	public void setDiscussionId(long discussionId) {
		this.discussionId = discussionId;
	}
	
	public String getRepliedComments() {
		return repliedComments;
	}

	public void setRepliedComments(String repliedComments) {
		this.repliedComments = repliedComments;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
