package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.album.MediaAlbumEntity;
import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VideoMediaRepo extends PagingAndSortingRepository<VideoMediaEntity, Long> {
	VideoMediaEntity findByOwnerAndId(UserEntity owner, Long id);
	List<VideoMediaEntity> findAllByOwnerAndAlbum_Id(UserEntity owner, Long albumId, Pageable pageable);
	VideoMediaEntity findByOwnerAndAlbum(UserEntity owner, MediaAlbumEntity album);
	VideoMediaEntity findFirstByAlbumOrderByNameAsc(MediaAlbumEntity album);
}
