package com.leavesystem.web;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.leavesystem.entity.Request;
import com.leavesystem.entity.RequestStatus;
import com.leavesystem.entity.User;
import com.leavesystem.repositories.UserRepository;
import com.leavesystem.service.LeaveRequestService;
import com.leavesystem.service.SqlDateConvertService;
import com.leavesystem.service.UserService;
import com.leavesystem.web.requestComponents.FormInputs;

import javassist.NotFoundException;

@Controller
public class ManagerDashboardController {

	@Autowired
	private UserService userService;

	@Autowired
	private LeaveRequestService requestService;

	@GetMapping("/manager-dashboard")
	public String mgrDashboard(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("loggedUser", user);
		map.put("message", "Welcome to the Manager Dashboard, " + user.getUsername());
		return "manager-dashboard";
	}

	@RequestMapping(value = "/manager-dashboard/review/{id}", method = RequestMethod.GET)
	public String reviewRequest(@AuthenticationPrincipal User user, ModelMap map, @PathVariable("id") Long id)
			throws NotFoundException {
		map.addAttribute("loggedUser", user);
		map.addAttribute("reviewedRequest", requestService.getRequestById(id));

		return "manager-dashboard/review";
	}

	@RequestMapping(value = "/manager-dashboard/review/{id}", method = RequestMethod.POST)
	public String manageRequest(@AuthenticationPrincipal User user, ModelMap map, Request request)
			throws NotFoundException, GeneralSecurityException, IOException {

		map.addAttribute("loggedUser", user);

		if (request.getStatus() == RequestStatus.Rejected && request.getLeaveType().getType().equals("Paid Leave")) {
			int days = (int) request.calculateTotalDaysOfLeaveSubmit();
			User userToReturnPL = request.getUser();

			userService.returnPaidLeaveDays(userToReturnPL, days);
		}

		map.put("message", requestService.setRequestStatus(request, user));
		return "manager-dashboard";
	}

	@GetMapping("/manager-dashboard/pending")
	public String viewPendingRequests(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("loggedUser", user);
		map.addAttribute("teamMembers", requestService.getTeamMembers(user));
		map.addAttribute("formInputs", new FormInputs());
		map.put("allPendingRequests", requestService.getTeamPendingRequests(user));

		return "manager-dashboard/pending";
	}

	@PostMapping("/manager-dashboard/pending")
	public String getPendingRequests(@AuthenticationPrincipal User user, ModelMap map, FormInputs formInputs) {
		map.addAttribute("loggedUser", user);
		map.addAttribute("teamMembers", requestService.getTeamMembers(user));
		map.put("allPendingRequests", requestService.getUserPendingRequests(formInputs));

		return "/manager-dashboard/pending";
	}
}
