package com.leavesystem.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

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
		return userRepo.findAllByRoleInAndIdNotIn(Arrays.asList("USER","MANAGER"), Arrays.asList(id));
	}
	
	public void submitRequest(Request request, User user) {
		requestRepo.save(request);
		// call mailServerSendMail(Request request, User user, User user.getManager());
	}
	
	public List<Request> getAllRequests(User user) {
		return requestRepo.findByUser(user);
	}
	
	public String statusOfDeleteRequestById(Long id) {
		if (!requestRepo.existsById(id)) {
			return "Error deleting selected request: Request with id: " + id + " does not exist!";
		}
		RequestStatus requestStatus = requestRepo.findById(id).get().getStatus();
		if(requestStatus != RequestStatus.Submitted) {
			return "Error deleting selected request: Request with id: " + id + " has already been "
					+ requestStatus.toString() + " by the responsible manager!";
		}
		requestRepo.deleteById(id);
		return "Request with id: " + id + " successfully deleted!";
	}

	public String setRequestStatus(Request request, User manager) throws NotFoundException, GeneralSecurityException, IOException {
		if(!requestRepo.existsById(request.getId())) {
			throw new NotFoundException("Error during request processing: request not found");
		}
		requestRepo.save(request);
		
		DateTime dt1 = new DateTime(request.getDateFrom());
		DateTime dt2 = new DateTime(request.getDateTo());
		GoogleCal googleCal = new GoogleCal();
		googleCal.createEvent(request.getEmployee().getFirstName() + " " + request.getEmployee().getLastName(), dt1,dt2);
		// call mailServerSendMail(Request request, User request.getEmployee(), User manager);
		return "Request (id: " + request.getId() + ") status set to " + request.getStatus();
	}
	
	public List<Request> getTeamRequests(User user, FormInputs formInputs) {
		return requestRepo.findByUserListAndDates(userRepo.findAllByManager(user), formInputs.getDateFrom(), formInputs.getDateTo());
	}
	
	public List<Request> getUserRequests(FormInputs formInputs) {
		return requestRepo.findByUserAndDates(formInputs.getUser(), formInputs.getDateFrom(), formInputs.getDateTo());
	}
}
