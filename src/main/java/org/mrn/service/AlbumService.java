package org.mrn.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mrn.exceptions.AlbumNotFound;
import org.mrn.filemanager.AlbumDirectory;
import org.mrn.filemanager.AlbumFileUtils;
import org.mrn.filemanager.AlbumMediaFile;
import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.jpa.model.album.MediaEntity;
import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.mrn.jpa.repo.AlbumRepo;
import org.mrn.jpa.repo.ImageMediaRepo;
import org.mrn.jpa.repo.VideoMediaRepo;
import org.mrn.query.model.Album;
import org.mrn.query.model.MediaItem;
import org.mrn.query.model.PageItems;
import org.mrn.service.builder.AlbumBuilder;
import org.mrn.service.builder.ImageMediaItemBuilder;
import org.mrn.service.builder.PageItemBuilder;
import org.mrn.service.builder.VideoMediaItemBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlbumService {

	@Value("${app.admin.main.source}")
	private String adminSource;
	@Value("${app.admin.main.thumbnail.source}")
	private String adminThumbnailSource;
	@Value("${app.admin.main.cover.source}")
	private String adminCoverSource;
	
	private static final String ALBUMS_DIRECTORY = "albums/";
	private static final String ZIPPED_DIRECTORY = "zips/";

	@Autowired
	private AlbumRepo mediaAlbumRepo;
	@Autowired
	private ImageMediaRepo imageMediaRepo;
	@Autowired
	private VideoMediaRepo videoMediaRepo;

	public PageItems<Album> searchMediaAlbum(UserEntity user, SearchQuery searchQuery, Pageable pageable) {
		Page<AlbumEntity> albums = mediaAlbumRepo.searchAlbums(user, searchQuery, pageable);
		return new PageItemBuilder<Album, AlbumEntity>().build(albums, new AlbumBuilder());
	}

	public PageItems<Album> listMediaAlbums(UserEntity user, Long parentId, Pageable pageable) {
		Page<AlbumEntity> albums;
		if (parentId == null || parentId < 1) albums = mediaAlbumRepo.findAllByOwner(user, pageable);
		else albums = mediaAlbumRepo.findAllByOwnerAndParent_Id(user, parentId, pageable);
		return new PageItemBuilder<Album, AlbumEntity>().build(albums, new AlbumBuilder());
	}

	public PageItems<MediaItem> listImageMedia(UserEntity user, Long albumId, Pageable pageable) {
		Page<ImageMediaEntity> imageMedia = imageMediaRepo.findAllByOwnerAndAlbum_Id(user, albumId, pageable);
		return new PageItemBuilder<MediaItem, ImageMediaEntity>().build(imageMedia, new ImageMediaItemBuilder());
	}

	public PageItems<MediaItem> listVideoMedia(UserEntity user, Long albumId, Pageable pageable) {
		Page<VideoMediaEntity> videoMedia = videoMediaRepo.findAllByOwnerAndAlbum_Id(user, albumId, pageable);
		return new PageItemBuilder<MediaItem, VideoMediaEntity>().build(videoMedia, new VideoMediaItemBuilder());
	}

	public AlbumDirectory uploadAlbumMedia(EndUserEntity user, Long albumId, MultipartFile file, Boolean loadMetadata)
			throws IllegalStateException, IOException {
		final String albumFolderPath = adminSource + ALBUMS_DIRECTORY + file.getOriginalFilename().replace(".zip", "");
		File zippedFile = new File(adminSource + ZIPPED_DIRECTORY + file.getOriginalFilename());
		file.transferTo(zippedFile);
		AlbumFileUtils.unzipFolder(zippedFile.getAbsolutePath(), albumFolderPath);
		return AlbumFileUtils.generateAlbumFolder(albumFolderPath, loadMetadata);
	}

	public Album createAlbum(EndUserEntity user, String name, String subtitle, String description) {
		AlbumEntity albumEntity = mediaAlbumRepo.save(new AlbumEntity(user, name, subtitle, description));
		return new AlbumBuilder().build(albumEntity);
	}

	public Album updateAlbum(EndUserEntity user, Long albumId, String name, String subtitle, String description)
			throws AlbumNotFound {
		AlbumEntity albumEntity = mediaAlbumRepo.findById(albumId).get();
		if (albumEntity != null) {
			if (name != null) albumEntity.setName(name);
			if (subtitle != null) albumEntity.setSubtitle(subtitle);
			if (description != null) albumEntity.setDescription(description);
			albumEntity = mediaAlbumRepo.save(albumEntity);
			return new AlbumBuilder().build(albumEntity);
		} else {
			throw new AlbumNotFound(user, albumId);
		}
	}

	private String generateThumbnailPath(AlbumEntity album, ImageMediaEntity image) {
		return adminThumbnailSource + album.getId() + "/" + image.getId() + image.getTypeExtension();
	}

	public List<MediaEntity> createMediaFromFile(EndUserEntity user, Long albumId, AlbumDirectory albumDirectory)
			throws AlbumNotFound, IOException {
		AlbumEntity albumEntity = mediaAlbumRepo.findById(albumId).orElseThrow( () -> new AlbumNotFound(user, albumId));
		List<ImageMediaEntity> imageMedia = new ArrayList<>();
		List<VideoMediaEntity> videoMedia = new ArrayList<>();
		for (AlbumMediaFile file : albumDirectory.getMediaFiles()) {
			switch(file.getType()) {
			case IMAGE:
				ImageMediaEntity image = new ImageMediaEntity(user, file.getAbsolutePath(), file.getName(), file.getMediaType(), albumEntity);
				imageMedia.add(image);
				break;
			case VIDEO:
				videoMedia.add(new VideoMediaEntity(user, file.getAbsolutePath(), file.getName(), file.getMediaType(), albumEntity));
				break;
			default:
			}
		}
		List<MediaEntity> savedMedia = new ArrayList<>(albumDirectory.getMediaFiles().size());
		imageMediaRepo.saveAll(imageMedia).forEach(savedMedia::add);
		videoMediaRepo.saveAll(videoMedia).forEach(savedMedia::add);

		// Create Thumbnails
		for (ImageMediaEntity image : imageMedia) {
			image.setThumbnailSource(generateThumbnailPath(albumEntity, image));
			AlbumFileUtils.createPhotoThumbnail(image.getSource(), image.getThumbnailSource());
		}
		return savedMedia;
	}

	public AlbumEntity setCoverPhotoByName(EndUserEntity user, Long albumId, String name) throws IOException, AlbumNotFound {
		AlbumEntity albumEntity = mediaAlbumRepo.findById(albumId).orElseThrow( () -> new AlbumNotFound(user, albumId));
		ImageMediaEntity imageMedia = imageMediaRepo.findFirstByAlbumAndName(albumEntity, name);
		if (imageMedia == null) return null;
		AlbumFileUtils.createCoverPhotoFile(imageMedia.getSource(),
				ImageService.generateCoverPhotoPath(adminCoverSource, albumEntity, imageMedia));
		albumEntity.setCoverPhoto(imageMedia);
		return mediaAlbumRepo.save(albumEntity);
	}

	public AlbumEntity setFirstAlbumCoverPhoto(EndUserEntity user, Long albumId) throws IOException, AlbumNotFound {
		AlbumEntity albumEntity = mediaAlbumRepo.findById(albumId).orElseThrow( () -> new AlbumNotFound(user, albumId));
		ImageMediaEntity imageMedia = imageMediaRepo.findFirstByAlbumOrderByNameAsc(albumEntity);
		if (imageMedia == null) return null;
		AlbumFileUtils.createCoverPhotoFile(imageMedia.getSource(),
				ImageService.generateCoverPhotoPath(adminCoverSource, albumEntity, imageMedia));
		albumEntity.setCoverPhoto(imageMedia);
		return mediaAlbumRepo.save(albumEntity);
	}
}
