package org.mrn.service;

import java.util.List;
import java.util.stream.Collectors;

import org.mrn.jpa.model.album.Album;
import org.mrn.jpa.model.album.ImageMedia;
import org.mrn.jpa.model.album.MediaAlbum;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.user.User;
import org.mrn.jpa.repo.AlbumRepo;
import org.mrn.jpa.repo.ImageMediaRepo;
import org.mrn.jpa.repo.MediaAlbumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MediaAlbumService {

	@Autowired
	private AlbumRepo albumRepo;
	@Autowired
	private MediaAlbumRepo mediaAlbumRepo;
	@Autowired
	private ImageMediaRepo imageMediaRepo;

	public Iterable<MediaAlbum> searchMediaAlbum(User user, SearchQuery searchQuery, Pageable pageable) {
		List<Album> albums = albumRepo.searchAlbums(user, searchQuery, pageable);
		List<Long> ids = albums.stream().map(Album::getId).collect(Collectors.toList());

		return mediaAlbumRepo.findAllById(ids);
	}

	public Iterable<MediaAlbum> listMediaAlbums(User user, Long parentId, Pageable pageable) {
		if (parentId == null || parentId < 1)
			return mediaAlbumRepo.findAllByOwner(user, pageable);
		else
			return mediaAlbumRepo.findAllByOwnerAndParent_Id(user, parentId, pageable);
	}

	public List<Long> listImageMedia(User user, Long albumId, Pageable pageable) {
		List<ImageMedia> imageMedia = imageMediaRepo.findAllByOwnerAndAlbum_Id(user, albumId, pageable);
		return imageMedia.stream().map(imageMedium -> imageMedium.getId()).collect(Collectors.toList());
	}
}
