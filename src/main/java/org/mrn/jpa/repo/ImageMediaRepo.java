package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.jpa.model.album.MediaAlbumEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImageMediaRepo extends PagingAndSortingRepository<ImageMediaEntity, Long> {
	ImageMediaEntity findByOwnerAndId(UserEntity owner, Long id);
	List<ImageMediaEntity> findAllByOwnerAndAlbum_Id(UserEntity owner, Long albumId, Pageable pageable);
	ImageMediaEntity findByOwnerAndAlbum(UserEntity owner, MediaAlbumEntity album);
	ImageMediaEntity findFirstByAlbumOrderByNameAsc(MediaAlbumEntity album);
}
