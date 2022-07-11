package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Discussion {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	private String title;
	
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
		return Long.hashCode(id);
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
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Discussion [id=" + id + ", title=" + title + ", headerComment=" + headerComment + "]";
	}
}