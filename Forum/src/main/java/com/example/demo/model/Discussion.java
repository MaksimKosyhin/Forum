package com.example.demo.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
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
	
	@OneToMany
	@JoinColumn(name = "discussion_id", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Comment> comments = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name = "header_comment_id", referencedColumnName = "id")
	private Comment headerComment;
	
	public Discussion(){}
	
	public Discussion(String title, Comment headerComment) {
		this.title = title;
		this.headerComment = headerComment;
	}

	public long getId() {
		return id;
	}
	
	public Comment getHeaderComment() {
		return headerComment;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Set<Comment> getComments() {
		return comments;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comments, id, title);
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
		return Objects.equals(comments, other.comments) && id == other.id && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Discussion [id=" + id + ", title=" + title + ", comments=" + comments + "]";
	}
}