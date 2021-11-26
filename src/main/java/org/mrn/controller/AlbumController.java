package org.mrn.controller;

import java.io.IOException;

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
import org.mrn.query.model.CoverImage;
import org.mrn.query.model.NewAlbumInfo;
import org.mrn.query.model.PageItems;
import org.mrn.service.AlbumService;
import org.mrn.service.TagService;
import org.mrn.utils.AlbumInfoParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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

	private static final int PAGE_SIZE = 10;

	@Autowired
	private AlbumService mediaAlbumService;
	@Autowired
	private TagService tagService;

	@ResponseBody
	@GetMapping(value = "/album/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageItems<Album> searchAlbums(@RequestParam(name = "query") String searchString,
			@RequestParam(name = "page") Integer page) throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.unsorted());
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
			@RequestParam(name = "page") Integer page) throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.unsorted());
		return mediaAlbumService.listMediaAlbums(user, parentId, pageable);
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
		tagService.tagAlbum(user, albumTag.getAlbumId(), albumTag.getCategories());
		return tagService.listAlbumTags(albumTag.getAlbumId());
	}

	@ResponseBody
	@PostMapping(value = "/album/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Album createAlbum(@RequestBody Album album) throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		return mediaAlbumService.createAlbum(user, album.getName(), album.getPublisher(), album.getDescription());
	}

	@ResponseBody
	@PostMapping(value = "/album/update", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Album updateAlbum(@RequestBody Album album) throws UnauthenticatedUserException, AlbumNotFound {
		EndUserEntity user = getUser();
		return mediaAlbumService.updateAlbum(user, album.getId(), album.getName(), album.getPublisher(),
				album.getDescription(), album.getRating(), album.getMetaType());
	}

	@PostMapping(value = "/album/update/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Boolean uploadAlbumMedia(@RequestParam("file") MultipartFile file, @RequestParam("albumId") Long albumId,
			@RequestParam("fromMetadata") Boolean fromMetadata)
			throws UnauthenticatedUserException, IllegalStateException, IOException, AlbumNotFound, UnsupportedTagException, InvalidDataException {
		EndUserEntity user = getUser();
		AlbumDirectory albumDirectory = mediaAlbumService.uploadAlbumMedia(user, albumId, file, fromMetadata);
		mediaAlbumService.createMediaFromFile(user, albumId, albumDirectory);
		if (fromMetadata) {
			NewAlbumInfo newAlbum = AlbumInfoParserUtil.loadAlbumInfoFromJson(albumDirectory.getInfoJson());
			mediaAlbumService.updateAlbum(user, albumId, newAlbum.getName(),
					newAlbum.getSubtitle(), newAlbum.getDescription(),
					newAlbum.getRating(), newAlbum.getMetaType());
			tagService.tagAlbum(user, albumId, newAlbum.getCategories());
			mediaAlbumService.setCoverPhotoByName(user, albumId, newAlbum.getCoverPhotoName());
		} else {
			mediaAlbumService.updateFirstAlbumCoverPhoto(user, albumId);
		}
		return true;
	}

	/**
	 * This allows an admin to dangerously create an album by giving a filepath of the file on the server.
	 * Please remove this when deploying to public domains for security.
	 *
	 * @param path file path of the album on the server
	 * @return
	 * @throws IOException
	 * @throws AlbumNotFound
	 * @throws UnsupportedTagException
	 * @throws InvalidDataException
	 * @throws UnauthenticatedUserException
	 */
	@PostMapping(value = "/album/update/filepath")
	public Album uploadAlbumMedia(@RequestBody String path)
			throws IOException, AlbumNotFound, UnsupportedTagException, InvalidDataException, UnauthenticatedUserException {
		EndUserEntity user = getUser();
		if (user.getRole() != Role.ADMIN) return null;
		AlbumDirectory albumDirectory = AlbumFileUtils.generateAlbumFolder(path, true);
		NewAlbumInfo newAlbum = AlbumInfoParserUtil.loadAlbumInfoFromJson(albumDirectory.getInfoJson());
		Album album = mediaAlbumService.createAlbum(user, newAlbum.getName(), newAlbum.getSubtitle(), newAlbum.getDescription());
		mediaAlbumService.updateAlbum(user, album.getId(), newAlbum.getName(), newAlbum.getSubtitle(), newAlbum.getDescription(),
				newAlbum.getRating(), newAlbum.getMetaType());
		mediaAlbumService.createMediaFromFile(user, album.getId(), albumDirectory);
		tagService.tagAlbum(user, album.getId(), newAlbum.getCategories());
		mediaAlbumService.setCoverPhotoByName(user, album.getId(), newAlbum.getCoverPhotoName());
		return album;
	}

	@ResponseBody
	@PostMapping(value = "/album/update/cover", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Album updateCoverId(@RequestBody CoverImage coverImage)
			throws AlbumNotFound, InvalidMediaAlbumException, UnauthenticatedUserException, IOException {
		return mediaAlbumService.updateCoverPhotoById(getUser(), coverImage.getAlbumId(), coverImage.getImageId());
	}
}
