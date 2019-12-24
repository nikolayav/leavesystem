package com.leavesystem.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.leavesystem.entity.User;
import com.leavesystem.repositories.UserRepository;
import com.leavesystem.service.UserService;

@Controller
public class DashboardController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("user", user);
		return "dashboard";
	}
	
	@GetMapping("/manager-dashboard")
	public String mgrDashboard(@AuthenticationPrincipal User user, ModelMap map) {
		map.addAttribute("user", user);
		return "manager-dashboard";
	}

}