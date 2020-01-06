package com.leavesystem.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.leavesystem.entity.User;
import com.leavesystem.repositories.UserRepository;
import com.leavesystem.service.UserService;

import javassist.NotFoundException;

@Controller
public class AdminDashboardController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
		
	@GetMapping("/admin-dashboard")
	public String showForm(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("loggedUser", user);
	    
	    List<User> userList = new ArrayList<>();
	    userRepo.findAll().forEach(userList::add);
	    model.addAttribute("users", userList);
		
	    return "admin-dashboard";
	}
	
	@GetMapping("/adduser")
	public String showAddUserForm(@AuthenticationPrincipal User loggedUser, Model model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("loggedUser", loggedUser);

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
	public String addUserPost(@Valid User user, BindingResult errors, RedirectAttributes redirectAttributes, Model model, @AuthenticationPrincipal User loggedUser) {
		
		
		if (errors.hasErrors()) {
		    redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", errors);
		    redirectAttributes.addFlashAttribute("createAccountModel", user);
		    model.addAttribute("loggedUser", loggedUser);
		    
			List<User> managerList = new ArrayList<>();
			userRepo.findAllByAuthoritiesAuthority("ROLE_MANAGER").forEach(managerList::add);
			model.addAttribute("managers", managerList);
			
		      return "adduser";
	    }
		
		userService.save(user, user.getRole());
		return "redirect:/admin-dashboard";
	}
	
	@GetMapping("/edit/{id}")
	public String editUserGet(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal User loggedUser) {
		model.addAttribute("loggedUser", loggedUser);
		List<User> managerList = new ArrayList<>();
		userRepo.findAllByAuthoritiesAuthority("ROLE_MANAGER").forEach(managerList::add);
		model.addAttribute("managers", managerList);
		
		for (int i = 0; i < managerList.size(); i++) {
			if (id == managerList.get(i).getId()) {
				managerList.remove(i);
				break;
			}
		}
		
		model.addAttribute("user", userRepo.findById(id));
	    return "edituser";
	}
	
	@PostMapping(value="/edit/{id}")
	public String editUserPost(@Valid @ModelAttribute User user, BindingResult errors, Model model, Long id, @AuthenticationPrincipal User loggedUser) throws NotFoundException {
		model.addAttribute("loggedUser", loggedUser);
	    if (errors.hasErrors()) {
			List<User> managerList = new ArrayList<>();
			userRepo.findAllByAuthoritiesAuthority("ROLE_MANAGER").forEach(managerList::add);
			model.addAttribute("managers", managerList);
			model.addAttribute("loggedUser", loggedUser);
			
			for (int i = 0; i < managerList.size(); i++) {
				if (id == managerList.get(i).getId()) {
					managerList.remove(i);
					break;
				}
			}
			
	       return "edituser";
	    }
	    
	    userService.update(user, user.getRole());
	    return "redirect:/admin-dashboard";
	}
	
	@GetMapping("/pwreset/{id}")
	public String pwResetGet(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal User loggedUser) {
		List<User> managerList = new ArrayList<>();
		userRepo.findAllByAuthoritiesAuthority("ROLE_MANAGER").forEach(managerList::add);
		model.addAttribute("managers", managerList);
		model.addAttribute("loggedUser", loggedUser);
		
		model.addAttribute("user", userRepo.findById(id));
	    return "pwreset";
	}
	
	@PostMapping(value="/pwreset/{id}")
	public String pwResetPost(@Valid @ModelAttribute User user, BindingResult errors, Model model, Long id) throws NotFoundException {
		
	    if (errors.hasErrors()) {
			List<User> managerList = new ArrayList<>();
			userRepo.findAllByAuthoritiesAuthority("ROLE_MANAGER").forEach(managerList::add);
			model.addAttribute("managers", managerList);
			
	       return "pwreset";
	    }
	    
	    userService.update(user, user.getRole());
	    return "redirect:/admin-dashboard";
	}
	
}