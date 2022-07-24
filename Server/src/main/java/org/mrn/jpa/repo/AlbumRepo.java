package org.mrn.jpa.repo;

import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AlbumRepo extends PagingAndSortingRepository<AlbumEntity, Long>, AlbumSearchRepo {
	public AlbumEntity findByOwnerAndId(UserEntity user, Long id);
	public Page<AlbumEntity> findAllByOwner(UserEntity user, Pageable pageable);
	public Page<AlbumEntity> findAllByOwnerAndParent_Id(UserEntity user, Long parentId, Pageable pageable);
}
