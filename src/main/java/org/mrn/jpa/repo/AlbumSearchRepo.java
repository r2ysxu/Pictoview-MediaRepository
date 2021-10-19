package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.user.UserEntity;
import org.springframework.data.domain.Pageable;

public interface AlbumSearchRepo {
	public List<AlbumEntity> searchAlbums(UserEntity user, SearchQuery searchQuery, Pageable pageable);
}
