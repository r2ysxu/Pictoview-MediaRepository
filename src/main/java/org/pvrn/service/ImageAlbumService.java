package org.pvrn.service;

import java.util.List;
import java.util.stream.Collectors;

import org.pvrn.jpa.model.album.Album;
import org.pvrn.jpa.model.album.ImageAlbum;
import org.pvrn.jpa.model.album.ImageMedia;
import org.pvrn.jpa.model.tags.SearchQuery;
import org.pvrn.jpa.model.user.User;
import org.pvrn.jpa.repo.AlbumRepo;
import org.pvrn.jpa.repo.ImageAlbumRepo;
import org.pvrn.jpa.repo.ImageMediaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ImageAlbumService {

	@Autowired
	private AlbumRepo albumRepo;
	@Autowired
	private ImageAlbumRepo imageAlbumRepo;
	@Autowired
	private ImageMediaRepo imageMediaRepo;

	public Iterable<ImageAlbum> searchImageAlbum(User user, SearchQuery searchQuery, Pageable pageable) {
		List<Album> albums = albumRepo.searchAlbums(user, searchQuery, pageable);
		List<Long> ids = albums.stream().map(Album::getId).collect(Collectors.toList());

		return imageAlbumRepo.findAllById(ids);
	}

	public Iterable<ImageAlbum> listImageAlbums(User user, Long parentId, Pageable pageable) {
		if (parentId == null || parentId < 1)
			return imageAlbumRepo.findAllByOwner(user, pageable);
		else
			return imageAlbumRepo.findAllByOwnerAndParent_Id(user, parentId, pageable);
	}

	public List<Long> listImageMedia(User user, Long albumId, Pageable pageable) {
		List<ImageMedia> imageMedia = imageMediaRepo.findAllByOwnerAndAlbum_Id(user, albumId, pageable);
		return imageMedia.stream().map(imageMedium -> imageMedium.getId()).collect(Collectors.toList());
	}
}
