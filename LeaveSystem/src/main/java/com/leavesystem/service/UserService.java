package com.leavesystem.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.leavesystem.entity.User;
import com.leavesystem.repositories.UserRepository;
import com.leavesystem.security.Authority;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User save(User user, String userRole) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		Authority authority = new Authority();
		authority.setAuthority("ROLE_" + userRole.toUpperCase());
		authority.setAuthUser(user);
		
		user.getAuthorities().add(authority);
		return userRepo.save(user);
	}
	
    public void deleteUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()) {
            userRepo.deleteById(id);
        }
    } 
}
