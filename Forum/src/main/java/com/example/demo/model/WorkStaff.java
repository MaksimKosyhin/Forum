package com.example.demo.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.security.UserRole;
import com.example.demo.util.annotation.ExistingRole;

@Entity
public class WorkStaff {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank(message = "username must not be empty")
	private String username;
	@Size(min = 5)
	private String password;
	@ExistingRole
	private String userRole;
	private boolean roleAllowed;
	
	public WorkStaff() {}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getUserRole() {
		return UserRole.valueOf(this.userRole);
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public boolean isRoleAllowed() {
		return roleAllowed;
	}

	public void setRoleAllowed(boolean roleAllowed) {
		this.roleAllowed = roleAllowed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, password, roleAllowed, userRole, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkStaff other = (WorkStaff) obj;
		return id == other.id && Objects.equals(password, other.password) && roleAllowed == other.roleAllowed
				&& Objects.equals(userRole, other.userRole) && Objects.equals(username, other.username);
	}
}
