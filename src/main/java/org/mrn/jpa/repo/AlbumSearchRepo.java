package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.album.Album;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.user.User;
import org.springframework.data.domain.Pageable;

public interface AlbumSearchRepo {
	public List<Album> searchAlbums(User user, SearchQuery searchQuery, Pageable pageable);
}
