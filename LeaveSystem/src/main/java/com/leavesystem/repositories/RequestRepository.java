package com.leavesystem.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.leavesystem.entity.Request;
import com.leavesystem.entity.User;

public interface RequestRepository extends CrudRepository<Request, Long> {
	
	List<Request> findByUser(User user);
}
