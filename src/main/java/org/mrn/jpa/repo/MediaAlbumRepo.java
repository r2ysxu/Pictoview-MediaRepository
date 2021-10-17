package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.album.MediaAlbum;
import org.mrn.jpa.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MediaAlbumRepo extends PagingAndSortingRepository<MediaAlbum, Long> {
	public MediaAlbum findByOwnerAndId(User user, Long id);
	public List<MediaAlbum> findAllByOwner(User user, Pageable pageable);
	public List<MediaAlbum> findAllByOwnerAndParent_Id(User user, Long parentId, Pageable pageable);
}
