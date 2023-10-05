package org.mrn.controller;

import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.query.model.AudioMediaItem;
import org.mrn.query.model.PageItems;
import org.mrn.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AudioController extends BaseController {

	private static final int PAGE_SIZE = 50;

	@Autowired
	private AlbumService mediaAlbumService;

	@ResponseBody
	@GetMapping(value = "/album/audio/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageItems<AudioMediaItem> listVideos(@RequestParam(name = "albumId") Long albumId,
			@RequestParam(name = "page") Integer page) throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("trackNumber").ascending());
		return mediaAlbumService.listAudioMedia(user, albumId, pageable);
	}
}
