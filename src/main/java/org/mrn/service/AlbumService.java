package org.mrn.service;

import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.jpa.model.tags.SearchQuery;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {

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
}
