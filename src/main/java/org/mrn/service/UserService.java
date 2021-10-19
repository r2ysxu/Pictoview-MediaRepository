package org.mrn.service;

import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.exceptions.UserExistsException;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.jpa.model.user.NewUser;
import org.mrn.jpa.model.user.UserEntity;
import org.mrn.jpa.repo.EndUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	EndUserRepo endUsersRepo;

	public static UserDetails getAuthenticatedUser() throws UnauthenticatedUserException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!(principal instanceof UserDetails) || principal == null)
			throw new UnauthenticatedUserException();
		return (UserDetails) principal;
	}

	public EndUserEntity findByUserName(String username) {
		return endUsersRepo.findByUsername(username);
	}

	public EndUserEntity registerNewUser(NewUser newUser) throws UserExistsException {
		UserEntity user = endUsersRepo.findByUsername(newUser.getUsername());
		if (user != null)
			throw new UserExistsException(newUser.getUsername());
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return endUsersRepo.save(EndUserEntity.create(newUser.getUsername(), encoder.encode(newUser.getPassword())));
	}
}