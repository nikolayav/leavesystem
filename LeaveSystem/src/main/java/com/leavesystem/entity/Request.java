package com.leavesystem.entity;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.FutureOrPresent;

import org.springframework.format.annotation.DateTimeFormat;
@Entity
public class Request {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
    private Date dateFrom;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
    private Date dateTo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date created;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private RequestStatus status;
	
	@Column(columnDefinition = "TEXT")
	private String employeeComment;
	
	@Column(columnDefinition = "TEXT")
	private String managerComment;
	
	@Column(nullable = false, length = 80)
	private String standin;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "leavetype_id", nullable = false)
	private LeaveType leaveType;

	private String googleCalendarEventId;
	
	public int calculateTotalDaysOfLeaveSubmit() {
		
		Date dateStart = this.getDateFrom();
		Date dateEnd = this.getDateTo();
		
		LocalDate localDateStart = dateStart.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
		
		LocalDate localDateTo = dateEnd.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
		
		
		Period p = Period.between(localDateStart, localDateTo) ;
		int leaveDays = p.getDays() + 1;
		
		int totalDays = leaveDays;
		
		for (int i = 0; i < leaveDays; i++) {
			LocalDate date = localDateStart.plusDays(i);
			switch (date.getDayOfWeek()) {
			case SATURDAY:
			case SUNDAY:
				totalDays -= 1;
				break;
			}
		}

		return totalDays;
	}
	
	public Request() {
		this.status = RequestStatus.Submitted; // Added default status
	}
	
	public Request(User user) {
		this();
		this.user = user;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public String getEmployeeComment() {
		return employeeComment;
	}

	public void setEmployeeComment(String employee_comment) {
		this.employeeComment = employee_comment;
	}

	public String getManagerComment() {
		return managerComment;
	}

	public void setManagerComment(String manager_comment) {
		this.managerComment = manager_comment;
	}

	public String getStandin() {
		return standin;
	}

	public void setStandin(String standin) {
		this.standin = standin;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	
	public String getGoogleCalendarEventId() {
		return googleCalendarEventId;
	}

	public void setGoogleCalendarEventId(String googleCalendarEventId) {
		this.googleCalendarEventId = googleCalendarEventId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((dateFrom == null) ? 0 : dateFrom.hashCode());
		result = prime * result + ((dateTo == null) ? 0 : dateTo.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((employeeComment == null) ? 0 : employeeComment.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((leaveType == null) ? 0 : leaveType.hashCode());
		result = prime * result + ((managerComment == null) ? 0 : managerComment.hashCode());
		result = prime * result + ((standin == null) ? 0 : standin.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (dateFrom == null) {
			if (other.dateFrom != null)
				return false;
		} else if (!dateFrom.equals(other.dateFrom))
			return false;
		if (dateTo == null) {
			if (other.dateTo != null)
				return false;
		} else if (!dateTo.equals(other.dateTo))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (employeeComment == null) {
			if (other.employeeComment != null)
				return false;
		} else if (!employeeComment.equals(other.employeeComment))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (leaveType == null) {
			if (other.leaveType != null)
				return false;
		} else if (!leaveType.equals(other.leaveType))
			return false;
		if (managerComment == null) {
			if (other.managerComment != null)
				return false;
		} else if (!managerComment.equals(other.managerComment))
			return false;
		if (standin == null) {
			if (other.standin != null)
				return false;
		} else if (!standin.equals(other.standin))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	
	
}
