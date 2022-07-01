package com.example.demo.model;

import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;

public class CommentDTO {
	private long discussionId;
	
	private long questionCommentId;
	
	private Timestamp ts;
	
	@NotBlank(message = "message must not be blank")
	private String msg;
	
	private String img_location;
	
	public CommentDTO() {}
	
	public CommentDTO(long discussionId, long questionCommentId, Timestamp ts, String msg, String img_location) {
		this.discussionId = discussionId;
		this.questionCommentId = questionCommentId;
		this.ts = ts;
		this.msg = msg;
		this.img_location = img_location;
	}

	public long getDiscussionId() {
		return discussionId;
	}

	public void setDiscussionId(long discussionId) {
		this.discussionId = discussionId;
	}

	public long getQuestionCommentId() {
		return questionCommentId;
	}

	public void setQuestionCommentId(long questionCommentId) {
		this.questionCommentId = questionCommentId;
	}

	public Timestamp getTs() {
		return ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getImg_location() {
		return img_location;
	}

	public void setImg_location(String img_location) {
		this.img_location = img_location;
	}
}
