package com.example.demo.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.example.demo.security.UserAuthority.*;

public enum UserRole {
	ADMIN(Set.of(CREATE_THEME,
			CLOSE_THEME, 
			CLOSE_DISCUSSION, 
			DELETE_THEME, 
			DELETE_DISCUSSION, 
			DELETE_COMMENT)),
	
	MODERATOR(Set.of(CLOSE_DISCUSSION,
			DELETE_DISCUSSION,
			DELETE_COMMENT));
	
	private final Set<GrantedAuthority> authorities;
	
	UserRole(Set<UserAuthority> authorities) {
		this.authorities = authorities
				.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.name()))
				.collect(Collectors.toSet());
		
		this.authorities.add(new SimpleGrantedAuthority("ROLE_%s".formatted(this.name())));
	}
	
	public Set<GrantedAuthority> getGrantedAuthorities() {
		return authorities;
	}
}
