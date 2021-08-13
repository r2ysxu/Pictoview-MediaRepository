package org.pvrn.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.pvrn.exceptions.UnauthenticatedUserException;
import org.pvrn.jpa.model.user.EndUser;
import org.pvrn.query.model.Category;
import org.pvrn.service.TagService;
import org.pvrn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {

	@Autowired
	private UserService userService;
	@Autowired
	private TagService tagService;

	private EndUser getUser() throws UnauthenticatedUserException {
		UserDetails user = UserService.getAuthenticatedUser();
		return userService.findByUserName(user.getUsername());
	}

	@ResponseBody
	@GetMapping(value = "/album/category/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Category> listCategoryNames() throws UnauthenticatedUserException {
		getUser();
		return tagService.listCategories().stream().map(category -> new Category(category.getId(), category.getName()))
				.collect(Collectors.toList());
	}

	@ResponseBody
	@GetMapping(value = "/album/tag/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> listTagNames(@RequestParam(name = "categoryId") Long categoryId)
			throws UnauthenticatedUserException {
		getUser();
		return tagService.listTags(categoryId).stream().map(tag -> tag.getName()).collect(Collectors.toList());
	}

	@ResponseBody
	@PostMapping(value = "/album/tag/category/create", consumes = MediaType.TEXT_PLAIN_VALUE)
	public Boolean createTagCategory(@RequestBody String categoryName) throws UnauthenticatedUserException {
		getUser();
		tagService.createCategory(categoryName);
		return true;
	}
}
