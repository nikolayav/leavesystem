package com.leavesystem.web;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.leavesystem.entity.Request;
import com.leavesystem.entity.RequestStatus;
import com.leavesystem.entity.User;
import com.leavesystem.repositories.RequestRepository;
import com.leavesystem.repositories.UserRepository;
import com.leavesystem.service.LeaveRequestService;
import com.leavesystem.service.SqlDateConvertService;
import com.leavesystem.service.UserService;
import com.leavesystem.web.requestComponents.FormInputs;

import javassist.NotFoundException;

@Controller
public class DashboardController {

	@Autowired
	private SqlDateConvertService dateConvert;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private RequestRepository requestRepo;

	@Autowired
	private LeaveRequestService requestService;

	@GetMapping("/dashboard")
	public String loadDashboard(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("loggedUser", user);
		map.put("message", "Welcome to the Leave request system, " + user.getUsername());

		return "dashboard";
	}

	@GetMapping("/dashboard/newrequest")
	public String loadRequestTemplate(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("loggedUser", user);
		map.addAttribute("request", new Request());
		map.addAttribute("leaveTypeList", requestService.getAllLeaveTypes());
		map.addAttribute("userList", requestService.getAllOtherEmployees(user.getId()));

		return "/dashboard/newrequest";
	}

	@PostMapping("/dashboard/newrequest")
	public String submitRequestTemplate(@AuthenticationPrincipal User user, Request request, ModelMap map)
			throws GeneralSecurityException, IOException {
		map.addAttribute("loggedUser", user);

		Optional<User> userOpt = userRepo.findById(user.getId());
		User currentUser = null;
		if (userOpt.isPresent()) {
			currentUser = userOpt.get();
		} else {
			map.put("message", "Error submitting the request! Such user does not exist!");
			return "dashboard";
		}

		request.setUser(currentUser);
		request.setCreated(Timestamp.valueOf(LocalDateTime.now()));
		map.put("message", requestService.submitRequestStatus(request, currentUser));

		return "dashboard";
	}

	@GetMapping("/dashboard/history")
	public String viewRequestHistory(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("loggedUser", user);
		map.addAttribute("formInputs", new FormInputs());
		map.put("requestHistory", requestService.getAllRequests(user));

		return "/dashboard/history";
	}

	@PostMapping("/dashboard/history")
	public String submitRequestQuery(@AuthenticationPrincipal User user, ModelMap map, FormInputs formInputs) {
		map.addAttribute("loggedUser", user);
		map.put("requestHistory",
				requestService.findByUserAndDates(user, formInputs.getDateFrom(), formInputs.getDateTo()));
		return "/dashboard/history";
	}

	@RequestMapping(path = "/dashboard/history/cancel/{id}")
	public String editUserPost(@AuthenticationPrincipal User user, Request request, ModelMap map,
			@PathVariable("id") Long id) throws NotFoundException, GeneralSecurityException, IOException {

		map.addAttribute("loggedUser", user);

		Request currentRequest = null;
		Optional<Request> requestOpt = requestRepo.findById(id);
		if (requestOpt.isPresent()) {
			currentRequest = requestOpt.get();
		} else {
			map.put("message", "Error canceling the request. Request not found in database!");
			return "dashboard";
		}

		if (currentRequest.getLeaveType().getType().equals("Paid Leave") && currentRequest.getStatus() == RequestStatus.Submitted) {
			int days = dateConvert.calculateTotalDaysOfLeaveReturn(currentRequest);
			userService.returnPaidLeaveDays(user, days);
		}

		map.put("message", requestService.statusOfCancelRequest(id));
		return "dashboard";
	}

	@RequestMapping(path = "/dashboard/history/delete/{id}")
	public String deleteSubmittedRequest(@AuthenticationPrincipal User user, ModelMap map,
			@PathVariable("id") Long id) {
		map.addAttribute("loggedUser", user);

		Request currentRequest = null;
		Optional<Request> requestOpt = requestRepo.findById(id);
		if (requestOpt.isPresent()) {
			currentRequest = requestOpt.get();
		} else {
			map.put("message", "Error deleting the request. Request not found in database!");
			return "dashboard";
		}

		if (currentRequest.getLeaveType().getType().equals("Paid Leave") && currentRequest.getStatus() == RequestStatus.Submitted) {
			int days = dateConvert.calculateTotalDaysOfLeaveReturn(currentRequest);
			userService.returnPaidLeaveDays(user, days);
		}

		map.put("message", requestService.statusOfDeleteRequestById(id));
		return "dashboard";
	}
}