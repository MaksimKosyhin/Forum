package com.example.demo.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Theme {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private long id;
	
	private String title;
	
	@OneToMany
	@JoinColumn(name = "theme_id", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Discussion> discussions;

	public Theme() {
		
	}
	
	public Theme(String title, Set<Discussion> discussions) {
		this.title = title;
		this.discussions = discussions;
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
	
	public Set<Discussion> getDiscussions() {
		return discussions;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(discussions, id, title);
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
		return Objects.equals(discussions, other.discussions) && id == other.id && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Theme [id=" + id + ", title=" + title + ", discussions=" + discussions + "]";
	}
}
