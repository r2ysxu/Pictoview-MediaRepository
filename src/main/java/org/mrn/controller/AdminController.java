package org.mrn.controller;

import java.io.IOException;
import java.util.List;

import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.query.model.ScannedDirectory;
import org.mrn.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController extends BaseController {

	@Autowired
	private DirectoryService directoryService;

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

	@ResponseBody
	@PostMapping(value = "/admin/files/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ScannedDirectory> getFileList(@RequestBody Path currentPath) throws UnauthenticatedUserException {
		getUser();
		return directoryService.getScannedFiles(currentPath.getPath());
	}

	@ResponseBody
	@PostMapping(value = "/admin/files/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ScannedDirectory addImageAlbum(@RequestBody Path currentPath)
			throws UnauthenticatedUserException, IOException {
		EndUserEntity user = getUser();
		AlbumEntity mediaAlbum = directoryService.addImageDirectory(user, currentPath.getPath(), currentPath.getName());
		List<ImageMediaEntity> imageMedia = directoryService.addImages(user, currentPath.getPath(), mediaAlbum);
		directoryService.createImageThumbnails(mediaAlbum, imageMedia);
		directoryService.addVideos(user, currentPath.getPath(), mediaAlbum);
		mediaAlbum = directoryService.setAlbumCoverPhoto(mediaAlbum);
		return directoryService.addScannedDirectory(currentPath.getPath(), mediaAlbum);
	}
}
