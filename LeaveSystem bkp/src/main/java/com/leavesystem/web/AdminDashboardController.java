package com.leavesystem.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.leavesystem.entity.User;
import com.leavesystem.repositories.UserRepository;
import com.leavesystem.service.UserService;

@Controller
public class AdminDashboardController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/")
	public String rootView( ) {
		return "index";
	}
	
	@GetMapping(value = { "/admin-dashboard" })
	public String showForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
	    
	    List<User> userList = new ArrayList<>();
	    userRepo.findAll().forEach(userList::add);
	    model.addAttribute("users", userList);
		
	    return "admin-dashboard";
	}
	
	@GetMapping("/adduser")
	public String showAddUserForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);

		List<User> managerList = new ArrayList<>();
		userRepo.findAllByAuthoritiesAuthority("ROLE_MANAGER").forEach(managerList::add);
		model.addAttribute("managers", managerList);
	    
	    return "adduser";
	}
	
	@RequestMapping(path = "/delete/{id}")
    public String deleteUserById(Model model, @PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin-dashboard";
    }
	
	@PostMapping("/adduser")
	public String addUserPost(@Valid User user, BindingResult errors, RedirectAttributes redirectAttributes, Model model) {
		if (errors.hasErrors()) {
		    redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", errors);
		    redirectAttributes.addFlashAttribute("createAccountModel", user);
		    
			List<User> managerList = new ArrayList<>();
			userRepo.findAllByAuthoritiesAuthority("ROLE_MANAGER").forEach(managerList::add);
			model.addAttribute("managers", managerList);
			
		      return "adduser";
	    }
		
		userService.save(user, user.getRole());
		return "redirect:/admin-dashboard";
	}
	
	@GetMapping(value="/edit/{id}")
	public String editUserGet(@PathVariable("id") Long id, Model model) {
		List<User> managerList = new ArrayList<>();
		userRepo.findAllByAuthoritiesAuthority("ROLE_MANAGER").forEach(managerList::add);
		model.addAttribute("managers", managerList);
		
		model.addAttribute("user", userRepo.findById(id));
	    return "edituser";
	}
	
	@PostMapping(value="/edit/{id}")
	public String editUserPost(@ModelAttribute("edituser") User user, BindingResult result) {
	    if (result.hasErrors()) {
	        return "edit";
	    }
	    
	    userService.save(user, user.getRole());
	    return "redirect:/admin-dashboard";
	}
}