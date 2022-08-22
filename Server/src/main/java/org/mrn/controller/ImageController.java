package org.mrn.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.stream.ImageInputStream;

import org.mrn.exceptions.AlbumNotFound;
import org.mrn.exceptions.ImageNotFound;
import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.mrn.query.model.MediaItem;
import org.mrn.query.model.PageItems;
import org.mrn.service.AlbumService;
import org.mrn.service.ImageService;
import org.mrn.utils.ImageStreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController extends BaseController {

	private static final int PAGE_SIZE = 50;

	@Autowired
	private ImageService imageService;
	@Autowired
	private AlbumService mediaAlbumService;

	@ResponseBody
	@GetMapping(value = "/album/images/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageItems<MediaItem> listPhotos(@RequestParam(name = "albumId") Long albumId,
			@RequestParam(name = "page") Integer page) throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.unsorted());
		return mediaAlbumService.listImageMedia(user, albumId, pageable);
	}

	@ResponseBody
	@RequestMapping(value = "/album/image/cover", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchAlbumCoverPhoto(@RequestParam("albumid") long albumId, OutputStream responseOutput)
			throws UnauthenticatedUserException, IOException, AlbumNotFound, ImageNotFound {
		if (albumId == 0) return;
		UserEntity user = getUser();
		ImageInputStream is = imageService.fetchCoverPhotoStream(user, albumId);
		if (is == null) return;
		ImageStreamUtils.writeImageStreamToResponse(is, responseOutput);
	}

	@ResponseBody
	@GetMapping(value = "/album/image/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchThumbnailPhoto(@RequestParam("mediaid") long mediaId, OutputStream responseOutput)
			throws UnauthenticatedUserException, IOException, AlbumNotFound, ImageNotFound {
		if (mediaId == 0) return;
		UserEntity user = getUser();
		ImageInputStream is = imageService.fetchImageThumbnailStream(user, mediaId);
		if (is == null) return;
		ImageStreamUtils.writeImageStreamToResponse(is, responseOutput);
	}

	@ResponseBody
	@GetMapping(value = "/album/image/full", produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchFullPhoto(@RequestParam("mediaid") long mediaId, OutputStream responseOutput)
			throws UnauthenticatedUserException, IOException, AlbumNotFound, ImageNotFound {
		if (mediaId == 0) return;
		UserEntity user = getUser();
		ImageInputStream is = imageService.fetchImageStream(user, mediaId);
		if (is == null) return;
		ImageStreamUtils.writeImageStreamToResponse(is, responseOutput);
	}
}
