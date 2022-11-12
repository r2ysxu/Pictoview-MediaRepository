package org.mrn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.mrn.exceptions.AlbumNotFound;
import org.mrn.exceptions.ImageNotFound;
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

	public ImageInputStream fetchCoverPhotoStream(UserEntity owner, Long albumId) throws AlbumNotFound, IOException, ImageNotFound {
		AlbumEntity album = imageAlbumRepo.findByOwnerAndId(owner, albumId);
		if (album == null) throw new AlbumNotFound(owner, albumId);
		if (album.getCoverPhoto() == null) return null;
		ImageMediaEntity imageMedia = imageMediaRepo.findByOwnerAndAlbum_IdAndId(owner, albumId, album.getCoverPhoto().getId());
		if (imageMedia == null) return null;
		File file = new File(generateCoverPhotoPath(adminCoverSource, album, imageMedia));
		if (!file.exists()) throw new ImageNotFound(imageMedia.getId());
		return ImageIO.createImageInputStream(new FileInputStream(file));
	}

	public ImageInputStream fetchImageThumbnailStream(UserEntity owner, Long mediaId) throws IOException, ImageNotFound {
		ImageMediaEntity imageMedia = imageMediaRepo.findByOwnerAndId(owner, mediaId);
		File file = new File(imageMedia.getThumbnailSource());
		if (!file.exists()) throw new ImageNotFound(mediaId);
		return ImageIO.createImageInputStream(new FileInputStream(file));
	}

	public ImageInputStream fetchImageStream(UserEntity owner, Long mediaId) throws ImageNotFound, IOException {
		ImageMediaEntity imageMedia = imageMediaRepo.findByOwnerAndId(owner, mediaId);
		File file = new File(imageMedia.getSource());
		if (!file.exists()) throw new ImageNotFound(mediaId);
		return ImageIO.createImageInputStream(new FileInputStream(file));
	}
}
