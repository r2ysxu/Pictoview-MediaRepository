package org.mrn.controller;

import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.query.model.MediaItem;
import org.mrn.query.model.PageItems;
import org.mrn.service.AlbumService;
import org.mrn.service.UserService;
import org.mrn.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class VideoController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private VideoService videoService;
	@Autowired
	private AlbumService mediaAlbumService;

	@ResponseBody
	@GetMapping(value = "/album/videos/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageItems<MediaItem> listVideos(@RequestParam(name = "albumId") Long albumId,
			@RequestParam(name = "page") Integer page) throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.unsorted());
		return mediaAlbumService.listVideoMedia(user, albumId, pageable);
	}

	@GetMapping(value = "/album/video")
	public Mono<Resource> getVideo(@RequestParam("mediaid") long mediaId) throws UnauthenticatedUserException {
		return videoService.fetchVideoStream(UserService.getAuthenticatedUser(), mediaId);
	}
}
