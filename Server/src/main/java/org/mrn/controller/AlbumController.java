package org.mrn.controller;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.mrn.exceptions.AlbumInfoNotFound;
import org.mrn.exceptions.AlbumNotFound;
import org.mrn.exceptions.InvalidMediaAlbumException;
import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.filemanager.AlbumDirectory;
import org.mrn.filemanager.AlbumFileUtils;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.jpa.model.user.UserEntity.Role;
import org.mrn.query.model.Album;
import org.mrn.query.model.AlbumTag;
import org.mrn.query.model.Category;
import org.mrn.query.model.CoverImage;
import org.mrn.query.model.FileMedia;
import org.mrn.query.model.NewAlbumInfo;
import org.mrn.query.model.PageItems;
import org.mrn.query.model.SortBy;
import org.mrn.service.AlbumService;
import org.mrn.service.TagService;
import org.mrn.utils.AlbumInfoParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

@RestController
public class AlbumController extends BaseController {

	private static final int PAGE_SIZE = 20;

	@Autowired
	private AlbumService mediaAlbumService;
	@Autowired
	private TagService tagService;

	@ResponseBody
	@GetMapping(value = "/album/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageItems<Album> searchAlbums(@RequestParam(name = "query") String searchString,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "sortField", required = false) String sortBy,
			@RequestParam(name = "ascending", required = false) Boolean asc) throws UnauthenticatedUserException {
		if (asc == null) asc = true;
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, SortBy.getSortField(sortBy, asc));
		return mediaAlbumService.searchMediaAlbum(user, SearchQuery.parse(searchString), pageable);
	}

	@ResponseBody
	@GetMapping(value = "/album/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public Album getAlbum(@RequestParam("albumId") Long albumId) throws AlbumNotFound, UnauthenticatedUserException {
		return mediaAlbumService.fetchMediaAlbum(getUser(), albumId);
	}

	@ResponseBody
	@GetMapping(value = "/album/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageItems<Album> listAlbums(@RequestParam(name = "parentId") Long parentId,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "sortField", required = false) String sortBy,
			@RequestParam(name = "ascending", required = false) Boolean asc) throws UnauthenticatedUserException {
		if (asc == null) asc = true;
		if (parentId == 0L) parentId = null;
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, SortBy.getSortField(sortBy, asc));
		PageItems<Album> albums = mediaAlbumService.listMediaAlbums(user, parentId, pageable);
		albums.getPageInfo().setSortedBy(SortBy.instance(sortBy, asc));
		return albums;
	}

	@ResponseBody
	@GetMapping(value = "/album/tag/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public AlbumTag listAlbumTags(@RequestParam(name = "albumId") Long albumId) {
		return tagService.listAlbumTags(albumId);
	}

	@ResponseBody
	@PostMapping(value = "/album/tag/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public AlbumTag tagAlbum(@RequestBody AlbumTag albumTag) throws UnauthenticatedUserException, AlbumNotFound {
		EndUserEntity user = getUser();
		Set<Long> categoryIds = albumTag.getCategories().stream().map(Category::getId).collect(Collectors.toSet());
		tagService.tagAlbum(user, albumTag.getAlbumId(), albumTag.getTags(), categoryIds);
		return tagService.listAlbumTags(albumTag.getAlbumId());
	}

	@ResponseBody
	@PostMapping(value = "/album/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Album createAlbum(@RequestBody Album album) throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		return mediaAlbumService.createAlbum(user, album.getName(), album.getAltname(), album.getPublisher(),
				album.getDescription());
	}

	@ResponseBody
	@PostMapping(value = "/album/update", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Album updateAlbum(@RequestBody Album album) throws UnauthenticatedUserException, AlbumNotFound {
		EndUserEntity user = getUser();
		return mediaAlbumService.updateAlbum(user, album.getId(), album.getName(), album.getAltname(), album.getPublisher(),
				album.getDescription(), album.getRating(), album.getMetaType());
	}

	@PostMapping(value = "/album/update/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Boolean uploadAlbum(@RequestParam("file") MultipartFile file, @RequestParam("albumId") Long albumId,
			@RequestParam("fromMetadata") Boolean fromMetadata) throws UnauthenticatedUserException, IllegalStateException,
			IOException, AlbumNotFound, UnsupportedTagException, InvalidDataException {
		EndUserEntity user = getUser();
		AlbumDirectory albumDirectory = mediaAlbumService.uploadAlbumMedia(user, albumId, file, fromMetadata);
		mediaAlbumService.createMediaFromFile(user, albumId, albumDirectory);
		if (fromMetadata) {
			NewAlbumInfo newAlbum = AlbumInfoParserUtil.loadAlbumInfoFromJson(albumDirectory.getInfoJson());
			mediaAlbumService.updateAlbum(user, albumId, newAlbum.getName(), newAlbum.getAltname(), newAlbum.getSubtitle(),
					newAlbum.getDescription(), newAlbum.getRating(), newAlbum.getMetaType());
			mediaAlbumService.setCoverPhotoByName(user, albumId, newAlbum.getCoverPhotoName());
			tagService.tagAlbum(user, albumId, newAlbum.getTags());
		} else {
			mediaAlbumService.updateFirstAlbumCoverPhoto(user, albumId);
		}
		return true;
	}

	@PostMapping(value = "/album/update/upload/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Boolean uploadAlbumMedium(@RequestParam("file") MultipartFile file, @RequestParam("albumId") Long albumId)
			throws UnauthenticatedUserException, UnsupportedTagException, InvalidDataException, IOException, AlbumNotFound {
		EndUserEntity user = getUser();
		mediaAlbumService.createMediumFromFile(user, albumId, file);
		return true;
	}

	/**
	 * This allows an admin to create an album by giving a filepath of the file on the server. Please remove this when deploying to public domains for security.
	 *
	 * @param path
	 *            file path of the album on the server
	 * @return
	 * @throws IOException
	 * @throws AlbumNotFound
	 * @throws UnsupportedTagException
	 * @throws InvalidDataException
	 * @throws UnauthenticatedUserException
	 * @throws AlbumInfoNotFound
	 */
	@PostMapping(value = "/album/update/filepath", consumes = "application/json;charset=UTF-8")
	public Album uploadAlbumFilepath(@RequestBody String path) throws IOException, AlbumNotFound, AlbumInfoNotFound,
			UnsupportedTagException, InvalidDataException, UnauthenticatedUserException {
		EndUserEntity user = getUser();
		if (user.getRole() != Role.ADMIN) return null;
		AlbumDirectory albumDirectory = AlbumFileUtils.generateAlbumFolder(path, true);
		return createAlbumFromFileInfo(user, albumDirectory, null);
	}

	@PostMapping(value = "/album/update/json", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean generateJsonFromData(@RequestBody String albumId) throws UnauthenticatedUserException, NumberFormatException, AlbumNotFound {
		EndUserEntity user = getUser();
		if (user.getRole() != Role.ADMIN) return false;
		return mediaAlbumService.generateJsonFromAlbum(user, Long.parseLong(albumId));
	}

	@PostMapping(value = "/album/update/media/filepath", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean uploadMediaFilePath(@RequestBody FileMedia media)
			throws UnauthenticatedUserException, UnsupportedTagException, InvalidDataException, AlbumNotFound, IOException {
		EndUserEntity user = getUser();
		if (user.getRole() != Role.ADMIN) return null;
		File mediaFile = new File(media.getPath());
		if (mediaFile.exists() && !mediaFile.isDirectory() && AlbumFileUtils.isValidMediaType(media.getPath())) {
			AlbumDirectory albumDirectory = new AlbumDirectory(media.getPath())
					.addMediaFile(AlbumFileUtils.generateAlbumMediaFile(mediaFile));
			return mediaAlbumService.createMediaFromFile(user, media.getAlbumId(), albumDirectory).size() == 1;
		}
		return false;
	}

	private Album createAlbumFromFileInfo(EndUserEntity user, AlbumDirectory albumDirectory, Long parentAlbumId)
			throws IOException, AlbumNotFound, AlbumInfoNotFound, UnsupportedTagException, InvalidDataException {
		NewAlbumInfo newAlbum = AlbumInfoParserUtil.loadAlbumInfoFromJson(albumDirectory.getInfoJson());
		if (newAlbum != null) {
			Album album = mediaAlbumService.createAlbum(user, newAlbum.getName(), newAlbum.getAltname(), newAlbum.getSubtitle(),
					newAlbum.getDescription(), parentAlbumId);
			mediaAlbumService.updateAlbum(user, album.getId(), newAlbum.getName(), newAlbum.getAltname(),
					newAlbum.getSubtitle(), newAlbum.getDescription(), newAlbum.getRating(), newAlbum.getMetaType());
			mediaAlbumService.createMediaFromFile(user, album.getId(), albumDirectory);
			mediaAlbumService.setCoverPhotoByName(user, album.getId(), newAlbum.getCoverPhotoName());
			tagService.tagAlbum(user, album.getId(), newAlbum.getTags());
			for (AlbumDirectory subAlbumDirectory : albumDirectory.getSubAlbums()) {
				try {
					createAlbumFromFileInfo(user, subAlbumDirectory, album.getId());
				} catch (AlbumInfoNotFound ex) {
				}
			}
			return album;
		} else {
			throw new AlbumInfoNotFound(albumDirectory.getAbsolutePath());
		}
	}

	@ResponseBody
	@PostMapping(value = "/album/update/cover", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Album updateCoverId(@RequestBody CoverImage coverImage)
			throws AlbumNotFound, InvalidMediaAlbumException, UnauthenticatedUserException, IOException {
		return mediaAlbumService.updateCoverPhotoById(getUser(), coverImage.getAlbumId(), coverImage.getImageId());
	}

	@DeleteMapping(value = "/album/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean deleteAlbum(@RequestBody String albumId)
			throws UnauthenticatedUserException, AlbumNotFound, NumberFormatException, IOException {
		mediaAlbumService.deleteAlbum(getUser(), Long.parseLong(albumId));
		return true;
	}
}
