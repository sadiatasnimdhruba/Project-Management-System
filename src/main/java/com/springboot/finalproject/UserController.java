package com.springboot.finalproject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")

public class UserController {

    @Autowired
	private UserService userService;

    public UserController(UserService userService) {
		super();
		this.userService = userService;
	}


	@GetMapping("/")
	public String home(Model model) {

    	return "redirect:/projects";
	}


	@GetMapping("/login")
	public String loginForm( Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "login";
	}

	@PostMapping("/login")
	public String loginPost() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		User loggedInUser = userService.getUserByEmail(currentPrincipalName);
		Long userId = loggedInUser.getId();
    	return "redirect:/projects";
	}

	@PostMapping("/logout")
	public String logoutPost() {
		return "redirect:/login?logout";
	}

	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}

	@GetMapping("/registration")
	public String showRegistrationForm() {
		return "registration";
	}

	@PostMapping("/registration")
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {


		String username = registrationDto.getUsername();
		String email = registrationDto.getEmail();

		if (userService.findByUsername(username)) {
			return "redirect:/registration?username";
		}

		if (userService.getUserByEmail(email) != null) {
			return "redirect:/registration?email";
		}

		userService.save(registrationDto);
		return "redirect:/registration?success";
	}


}
