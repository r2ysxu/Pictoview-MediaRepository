package org.mrn.jpa.repo;

import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VideoMediaRepo extends PagingAndSortingRepository<VideoMediaEntity, Long> {
	VideoMediaEntity findByOwnerAndId(UserEntity owner, Long id);
	Page<VideoMediaEntity> findAllByOwnerAndAlbum_Id(UserEntity owner, Long albumId, Pageable pageable);
	VideoMediaEntity findByOwnerAndAlbum(UserEntity owner, AlbumEntity album);
	VideoMediaEntity findFirstByAlbumOrderByNameAsc(AlbumEntity album);
}
