package com.example.demo.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Discussion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	private String title;
	
	private boolean discussionActive = true;
	
	@OneToOne
	@JoinColumn(name = "header_comment_id", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Comment headerComment;
	
	public Discussion(){}
	
	public Discussion(String title, Comment headerComment) {
		this.title = title;
		this.headerComment = headerComment;
	}

	public long getId() {
		return id;
	}
	
	public boolean isDiscussionActive() {
		return discussionActive;
	}

	public void setDiscussionActive(boolean discussionActive) {
		this.discussionActive = discussionActive;
	}

	public Comment getHeaderComment() {
		return headerComment;
	}
	
	public String getTitle() {
		return title;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Discussion other = (Discussion) obj;
		return id == other.id && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Discussion [id=" + id + ", title=" + title + ", headerComment=" + headerComment + "]";
	}
}