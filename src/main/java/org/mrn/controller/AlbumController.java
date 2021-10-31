package org.mrn.controller;

import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.query.model.Album;
import org.mrn.query.model.AlbumTag;
import org.mrn.query.model.PageItems;
import org.mrn.service.AlbumService;
import org.mrn.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlbumController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private AlbumService mediaAlbumService;
	@Autowired
	private TagService tagService;

	@ResponseBody
	@GetMapping(value = "/album/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageItems<Album> searchAlbums(@RequestParam(name = "query") String searchString,
			@RequestParam(name = "page") Integer page) throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.unsorted());
		return mediaAlbumService.searchMediaAlbum(user, SearchQuery.parse(searchString), pageable);
	}

	@ResponseBody
	@GetMapping(value = "/album/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageItems<Album> listAlbums(@RequestParam(name = "parentId") Long parentId, @RequestParam(name = "page") Integer page)
			throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.unsorted());
		return mediaAlbumService.listMediaAlbums(user, parentId, pageable);
	}

	@ResponseBody
	@GetMapping(value = "/album/tag/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public AlbumTag listAlbumTags(@RequestParam(name = "albumId") Long albumId) {
		return tagService.listAlbumTags(albumId);
	}

	@ResponseBody
	@PostMapping(value = "/album/tag/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public AlbumTag tagAlbum(@RequestBody AlbumTag albumTag) {
		tagService.tagAlbum(albumTag);
		return tagService.listAlbumTags(albumTag.getAlbumId());
	}
}
