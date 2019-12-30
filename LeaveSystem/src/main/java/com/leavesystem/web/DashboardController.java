package com.leavesystem.web;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.api.client.util.DateTime;
import com.leavesystem.entity.*;
import com.leavesystem.repositories.*;
import com.leavesystem.service.GoogleCal;
import com.leavesystem.service.UserService;
import com.leavesystem.entity.Request;
import com.leavesystem.entity.User;
import com.leavesystem.repositories.RequestRepository;
import com.leavesystem.service.LeaveRequestService;
import com.leavesystem.web.requestComponents.FormInputs;

@Controller
public class DashboardController {
	
	@Autowired
	private RequestRepository requestRepository;

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
		request.setEmployee(user);
		request.setCreated(Timestamp.valueOf(LocalDateTime.now()));
		requestService.submitRequest(request, user);
		map.put("message", "Request successfully submitted!");
		
//		request.setCreated(Timestamp.valueOf(LocalDateTime.now()));
//		
//		Request newRequest = requestRepo.save(request);
//		System.out.println(newRequest.getDate_from());
//		
//		System.out.println(newRequest.getDate_to());;

//		return "redirect:/dashboard";
		
		return "dashboard";
	}
	
	@GetMapping("/dashboard/history")	
	public String viewRequestHistory(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("loggedUser", user);
		map.addAttribute("formInputs", new FormInputs());
		map.put("requestHistory", requestService.getAllRequests(user));
		map.put("deleteInputs", new FormInputs());
		
		return "/dashboard/history";
	}
	
	@PostMapping("/dashboard/history")	
	public String submitRequestQuery(@AuthenticationPrincipal User user, ModelMap map, FormInputs formInputs) {
		map.addAttribute("loggedUser", user);
		map.put("requestHistory", requestRepository.findByUserAndDates(user,
				formInputs.getDateFrom(), formInputs.getDateTo()));
		map.put("deleteInputs", new FormInputs());
		return "/dashboard/history";
	}
	
	@PostMapping("/dashboard/history/delete/{id}")	
	public String deleteSubmittedRequest(ModelMap map, @PathVariable("id") Long id) {
		map.put("message", requestService.statusOfDeleteRequestById(id));
		
		return "/dashboard/history";
	}
}