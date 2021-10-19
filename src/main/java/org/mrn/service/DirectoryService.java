package org.mrn.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mrn.filemanager.AlbumFileManager;
import org.mrn.filemanager.ThumbnailDirectories;
import org.mrn.filemanager.ThumbnailDirectory;
import org.mrn.filemanager.ThumbnailDirectoryManager;
import org.mrn.filemanager.ThumbnailFile;
import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.jpa.model.album.MediaAlbumEntity;
import org.mrn.jpa.model.file.DirectoryAddedEntity;
import org.mrn.jpa.model.tags.AlbumTagEntity;
import org.mrn.jpa.model.tags.Categories;
import org.mrn.jpa.model.tags.CategoryEntity;
import org.mrn.jpa.model.tags.TagEntity;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.jpa.repo.AlbumTagRepo;
import org.mrn.jpa.repo.CategoryRepo;
import org.mrn.jpa.repo.DirectoryAddedRepo;
import org.mrn.jpa.repo.ImageMediaRepo;
import org.mrn.jpa.repo.MediaAlbumRepo;
import org.mrn.jpa.repo.TagRepo;
import org.mrn.query.model.ScannedDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DirectoryService {

	@Value("${app.admin.main.source}")
	private String adminSource;
	@Value("${app.admin.main.cover.source}")
	private String adminCoverSource;
	@Value("${app.admin.main.thumbnail.source}")
	private String adminThumbnailSource;

	@Autowired
	private DirectoryAddedRepo directoryRepo;
	@Autowired
	private MediaAlbumRepo mediaAlbumRepo;
	@Autowired
	private TagRepo tagRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private AlbumTagRepo albumTagRepo;
	@Autowired
	private ImageMediaRepo imageMediaRepo;

	public List<ScannedDirectory> getScannedFiles(String currentPath) {
		ThumbnailDirectoryManager manager = new ThumbnailDirectoryManager(adminSource);

		ThumbnailDirectories thumbnailDirectories = manager.listSubdirectories(currentPath);
		List<ThumbnailDirectory> subDirectories = thumbnailDirectories.getDirectories();
		List<String> subDirectoriesAbsolutePaths = thumbnailDirectories.getSubDirectoriesAbsolutePaths();

		List<DirectoryAddedEntity> scannedDirectories = directoryRepo.findAllByAbsolutePathIn(subDirectoriesAbsolutePaths);
		Set<String> scannedAbsolutePaths = new HashSet<>(scannedDirectories.size());
		for (DirectoryAddedEntity scannedDirectory : scannedDirectories) {
			scannedAbsolutePaths.add(scannedDirectory.getAbsolutePath());
		}

		List<ScannedDirectory> directories = new ArrayList<>(subDirectories.size());
		for (int i = 0; i < subDirectories.size(); i++) {
			directories.add(new ScannedDirectory(subDirectories.get(i),
					scannedAbsolutePaths.contains(subDirectoriesAbsolutePaths.get(i))));
		}

		return directories;
	}

	public ScannedDirectory addScannedDirectory(String currentPath, AlbumEntity album) {
		File directory = new File(adminSource + currentPath);
		directoryRepo.save(new DirectoryAddedEntity(directory.getAbsolutePath(), album));
		return new ScannedDirectory(new ThumbnailDirectory(directory.getAbsolutePath(), directory.getName()), true);
	}

	public List<ImageMediaEntity> addImages(EndUserEntity user, String currentPath, MediaAlbumEntity album) throws IOException {
		ThumbnailDirectoryManager manager = new ThumbnailDirectoryManager(adminSource);
		List<ThumbnailFile> imageFiles = manager.listImageFiles(currentPath);
		List<ImageMediaEntity> imageMedia = new ArrayList<>(imageFiles.size());
		for (ThumbnailFile file : imageFiles) {
			imageMedia.add(new ImageMediaEntity(user, file.getAbsoluteFile(), file.getName(), file.getType(), album));
		}
		List<ImageMediaEntity> photos = new ArrayList<>();
		imageMediaRepo.saveAll(imageMedia).forEach(photos::add);
		return photos;
	}

	public Iterable<ImageMediaEntity> createImageThumbnails(MediaAlbumEntity album, List<ImageMediaEntity> images) throws IOException {
		for (ImageMediaEntity image : images) {
			image.setThumbnailSource(
					adminThumbnailSource + album.getId() + "/" + image.getId() + image.getTypeExtension());
			AlbumFileManager.createPhotoThumbnail(image.getSource(), image.getThumbnailSource());
		}
		return imageMediaRepo.saveAll(images);
	}

	public static String generateCoverPhotoFileSource(String base, MediaAlbumEntity album, ImageMediaEntity imageMedia) {
		return base + album.getId() + "_" + imageMedia.getId() + imageMedia.getTypeExtension();
	}

	public MediaAlbumEntity setAlbumCoverPhoto(MediaAlbumEntity album) throws IOException {
		ImageMediaEntity imageMedia = imageMediaRepo.findFirstByAlbumOrderByNameAsc(album);
		AlbumFileManager.createCoverPhotoFile(imageMedia.getSource(),
				generateCoverPhotoFileSource(adminCoverSource, album, imageMedia));
		album.setCoverPhoto(imageMedia);
		return mediaAlbumRepo.save(album);
	}

	private TagEntity parsePublisherTag(String name) {
		TagEntity albumPublisher = null;
		CategoryEntity publisherCategory = categoryRepo.findByName(Categories.Publisher);
		if (name.startsWith("[") && name.indexOf(']') > 1) {
			String publisherName = name.substring(1, name.lastIndexOf(']'));
			albumPublisher = tagRepo.findFirstByName(publisherName);
			if (albumPublisher == null) {
				albumPublisher = tagRepo.save(new TagEntity(publisherCategory, publisherName));
			}
		}
		return albumPublisher;
	}

	public MediaAlbumEntity addImageDirectory(EndUserEntity user, String currentPath, String name) {
		if (!StringUtils.hasLength(name))
			return null;

		TagEntity albumPublisher = parsePublisherTag(name);
		name = name.replaceFirst("^\\[.*\\]", "").trim();

		File currentPathFile = new File(adminSource + currentPath);
		String parentPath = new File(currentPathFile.getParent()).getAbsolutePath();

		MediaAlbumEntity imageAlbum = new MediaAlbumEntity(user, name, "");
		imageAlbum.setSubtitle(albumPublisher.getName());
		imageAlbum = mediaAlbumRepo.save(imageAlbum);

		DirectoryAddedEntity parentAdded = directoryRepo.findFirstByAbsolutePath(parentPath);
		if (parentAdded != null) {
			imageAlbum.setParent(parentAdded.getAlbum());
		}

		tagRepo.save(albumPublisher.setName(albumPublisher.getName().toLowerCase()));
		AlbumTagEntity albumPublisherTag = albumTagRepo.save(new AlbumTagEntity(imageAlbum, albumPublisher));
		imageAlbum.addAlbumTag(albumPublisherTag);
		return mediaAlbumRepo.save(imageAlbum);
	}
}