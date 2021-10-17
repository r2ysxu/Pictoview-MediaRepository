package org.mrn.controller;

import org.mrn.jpa.model.user.EndUser;
import org.mrn.jpa.model.user.NewUser;
import org.mrn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

	@Autowired
	UserService userService;

	@ResponseBody
	@GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public NewUser getProfile() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!(principal instanceof UserDetails) || principal == null)
			return new NewUser();
		EndUser user = userService.findByUserName(((UserDetails) principal).getUsername());
		return new NewUser(user.getUsername());
	}
}
