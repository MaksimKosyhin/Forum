package com.example.demo.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	private Timestamp ts;
	
	@Column(columnDefinition = "text")
	private String msg;
	
	private String img_location;

	@ManyToMany
	private List<Comment> replies = new ArrayList<>();
	
	public Comment() {}
	
	public Comment(Timestamp ts, String msg, String img_location) {
		this.ts = ts;
		this.msg = msg;
		this.img_location = img_location;
	}
	
	public List<Comment> getReplies() {
		return replies;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
	@Override
	public int hashCode() {
		return Objects.hash(id, img_location, msg, ts);
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
		return id == other.id && Objects.equals(img_location, other.img_location) && Objects.equals(msg, other.msg)
				&& Objects.equals(ts, other.ts);
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", ts=" + ts + ", msg=" + msg + ", img_location=" + img_location;
	}
}
