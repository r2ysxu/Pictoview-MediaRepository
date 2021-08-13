package org.pvrn.jpa.repo;

import java.util.List;

import org.pvrn.jpa.model.album.ImageAlbum;
import org.pvrn.jpa.model.album.ImageMedia;
import org.pvrn.jpa.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImageMediaRepo extends PagingAndSortingRepository<ImageMedia, Long> {
	ImageMedia findByOwnerAndId(User owner, Long id);
	List<ImageMedia> findAllByOwnerAndAlbum_Id(User owner, Long albumId, Pageable pageable);
	ImageMedia findByOwnerAndAlbum(User owner, ImageAlbum album);
	ImageMedia findFirstByAlbumOrderByNameAsc(ImageAlbum album);
}
