package org.pvrn.jpa.repo;

import java.util.List;

import org.pvrn.jpa.model.album.ImageAlbum;
import org.pvrn.jpa.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImageAlbumRepo extends PagingAndSortingRepository<ImageAlbum, Long> {
	public ImageAlbum findByOwnerAndId(User user, Long id);
	public List<ImageAlbum> findAllByOwner(User user, Pageable pageable);
	public List<ImageAlbum> findAllByOwnerAndParent_Id(User user, Long parentId, Pageable pageable);
}
