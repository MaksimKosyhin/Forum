package com.example.demo.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Comment{
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	private Timestamp dateCreated;
	
	@Column(columnDefinition = "text")
	private String msg;

	private boolean closed;
	
	@ManyToMany
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Comment> replies;
	
	public Comment() {}

	public Comment(Timestamp dateCreated, String msg) {
		this.dateCreated = dateCreated;
		this.msg = msg;
	}

	public Comment(Timestamp dateCreated, String msg, List<Comment> replies) {
		this.dateCreated = dateCreated;
		this.msg = msg;
		this.replies = replies;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated.toLocalDateTime();
	}

	public void setDateCreateed(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	public List<Comment> getReplies() {
		return replies;
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
		Comment other = (Comment) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", dateCreated=" + dateCreated + ", msg=" + msg + ", replies=" + replies + "]";
	}
	
}
