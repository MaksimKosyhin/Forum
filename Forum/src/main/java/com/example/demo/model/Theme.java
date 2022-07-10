package com.example.demo.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Theme {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank(message = "theme title must not be blank")
	private String title;
	
	@OneToMany
	@JoinColumn(name = "theme_id", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Discussion> discussions;

	public Theme() {
		
	}
	
	public Theme(long id, String title, List<Discussion> discussions) {
		this.id = id;
		this.title = title;
		this.discussions = discussions;
	}

	public Theme(String title) {
		this.title = title;
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
	
	public List<Discussion> getDiscussions() {
		return discussions;
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
		Theme other = (Theme) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Theme [id=" + id + ", title=" + title + ", discussions=" + discussions + "]";
	}
}
