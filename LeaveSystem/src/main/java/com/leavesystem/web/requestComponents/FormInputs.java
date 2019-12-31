package com.leavesystem.web.requestComponents;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.leavesystem.entity.*;

@Component
public class FormInputs {
	private Long id;
	private User user;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateFrom;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateTo;
	private RequestStatus status;
	private LeaveType leaveType;
	private String comments;
	
	public FormInputs() {
		this.dateFrom = Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1));
		this.dateTo = Timestamp.valueOf(LocalDateTime.now());
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
