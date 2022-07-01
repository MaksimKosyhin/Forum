package com.example.demo.model;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

public class DiscussionDTO {
	
	private long themeId;
	
	@NotBlank(message = "discussion title must not be blank")
	private String title;
	
	private Comment headerComment;
	
	public DiscussionDTO(){}

	public long getThemeId() {
		return themeId;
	}

	public void setThemeId(long themeId) {
		this.themeId = themeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Comment getHeaderComment() {
		return headerComment;
	}

	public void setHeaderComment(Comment headerComment) {
		this.headerComment = headerComment;
	}

	@Override
	public int hashCode() {
		return Objects.hash(headerComment, themeId, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiscussionDTO other = (DiscussionDTO) obj;
		return Objects.equals(headerComment, other.headerComment) && themeId == other.themeId
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "DiscussionDTO [themeId=" + themeId + ", title=" + title + ", headerComment=" + headerComment + "]";
	}
}
