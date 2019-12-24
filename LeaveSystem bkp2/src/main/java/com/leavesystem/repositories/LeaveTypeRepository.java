package com.leavesystem.repositories;

import org.springframework.data.repository.CrudRepository;

import com.leavesystem.entity.LeaveType;

public interface LeaveTypeRepository extends CrudRepository<LeaveType, Long> {

}
