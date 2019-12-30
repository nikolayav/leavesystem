package com.leavesystem.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.leavesystem.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
	List<User> findAll();
	List<User> findAllByAuthoritiesAuthority(String authority);
	List<User> findAllByManager(User user); // Used to get a list of employees under the given manager
	List<User> findAllByRoleInAndIdNotIn(List<String> roles, List<Long> id); // Used to generate the list of employees in the system, excluding admins, the current user and/or other constraints
}
