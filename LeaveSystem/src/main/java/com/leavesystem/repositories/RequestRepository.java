package com.leavesystem.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.leavesystem.entity.*;

public interface RequestRepository extends CrudRepository<Request, Long> {
	
	List<Request> findByUser(User user);
	List<Request> findByStatusInAndUserIn(List<RequestStatus> status, List<User> users); // Used for lists of requests of a given team by the specified statuses
	
	List<Request> findByUserAndDateFromGreaterThanEqualAndDateToLessThanEqual(User user, Date dateFrom, Date dateTo);
	// Used for a list of all requests by a specific employee in a given time period.
	// Defined wrapper method to shorten method name
	default List<Request> findByUserAndDates(User user, Date dateFrom, Date dateTo) {
		return findByUserAndDateFromGreaterThanEqualAndDateToLessThanEqual(user, dateFrom, dateTo);
	}
	
	List<Request> findByUserInAndDateFromGreaterThanEqualAndDateToLessThanEqual(List<User> users, Date dateFrom, Date dateTo);
	default List<Request> findByUserListAndDates(List<User> users, Date dateFrom, Date dateTo) {
		return findByUserInAndDateFromGreaterThanEqualAndDateToLessThanEqual(users, dateFrom, dateTo);
	}
	
	List<Request> findByUserInAndStatusInAndDateFromGreaterThanEqualAndDateToLessThanEqual(List<User> users, List<RequestStatus> statusList, Date dateFrom, Date dateTo);
	default List<Request> findByUserListStatusAndDates(List<User> users, List<RequestStatus> statusList, Date dateFrom, Date dateTo) {
		return findByUserInAndStatusInAndDateFromGreaterThanEqualAndDateToLessThanEqual(users, statusList, dateFrom, dateTo);
	}
}
