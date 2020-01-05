package com.leavesystem.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.leavesystem.security.Authority;

@Entity
@Table(name="users")
public class User {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Pattern(regexp = "^[a-zA-Z]+$", message = "First name should contain only letters")
	@Size(min=2, message = "First name should be at least two characters")
	private String firstName;
	
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Middle name should contain only letters")
	@Size(min=2, message = "Middle name should be at least two characters")
	private String middleName;
	
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Last name should contain only letters")
	@Size(min=2, message = "Last name should be at least two characters")
	private String lastName;
	
	@Size(min=2, message = "Username name should be at least two characters")
	private String username;
	
	@Size(min=3, message = "Password name should be at least three characters")
	private String password;
	
	@NotBlank
	@Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", 
	message="Please, enter a valid email address")
	private String email;
	
	@NotNull(message = "Please, enter Paid Leave days")
	@Min(value = 0, message = "Paid leave days cannot be less than 0")
	private Integer paidLeaveDaysLeft;
	
	@Size(min=2, message = "Position should be at least two characters")
	private String position;
	
	@NotBlank(message = "Please, select a role")
	@Pattern(regexp = "Manager|User|Admin")
	private String role;
	
	@NotNull(message = "Please, select a manager")
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manager")
	private User manager;
	
	@OneToMany(mappedBy="user")
	private Set<Request> requests = new HashSet<>();
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="authUser")
	private Set<Authority> authorities = new HashSet<>();
	
	public Long getId() {
		return id;
	}
    
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getPaidLeaveDaysLeft() {
		return paidLeaveDaysLeft;
	}

	public void setPaidLeaveDaysLeft(Integer paidLeaveDaysLeft) {
		this.paidLeaveDaysLeft = paidLeaveDaysLeft;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((manager == null) ? 0 : manager.hashCode());
		result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result + ((paidLeaveDaysLeft == null) ? 0 : paidLeaveDaysLeft.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (manager == null) {
			if (other.manager != null)
				return false;
		} else if (!manager.equals(other.manager))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (paidLeaveDaysLeft == null) {
			if (other.paidLeaveDaysLeft != null)
				return false;
		} else if (!paidLeaveDaysLeft.equals(other.paidLeaveDaysLeft))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
