package com.leavesystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Request {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
    private Date date_from;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
    private Date date_to;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date created;
	
	@Column(nullable = false)
	private Boolean status;
	
	@Column(columnDefinition = "TEXT")
	private String employeeComment;
	
	@Column(columnDefinition = "TEXT")
	private String managerComment;
	
	@Column(nullable = false)
	private String standin;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leavetype_id", nullable = false)
	private LeaveType leaveType;

	public Request() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate_from() {
		return date_from;
	}

	public void setDate_from(Date date_from) {
		this.date_from = date_from;
	}

	public Date getDate_to() {
		return date_to;
	}

	public void setDate_to(Date date_to) {
		this.date_to = date_to;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getEmployee_comment() {
		return employeeComment;
	}

	public void setEmployee_comment(String employee_comment) {
		this.employeeComment = employee_comment;
	}

	public String getManager_comment() {
		return managerComment;
	}

	public void setManager_comment(String manager_comment) {
		this.managerComment = manager_comment;
	}

	public String getStandin() {
		return standin;
	}

	public void setStandin(String standin) {
		this.standin = standin;
	}

	public User getEmployee() {
		return user;
	}

	public void setEmployee(User user) {
		this.user = user;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((date_from == null) ? 0 : date_from.hashCode());
		result = prime * result + ((date_to == null) ? 0 : date_to.hashCode());
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
		if (date_from == null) {
			if (other.date_from != null)
				return false;
		} else if (!date_from.equals(other.date_from))
			return false;
		if (date_to == null) {
			if (other.date_to != null)
				return false;
		} else if (!date_to.equals(other.date_to))
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
