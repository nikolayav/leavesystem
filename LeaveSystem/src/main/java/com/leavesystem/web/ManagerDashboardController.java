package com.leavesystem.web;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.leavesystem.entity.Request;
import com.leavesystem.entity.User;
import com.leavesystem.service.LeaveRequestService;
import com.leavesystem.web.requestComponents.FormInputs;

import javassist.NotFoundException;

@Controller
public class ManagerDashboardController {

	@Autowired
	private LeaveRequestService requestService;
	
	@GetMapping("/manager-dashboard")
	public String mgrDashboard(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("loggedUser", user);
		
		return "manager-dashboard";
	}
	
	@GetMapping("/manager-dashboard/review")
	public String reviewRequest(@AuthenticationPrincipal User user, ModelMap map, Request request) {
		map.addAttribute("loggedUser", user);
		map.addAttribute("reviewedRequest", request);
		map.addAttribute("formInputs", new FormInputs());
		
		return "manager-dashboard/review";
	}
	
	@PostMapping("/manager-dashboard/review")
	public String manageRequest(@AuthenticationPrincipal User user, ModelMap map, Request request)
			throws NotFoundException, GeneralSecurityException, IOException {
		
		map.put("message", requestService.setRequestStatus(request, user));
		
		return "manager-dashboard";
	}
	
	@GetMapping("/manager-dashboard/pending")
	public String viewPendingRequests(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("loggedUser", user);
		map.addAttribute("formInputs", new FormInputs());
		
		return "manager-dashboard/pending";
	}
	
	@PostMapping("/manager-dashboard/pending")
	public String getPendingRequests(@AuthenticationPrincipal User user, ModelMap map, FormInputs formInputs) {
		map.addAttribute("allPendingRequests", requestService.getTeamRequests(user, formInputs));
		map.addAttribute("specificRequests", requestService.getUserRequests(formInputs));
		
		return "/manager-dashboard/pending";
	}
}
