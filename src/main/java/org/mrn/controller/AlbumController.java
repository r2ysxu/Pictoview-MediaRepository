package org.mrn.controller;

import java.io.IOException;

import org.mrn.exceptions.AlbumNotFound;
import org.mrn.exceptions.InvalidMediaAlbumException;
import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.filemanager.AlbumDirectory;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.user.EndUserEntity;
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
				album.getDescription());
	}

	@PostMapping(value = "/album/update/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Boolean uploadAlbumMedia(@RequestParam("file") MultipartFile file, @RequestParam("albumId") Long albumId,
			@RequestParam("fromMetadata") Boolean fromMetadata)
			throws UnauthenticatedUserException, IllegalStateException, IOException, AlbumNotFound {
		EndUserEntity user = getUser();
		AlbumDirectory albumDirectory = mediaAlbumService.uploadAlbumMedia(user, albumId, file, fromMetadata);
		mediaAlbumService.createMediaFromFile(user, albumId, albumDirectory);
		if (fromMetadata) {
			NewAlbumInfo newAlbum = AlbumInfoParserUtil.loadAlbumInfoFromJson(albumDirectory.getInfoJson());
			mediaAlbumService.updateAlbum(user, albumId, newAlbum.getName(), newAlbum.getSubtitle(), newAlbum.getDescription());
			tagService.tagAlbum(user, albumId, newAlbum.getCategories());
			mediaAlbumService.setCoverPhotoByName(user, albumId, newAlbum.getCoverPhotoName());
		} else {
			mediaAlbumService.setFirstAlbumCoverPhoto(user, albumId);
		}
		return true;
	}

	@ResponseBody
	@PostMapping(value = "/album/update/cover", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Album updateCoverId(@RequestBody CoverImage coverImage)
			throws AlbumNotFound, InvalidMediaAlbumException, UnauthenticatedUserException {
		return mediaAlbumService.setCoverPhotoById(getUser(), coverImage.getAlbumId(), coverImage.getImageId());
	}
}
