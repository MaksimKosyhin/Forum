package com.example.demo.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class HeaderComment {
	@Id
	@Column(name = "comment_id")
	private long id;
	
	@OneToOne
    @MapsId
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Comment comment;

	public HeaderComment() {
	}

	public HeaderComment(Comment comment) {
		this.comment = comment;
	}

	public long getId() {
		return id;
	}

	public Comment getComment() {
		return comment;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(comment, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeaderComment other = (HeaderComment) obj;
		return Objects.equals(comment, other.comment) && id == other.id;
	}

	@Override
	public String toString() {
		return "HeaderComment [id=" + id + ", comment=" + comment + "]";
	}
}
