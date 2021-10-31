package org.mrn.jpa.repo;

import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlbumSearchRepo {
	public Page<AlbumEntity> searchAlbums(UserEntity user, SearchQuery searchQuery, Pageable pageable);
}
