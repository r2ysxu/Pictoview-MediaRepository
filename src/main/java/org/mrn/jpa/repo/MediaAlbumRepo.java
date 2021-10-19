package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.album.MediaAlbumEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MediaAlbumRepo extends PagingAndSortingRepository<MediaAlbumEntity, Long> {
	public MediaAlbumEntity findByOwnerAndId(UserEntity user, Long id);
	public List<MediaAlbumEntity> findAllByOwner(UserEntity user, Pageable pageable);
	public List<MediaAlbumEntity> findAllByOwnerAndParent_Id(UserEntity user, Long parentId, Pageable pageable);
}
