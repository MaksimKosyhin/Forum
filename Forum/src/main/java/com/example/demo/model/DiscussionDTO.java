package com.example.demo.model;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

public class DiscussionDTO {
	
	private long themeId;
	
	@NotBlank(message = "discussion title must not be blank")
	private String title;
	
	private String msg;
	
	public DiscussionDTO(){}

	public DiscussionDTO(long themeId, String title,
			String msg) {
		this.themeId = themeId;
		this.title = title;
		this.msg = msg;
	}

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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public int hashCode() {
		return Objects.hash(msg, themeId, title);
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
		return Objects.equals(msg, other.msg) && themeId == other.themeId && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "DiscussionDTO [themeId=" + themeId + ", title=" + title + ", msg=" + msg + "]";
	}
}
