package com.leavesystem.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import com.leavesystem.entity.User;

public class CustomSecurityUser extends User implements UserDetails{
	private static final long serialVersionUID = 3123006403286261349L;
	
	Set<Authority> authorities = new HashSet<>();
	
	public CustomSecurityUser () {}
	
	public CustomSecurityUser(User user) {
		this.setId(user.getId());
		this.setAuthorities(user.getAuthorities());
		this.setFirstName(user.getFirstName());
		this.setMiddleName(user.getMiddleName());
		this.setLastName(user.getLastName());
		this.setPassword(user.getPassword());
		this.setUsername(user.getUsername());
		this.setPaidLeaveDaysLeft(user.getPaidLeaveDaysLeft());
		this.setPosition(user.getPosition());
		this.setManager(user.getManager());
				
	}

	@Override
	public Set<Authority> getAuthorities() {
		return super.getAuthorities();
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	
	
}
