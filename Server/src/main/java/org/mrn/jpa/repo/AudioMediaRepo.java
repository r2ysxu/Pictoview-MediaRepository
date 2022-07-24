package org.mrn.jpa.repo;

import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.music.AudioMediaEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AudioMediaRepo extends PagingAndSortingRepository<AudioMediaEntity, Long> {
	AudioMediaEntity findByOwnerAndId(UserEntity owner, Long id);
	Page<AudioMediaEntity> findAllByOwnerAndAlbum_Id(UserEntity owner, Long albumId, Pageable pageable);
	AudioMediaEntity findByOwnerAndAlbum(UserEntity owner, AlbumEntity album);
}