package com.leavesystem.web.requestComponents;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.leavesystem.entity.*;

@Component
public class FormInputs {
	private Long id;
	private User user;
	private Date dateFrom;
	private Date dateTo;
	private RequestStatus status;
	private LeaveType leaveType;
	private String comments;
	
	public FormInputs() {
		this.dateFrom = Date.valueOf(LocalDate.now());
		this.dateTo = Date.valueOf(LocalDate.now());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	public RequestStatus getStatus() {
		return status;
	}
	public void setStatus(RequestStatus status) {
		this.status = status;
	}
	public LeaveType getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
}
