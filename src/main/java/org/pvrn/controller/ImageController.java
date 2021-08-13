package org.pvrn.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.pvrn.exceptions.AlbumNotFound;
import org.pvrn.exceptions.UnauthenticatedUserException;
import org.pvrn.jpa.model.user.EndUser;
import org.pvrn.jpa.model.user.User;
import org.pvrn.service.ImageService;
import org.pvrn.service.UserService;
import org.pvrn.utils.ImageStreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {

	@Autowired
	private UserService userService;
	@Autowired
	private ImageService imageService;

	private EndUser getUser() throws UnauthenticatedUserException {
		UserDetails user = UserService.getAuthenticatedUser();
		return userService.findByUserName(user.getUsername());
	}

	@ResponseBody
	@RequestMapping(value = "/album/image/cover", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchAlbumCoverPhoto(@RequestParam("albumid") long albumId, OutputStream responseOutput)
			throws UnauthenticatedUserException, FileNotFoundException, IOException, AlbumNotFound {
		if (albumId == 0)
			return;
		User user = getUser();
		// Get Image
		ImageInputStream is = ImageIO.createImageInputStream(imageService.fetchCoverPhotoStream(user, albumId));
		ImageStreamUtils.writeImageStreamToResponse(is, responseOutput);
	}

	@ResponseBody
	@GetMapping(value = "/album/image/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchThumbnailPhoto(@RequestParam("mediaid") long mediaId, OutputStream responseOutput)
			throws UnauthenticatedUserException, FileNotFoundException, IOException, AlbumNotFound {
		if (mediaId == 0)
			return;
		User user = getUser();
		ImageInputStream is = ImageIO.createImageInputStream(imageService.fetchImageThumbnailStream(user, mediaId));
		ImageStreamUtils.writeImageStreamToResponse(is, responseOutput);
	}

	@ResponseBody
	@GetMapping(value = "/album/image/full", produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchFullPhoto(@RequestParam("mediaid") long mediaId, OutputStream responseOutput)
			throws UnauthenticatedUserException, FileNotFoundException, IOException, AlbumNotFound {
		if (mediaId == 0)
			return;
		User user = getUser();
		// Get Image
		ImageInputStream is = ImageIO.createImageInputStream(imageService.fetchImageStream(user, mediaId));
		ImageStreamUtils.writeImageStreamToResponse(is, responseOutput);
	}

}
