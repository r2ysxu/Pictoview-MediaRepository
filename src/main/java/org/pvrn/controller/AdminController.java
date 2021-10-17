package org.pvrn.controller;

import java.io.IOException;
import java.util.List;

import org.pvrn.exceptions.UnauthenticatedUserException;
import org.pvrn.jpa.model.album.MediaAlbum;
import org.pvrn.jpa.model.album.ImageMedia;
import org.pvrn.jpa.model.user.EndUser;
import org.pvrn.query.model.ScannedDirectory;
import org.pvrn.service.DirectoryService;
import org.pvrn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

	@Autowired
	private DirectoryService directoryService;

	@Autowired
	UserService userService;

	private static class Path {
		private String path;
		private String name;

		public String getPath() {
			return path;
		}

		public String getName() {
			return name;
		}
	}

	public EndUser getUser() throws UnauthenticatedUserException {
		UserDetails user = UserService.getAuthenticatedUser();
		return userService.findByUserName(user.getUsername());
	}

	@ResponseBody
	@PostMapping(value = "/admin/files/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ScannedDirectory> getFileList(@RequestBody Path currentPath) throws UnauthenticatedUserException {
		getUser();
		return directoryService.getScannedFiles(currentPath.getPath());
	}

	@ResponseBody
	@PostMapping(value = "/admin/files/add/image", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ScannedDirectory addImageAlbum(@RequestBody Path currentPath)
			throws UnauthenticatedUserException, IOException {
		EndUser user = getUser();
		MediaAlbum imageAlbum = directoryService.addImageDirectory(user, currentPath.getPath(), currentPath.getName());
		List<ImageMedia> imageMedia = directoryService.addImages(user, currentPath.getPath(), imageAlbum);
		directoryService.createImageThumbnails(imageAlbum, imageMedia);
		imageAlbum = directoryService.setAlbumCoverPhoto(imageAlbum);
		return directoryService.addScannedDirectory(currentPath.getPath(), imageAlbum);
	}
}
