package com.leavesystem.web;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.leavesystem.entity.Request;
import com.leavesystem.entity.User;
import com.leavesystem.repositories.RequestRepository;
import com.leavesystem.service.LeaveRequestService;
import com.leavesystem.web.requestComponents.FormInputs;

import javassist.NotFoundException;

@Controller
public class DashboardController {

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
	public String submitRequestTemplate(@AuthenticationPrincipal User user, Request request, ModelMap map) throws GeneralSecurityException, IOException{
		map.addAttribute("loggedUser", user);
		request.setUser(user);
		request.setCreated(Timestamp.valueOf(LocalDateTime.now()));
		map.put("message", requestService.submitRequestStatus(request, user));
		
		return "dashboard";
	}
	
	@GetMapping("/dashboard/history")	
	public String viewRequestHistory(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("loggedUser", user);
		map.addAttribute("formInputs", new FormInputs());
		map.put("requestHistory", new ArrayList<Request>());
		
		return "/dashboard/history";
	}
	
	@PostMapping("/dashboard/history")	
	public String submitRequestQuery(@AuthenticationPrincipal User user, ModelMap map, FormInputs formInputs) {
		map.addAttribute("loggedUser", user);
		map.put("requestHistory", requestService.findByUserAndDates(user,
				formInputs.getDateFrom(), formInputs.getDateTo()));
		return "/dashboard/history";
	}
		
	@RequestMapping(path="/dashboard/history/cancel/{id}")
	public String editUserPost(@AuthenticationPrincipal User user, Request request, ModelMap map, @PathVariable("id") Long id) throws NotFoundException, GeneralSecurityException, IOException {
		map.put("message", requestService.statusOfCancelRequest(id));
		map.addAttribute("loggedUser", user);
			
	       return "dashboard";
	    }
	
	@RequestMapping(path = "/dashboard/history/delete/{id}")
	public String deleteSubmittedRequest(@AuthenticationPrincipal User user, ModelMap map, @PathVariable("id") Long id) {
		map.put("message", requestService.statusOfDeleteRequestById(id));
		map.addAttribute("loggedUser", user);
		
		return "dashboard";
	}
}