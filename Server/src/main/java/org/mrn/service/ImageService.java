package org.mrn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.mrn.exceptions.AlbumNotFound;
import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.mrn.jpa.repo.AlbumRepo;
import org.mrn.jpa.repo.ImageMediaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

	@Value("${app.admin.main.cover.source}")
	private String adminCoverSource;

	@Autowired
	private AlbumRepo imageAlbumRepo;
	@Autowired
	private ImageMediaRepo imageMediaRepo;

	public static String generateCoverPhotoPath(String base, AlbumEntity album, ImageMediaEntity imageMedia) {
		return base + album.getId() + "_" + imageMedia.getId() + imageMedia.getTypeExtension();
	}

	public InputStream fetchCoverPhotoStream(UserEntity owner, Long albumId) throws FileNotFoundException, AlbumNotFound {
		AlbumEntity album = imageAlbumRepo.findByOwnerAndId(owner, albumId);
		if (album == null) throw new AlbumNotFound(owner, albumId);
		if (album.getCoverPhoto() == null) return null;
		ImageMediaEntity imageMedia = imageMediaRepo.findByOwnerAndId(owner, album.getCoverPhoto().getId());
		if (imageMedia == null) return null;
		return new FileInputStream(
				new File(generateCoverPhotoPath(adminCoverSource, album, imageMedia)));
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
