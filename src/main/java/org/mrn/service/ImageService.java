package org.mrn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.mrn.exceptions.AlbumNotFound;
import org.mrn.jpa.model.album.MediaAlbum;
import org.mrn.jpa.model.album.ImageMedia;
import org.mrn.jpa.model.user.User;
import org.mrn.jpa.repo.MediaAlbumRepo;
import org.mrn.jpa.repo.ImageMediaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

	@Value("${app.admin.main.cover.source}")
	private String adminCoverSource;

	@Autowired
	private MediaAlbumRepo imageAlbumRepo;
	@Autowired
	private ImageMediaRepo imageMediaRepo;

	public InputStream fetchCoverPhotoStream(User owner, Long albumId) throws FileNotFoundException, AlbumNotFound {
		MediaAlbum album = imageAlbumRepo.findByOwnerAndId(owner, albumId);
		if (album == null)
			throw new AlbumNotFound(owner, albumId);
		ImageMedia imageMedia = imageMediaRepo.findFirstByAlbumOrderByNameAsc(album);
		return new FileInputStream(
				new File(DirectoryService.generateCoverPhotoFileSource(adminCoverSource, album, imageMedia)));
	}

	public InputStream fetchImageThumbnailStream(User owner, Long mediaId) throws FileNotFoundException {
		ImageMedia imageMedia = imageMediaRepo.findByOwnerAndId(owner, mediaId);
		return new FileInputStream(new File(imageMedia.getThumbnailSource()));
	}

	public InputStream fetchImageStream(User owner, Long mediaId) throws FileNotFoundException {
		ImageMedia imageMedia = imageMediaRepo.findByOwnerAndId(owner, mediaId);
		return new FileInputStream(new File(imageMedia.getSource()));
	}
}
