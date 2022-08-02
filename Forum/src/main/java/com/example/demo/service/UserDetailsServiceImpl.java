package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.WorkStaff;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.util.exception.DBEntryNotFoundException;
import com.example.demo.util.exception.SecurityException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private final UserRepository users;
	
	@Autowired
	public UserDetailsServiceImpl(UserRepository users) {
		this.users = users;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		WorkStaff user =  users.findByUsername(username)
				.orElseThrow(() -> 
					new DBEntryNotFoundException("user with name: %s was not found"
							.formatted(username)));
		
//		if(user.isRoleAllowed()) {
//			return new AuthenticatedUser(user.getUsername(),
//					user.getPassword(),
//					user.getUserRole(),
//					true, true, true, user.isRoleAllowed());
//		} else {
//			throw new SecurityException(
//					"role for user %s is under confirmation".formatted(user.getUsername()));
//		}
		
		return new AuthenticatedUser(user.getUsername(),
				user.getPassword(),
				user.getUserRole(),
				true, true, true, user.isRoleAllowed());
	}
	
	public void saveUser(WorkStaff user) {
		if(users.findByUsername(user.getUsername()).isEmpty()) {
			user.setPassword(passwordEncoder().encode(user.getPassword()));
			users.save(user);
		} else {
			throw new SecurityException(
					"user with username %s already exists".formatted(user.getUsername()));
		}
	}
	
	public List<WorkStaff> getAll() {
		return users.findAll();
	}
	
	public void setRoleAllowed(long userId) {
		WorkStaff user = users.findById(userId)
				.orElseThrow(() -> new DBEntryNotFoundException(
						"user with id: %d was not found".formatted(userId)));
		
		user.setRoleAllowed(user.isRoleAllowed() ? false : true);
		users.save(user);
	}
	
	public void deleteUser(long userId) {
		if(users.existsById(userId)) {
			users.deleteById(userId);
		} else {
			throw new DBEntryNotFoundException(
				"user with id: %d was not found".formatted(userId));
		}
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
