package org.pvrn.controller;

import java.util.ArrayList;
import java.util.List;

import org.pvrn.exceptions.UnauthenticatedUserException;
import org.pvrn.jpa.model.album.ImageAlbum;
import org.pvrn.jpa.model.tags.SearchQuery;
import org.pvrn.jpa.model.user.EndUser;
import org.pvrn.query.model.Album;
import org.pvrn.query.model.AlbumTag;
import org.pvrn.service.ImageAlbumService;
import org.pvrn.service.TagService;
import org.pvrn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageAlbumController {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private UserService userService;
	@Autowired
	private ImageAlbumService imageAlbumService;
	@Autowired
	private TagService tagService;

	private EndUser getUser() throws UnauthenticatedUserException {
		UserDetails user = UserService.getAuthenticatedUser();
		return userService.findByUserName(user.getUsername());
	}

	private List<Album> convertImageAlbumToQueryAlbums(Iterable<ImageAlbum> imageAlbums) {
		List<Album> albums = new ArrayList<>();
		imageAlbums.forEach(imageAlbum -> {
			albums.add(Album.createImageAlbum(imageAlbum));
		});
		return albums;
	}

	@ResponseBody
	@GetMapping(value = "/album/image/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Album> searchAlbums(@RequestParam(name = "query") String searchString)
			throws UnauthenticatedUserException {
		EndUser user = getUser();
		Iterable<ImageAlbum> imageAlbums = imageAlbumService.searchImageAlbum(user, SearchQuery.parse(searchString),
				null);
		return convertImageAlbumToQueryAlbums(imageAlbums);
	}

	@ResponseBody
	@GetMapping(value = "/album/image/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Album> listAlbums(@RequestParam(name = "parentId") Long parentId,
			@RequestParam(name = "page") Integer page) throws UnauthenticatedUserException {
		EndUser user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.unsorted());
		Iterable<ImageAlbum> imageAlbums = imageAlbumService.listImageAlbums(user, parentId, pageable);
		return convertImageAlbumToQueryAlbums(imageAlbums);
	}

	@ResponseBody
	@GetMapping(value = "/album/image/photos/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Long> listPhotos(@RequestParam(name = "albumId") Long albumId,
			@RequestParam(name = "page") Integer page) throws UnauthenticatedUserException {
		EndUser user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.unsorted());
		return imageAlbumService.listImageMedia(user, albumId, pageable);
	}
	
	@ResponseBody
	@PostMapping(value = "/album/image/tag/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AlbumTag listAlbumTags(@RequestParam(name = "albumid") Long albumId) {
		return tagService.listAlbumTags(albumId);
	}

	@ResponseBody
	@PostMapping(value = "/album/image/tag/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean tagAlbum(@RequestBody AlbumTag albumTag) {
		tagService.tagAlbum(albumTag);
		return true;
	}
}
