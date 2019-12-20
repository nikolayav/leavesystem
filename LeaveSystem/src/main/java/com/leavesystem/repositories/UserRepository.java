package com.leavesystem.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.leavesystem.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
//	List<User> findAll();

	
}
