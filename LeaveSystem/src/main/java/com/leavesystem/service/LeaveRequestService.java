package com.leavesystem.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
//import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;
import com.leavesystem.entity.*;
import com.leavesystem.repositories.*;
import com.leavesystem.web.requestComponents.FormInputs;

import javassist.NotFoundException;

@Service
public class LeaveRequestService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RequestRepository requestRepo;

	@Autowired
	private LeaveTypeRepository leaveTypeRepo;

	public List<LeaveType> getAllLeaveTypes() {
		return (List<LeaveType>) leaveTypeRepo.findAll();
	}

	public List<User> getAllOtherEmployees(Long id) {
		return userRepo.findAllByRoleInAndIdNotIn(Arrays.asList("USER", "MANAGER"), Arrays.asList(id));
	}

	public String submitRequestStatus(Request request, User user) throws GeneralSecurityException, IOException {
		if (request.getDateFrom() == null || request.getDateTo() == null) {
			return "Request submit failed! Please, fill both From date and To date fields!";
		} else if(request.getDateTo().before(request.getDateFrom())) {
			return "Request submit failed! Date to must be equal to or after Date from."; 
		}
		
		int workingDays = (int) request.calculateTotalDaysOfLeaveSubmit();
		int paidLeaveDays = user.getPaidLeaveDaysLeft();
		
		if (request.getLeaveType().getType().equals("Paid Leave") && workingDays > paidLeaveDays) {
			return "Request submit failed! Your selected period exceeds the paid leave days you have.\n "
					+ "You have " + user.getPaidLeaveDaysLeft() + " paid leave days left.";
		}
		
		user.setPaidLeaveDaysLeft(paidLeaveDays - workingDays);
		
		requestRepo.save(request);
		userRepo.save(user); // updates Paid Leave Days Left for the current User submitting the request
		
		return "Request successfully submitted!";
	}

	public Request getRequestById(Long id) throws NotFoundException {
		if (requestRepo.existsById(id)) {
			return requestRepo.findById(id).get();
		} else {
			throw new NotFoundException("Error during request processing: request not found");
		}
	}

	public List<User> getTeamMembers(User manager) {
		return userRepo.findAllByManager(manager);
	}

	public List<Request> getAllRequests(User user) {
		return requestRepo.findByUser(user);
	}

	public List<Request> findByUserAndDates(User user, java.util.Date dateFrom, java.util.Date dateTo) {
		if (dateFrom == null || dateTo == null) {
			return new ArrayList<Request>();
		}
		return requestRepo.findByUserAndDates(user, dateFrom, dateTo);
	}

	public String statusOfDeleteRequestById(Long id) {
		if (!requestRepo.existsById(id)) {
			return "Error deleting selected request: Request with id: " + id + " does not exist!";
		}
		
		RequestStatus requestStatus = requestRepo.findById(id).get().getStatus();
		if (requestStatus != RequestStatus.Submitted) {
			return "Error deleting selected request: Only requests in status Submitted can be deleted!";
		}
		requestRepo.deleteById(id);

		return "Request with id: " + id + " successfully deleted!";
	}
	
	public String statusOfCancelRequest(Long id) throws GeneralSecurityException, IOException {
		
		Optional<Request> leaveRequestOpt = requestRepo.findById(id);
		Request leaveRequest = null;
		if(leaveRequestOpt.isPresent()) {
			leaveRequest = leaveRequestOpt.get();
			
			if (leaveRequest.getStatus() == RequestStatus.Rejected) {
				return "Error cancelling selected request: Request with id: " + id + " is in status Rejected";
				
			} else if (leaveRequest.getStatus() == RequestStatus.Cancelled)
				return "Request with id: " + id + " has already been cancelled";
			
			if (leaveRequest.getStatus() == RequestStatus.Accepted) {
				GoogleCal googleCal = new GoogleCal();
				try {
					googleCal.deleteEvent(leaveRequest.getGoogleCalendarEventId());
				} catch (Exception e) {
					System.out.println("No Google Id recorded for this request!\n" + e.getMessage());
				}
			}
			
			leaveRequest.setStatus(RequestStatus.Cancelled);
		} else {
			return "Error cancelling selected request: Request with id: " + id + " does not exist!";
		}
		
		requestRepo.save(leaveRequest);
		return "Request with id: " + id + " successfully cancelled!";
	}

	public String setRequestStatus(Request request, User manager)
			throws NotFoundException, GeneralSecurityException, IOException {
		if (!requestRepo.existsById(request.getId())) {
			throw new NotFoundException("Error during request processing: request not found");
		}
		if (request.getStatus() == RequestStatus.Submitted) {
			return "Request (id: " + request.getId() + ") status left unchanged.";
		}

		if (request.getStatus() == RequestStatus.Accepted) {

			Instant dateToInstant = request.getDateTo().toInstant().plusSeconds(86399);
			
			Date updatedDateTo = Date.from(dateToInstant);
			
			DateTime dt1 = new DateTime(request.getDateFrom());
			DateTime dt2 = new DateTime(updatedDateTo);
			GoogleCal googleCal = new GoogleCal();

			request.setGoogleCalendarEventId(googleCal.createEvent(request.getUser().getFirstName() + " "
					+ request.getUser().getMiddleName() + " " + request.getUser().getLastName(), dt1, dt2));
		}
		requestRepo.save(request);
		// call mailServerSendMail(Request request, User request.getEmployee(), User manager);
		return "Request (id: " + request.getId() + ") status set to " + request.getStatus();
	}

	public List<Request> getUserPendingRequests(FormInputs formInputs) {
		if (formInputs.getDateFrom() == null || formInputs.getDateTo() == null) {
			return new ArrayList<Request>();
		}
		return requestRepo.findByUserListStatusAndDates(Arrays.asList(formInputs.getUser()),
				Arrays.asList(RequestStatus.Submitted), formInputs.getDateFrom(), formInputs.getDateTo());
	}
	
	public List<Request> getTeamPendingRequests(User manager) {
		return requestRepo.findByStatusInAndUserIn(Arrays.asList(RequestStatus.Submitted), 
				userRepo.findAllByManager(manager));
	}

	
}
