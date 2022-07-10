package com.example.demo.model;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CommentDTO {
	private long discussionId;
	
	// 12;333;9;
	@Pattern(regexp = "(\\d+;)*")
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

	public List<Long> getRepliedCommentsAsIdList() {	
		return Arrays.stream(repliedComments.split(";"))
			.mapToLong(Long::valueOf)
			.boxed()
			.toList();
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
