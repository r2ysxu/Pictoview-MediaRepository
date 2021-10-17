package org.mrn.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.jpa.model.user.EndUser;
import org.mrn.query.model.Category;
import org.mrn.query.model.Tag;
import org.mrn.service.TagService;
import org.mrn.service.UserService;
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

	private static final int PAGE_SIZE = 10;

	@Autowired
	private UserService userService;
	@Autowired
	private TagService tagService;

	private EndUser getUser() throws UnauthenticatedUserException {
		UserDetails user = UserService.getAuthenticatedUser();
		return userService.findByUserName(user.getUsername());
	}

	@ResponseBody
	@GetMapping(value = "/category/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Category> listCategoryNames() throws UnauthenticatedUserException {
		getUser();
		return tagService.listCategories().stream().map(category -> new Category(category.getId(), category.getName()))
				.collect(Collectors.toList());
	}

	@ResponseBody
	@GetMapping(value = "/category/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Category> searchCategoryNames(@RequestParam(name = "categoryQuery") String categoryQuery,
			@RequestParam(name = "page", required = false) Integer page) throws UnauthenticatedUserException {
		getUser();
		if (page == null) page = 0;
		return tagService.searchCategories(categoryQuery, PAGE_SIZE, page).stream()
				.map(category -> new Category(category.getId(), category.getName())).collect(Collectors.toList());
	}

	@ResponseBody
	@GetMapping(value = "/tag/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Tag> searchTagName(@RequestParam(name = "tagQuery") String tagQuery,
			@RequestParam(name = "page", required = false) Integer page) throws UnauthenticatedUserException {
		getUser();
		if (page == null) page = 0;
		return tagService.searchTags(tagQuery, PAGE_SIZE, page).stream().map(tag -> Tag.createTag(tag))
				.collect(Collectors.toList());
	}

	@ResponseBody
	@GetMapping(value = "/tag/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Tag> listTagNames(@RequestParam(name = "categoryId") Long categoryId) throws UnauthenticatedUserException {
		getUser();
		return tagService.listTags(categoryId).stream().map(tag -> Tag.createTag(tag)).collect(Collectors.toList());
	}

	@ResponseBody
	@PostMapping(value = "/tag/category/create", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Category createTagCategory(@RequestBody String categoryName) throws UnauthenticatedUserException {
		getUser();
		return Category.createCategory(tagService.createCategory(categoryName));
	}
}
