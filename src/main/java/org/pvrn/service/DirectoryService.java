package org.pvrn.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pvrn.filemanager.AlbumFileManager;
import org.pvrn.filemanager.ThumbnailDirectories;
import org.pvrn.filemanager.ThumbnailDirectory;
import org.pvrn.filemanager.ThumbnailDirectoryManager;
import org.pvrn.filemanager.ThumbnailFile;
import org.pvrn.jpa.model.album.Album;
import org.pvrn.jpa.model.album.ImageAlbum;
import org.pvrn.jpa.model.album.ImageMedia;
import org.pvrn.jpa.model.file.DirectoryAdded;
import org.pvrn.jpa.model.tags.AlbumTag;
import org.pvrn.jpa.model.tags.Categories;
import org.pvrn.jpa.model.tags.Category;
import org.pvrn.jpa.model.tags.Tag;
import org.pvrn.jpa.model.user.EndUser;
import org.pvrn.jpa.repo.AlbumTagRepo;
import org.pvrn.jpa.repo.CategoryRepo;
import org.pvrn.jpa.repo.DirectoryAddedRepo;
import org.pvrn.jpa.repo.ImageAlbumRepo;
import org.pvrn.jpa.repo.ImageMediaRepo;
import org.pvrn.jpa.repo.TagRepo;
import org.pvrn.query.model.ScannedDirectory;
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
	private ImageAlbumRepo imageAlbumRepo;
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

		List<DirectoryAdded> scannedDirectories = directoryRepo.findAllByAbsolutePathIn(subDirectoriesAbsolutePaths);
		Set<String> scannedAbsolutePaths = new HashSet<>(scannedDirectories.size());
		for (DirectoryAdded scannedDirectory : scannedDirectories) {
			scannedAbsolutePaths.add(scannedDirectory.getAbsolutePath());
		}

		List<ScannedDirectory> directories = new ArrayList<>(subDirectories.size());
		for (int i = 0; i < subDirectories.size(); i++) {
			directories.add(new ScannedDirectory(subDirectories.get(i),
					scannedAbsolutePaths.contains(subDirectoriesAbsolutePaths.get(i))));
		}

		return directories;
	}

	public ScannedDirectory addScannedDirectory(String currentPath, Album album) {
		File directory = new File(adminSource + currentPath);
		directoryRepo.save(new DirectoryAdded(directory.getAbsolutePath(), album));
		return new ScannedDirectory(new ThumbnailDirectory(directory.getAbsolutePath(), directory.getName()), true);
	}

	public List<ImageMedia> addImages(EndUser user, String currentPath, ImageAlbum album) throws IOException {
		ThumbnailDirectoryManager manager = new ThumbnailDirectoryManager(adminSource);
		List<ThumbnailFile> imageFiles = manager.listImageFiles(currentPath);
		List<ImageMedia> imageMedia = new ArrayList<>(imageFiles.size());
		for (ThumbnailFile file : imageFiles) {
			imageMedia.add(new ImageMedia(user, file.getAbsoluteFile(), file.getName(), file.getType(), album));
		}
		List<ImageMedia> photos = new ArrayList<>();
		imageMediaRepo.saveAll(imageMedia).forEach(photos::add);
		return photos;
	}

	public Iterable<ImageMedia> createImageThumbnails(ImageAlbum album, List<ImageMedia> images) throws IOException {
		for (ImageMedia image : images) {
			image.setThumbnailSource(
					adminThumbnailSource + album.getId() + "/" + image.getId() + image.getTypeExtension());
			AlbumFileManager.createPhotoThumbnail(image.getSource(), image.getThumbnailSource());
		}
		return imageMediaRepo.saveAll(images);
	}

	public static String generateCoverPhotoFileSource(String base, ImageAlbum album, ImageMedia imageMedia) {
		return base + album.getId() + "_" + imageMedia.getId() + imageMedia.getTypeExtension();
	}

	public ImageAlbum setAlbumCoverPhoto(ImageAlbum album) throws IOException {
		ImageMedia imageMedia = imageMediaRepo.findFirstByAlbumOrderByNameAsc(album);
		AlbumFileManager.createCoverPhotoFile(imageMedia.getSource(),
				generateCoverPhotoFileSource(adminCoverSource, album, imageMedia));
		album.setCoverPhoto(imageMedia);
		return imageAlbumRepo.save(album);
	}

	private Tag parsePublisherTag(String name) {
		Tag albumPublisher = null;
		Category publisherCategory = categoryRepo.findByName(Categories.Publisher);
		if (name.startsWith("[") && name.indexOf(']') > 1) {
			String publisherName = name.substring(1, name.lastIndexOf(']')).toLowerCase();
			albumPublisher = tagRepo.findFirstByName(publisherName);
			if (albumPublisher == null) {
				albumPublisher = tagRepo.save(new Tag(publisherCategory, publisherName));
			}
		}
		return albumPublisher;
	}

	public ImageAlbum addImageDirectory(EndUser user, String currentPath, String name) {
		if (!StringUtils.hasLength(name))
			return null;

		Tag albumPublisher = parsePublisherTag(name);
		name = name.replaceFirst("^\\[.*\\]", "").trim();

		File currentPathFile = new File(adminSource + currentPath);
		String parentPath = new File(currentPathFile.getParent()).getAbsolutePath();

		ImageAlbum imageAlbum = new ImageAlbum(user, name, "");
		imageAlbum.setSubtitle(albumPublisher.getName());
		imageAlbum = imageAlbumRepo.save(imageAlbum);

		DirectoryAdded parentAdded = directoryRepo.findFirstByAbsolutePath(parentPath);
		if (parentAdded != null) {
			imageAlbum.setParent(parentAdded.getAlbum());
		}

		AlbumTag albumPublisherTag = albumTagRepo.save(new AlbumTag(imageAlbum, albumPublisher));
		imageAlbum.addAlbumTag(albumPublisherTag);
		return imageAlbumRepo.save(imageAlbum);
	}
}
