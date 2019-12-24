package com.leavesystem.web;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.leavesystem.entity.*;
import com.leavesystem.repositories.*;
import com.leavesystem.service.UserService;

@Controller
public class DashboardController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RequestRepository requestRepo;
	
	@Autowired
	private LeaveTypeRepository leaveTypeRepo;
	
	@GetMapping("/")
	public String rootView( ) {
		return "index";
	}
	
	@GetMapping("/dashboard")
	public String loadLoggedUserData(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("user", user);
		map.addAttribute("request", new Request());
		
		user.setId(userRepo.findByUsername(user.getUsername()).getId()); // This is required because no id is loaded for the CustomSecurityUser object
		map.addAttribute("thisUserRequestList", requestRepo.findByUser(user));
		
		map.addAttribute("leaveTypeList", leaveTypeRepo.findAll());
		return "dashboard";
	}
	
	@PostMapping("/dashboard")
	public String submitLeaveRequest(@AuthenticationPrincipal User user, Request request) {
		
		user.setId(userRepo.findByUsername(user.getUsername()).getId());
		request.setEmployee(user);
		
		request.setCreated(Timestamp.valueOf(LocalDateTime.now()));
		
		requestRepo.save(request);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/manager-dashboard")
	public String mgrDashboard(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("user", user);
		return "manager-dashboard";
	}

}