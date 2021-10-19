package org.mrn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.mrn.exceptions.AlbumNotFound;
import org.mrn.jpa.model.album.MediaAlbumEntity;
import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.jpa.model.user.UserEntity;
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

	public InputStream fetchCoverPhotoStream(UserEntity owner, Long albumId) throws FileNotFoundException, AlbumNotFound {
		MediaAlbumEntity album = imageAlbumRepo.findByOwnerAndId(owner, albumId);
		if (album == null)
			throw new AlbumNotFound(owner, albumId);
		ImageMediaEntity imageMedia = imageMediaRepo.findFirstByAlbumOrderByNameAsc(album);
		return new FileInputStream(
				new File(DirectoryService.generateCoverPhotoFileSource(adminCoverSource, album, imageMedia)));
	}

	public InputStream fetchImageThumbnailStream(UserEntity owner, Long mediaId) throws FileNotFoundException {
		ImageMediaEntity imageMedia = imageMediaRepo.findByOwnerAndId(owner, mediaId);
		return new FileInputStream(new File(imageMedia.getThumbnailSource()));
	}

	public InputStream fetchImageStream(UserEntity owner, Long mediaId) throws FileNotFoundException {
		ImageMediaEntity imageMedia = imageMediaRepo.findByOwnerAndId(owner, mediaId);
		return new FileInputStream(new File(imageMedia.getSource()));
	}
}
