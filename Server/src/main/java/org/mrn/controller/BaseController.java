package org.mrn.controller;

import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class BaseController {

	@Autowired
	protected UserService userService;

	protected EndUserEntity getUser() throws UnauthenticatedUserException {
		UserDetails user = UserService.getAuthenticatedUser();
		return userService.findByUserName(user.getUsername());
	}
}
