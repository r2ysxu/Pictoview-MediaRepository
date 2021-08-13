package org.pvrn.jpa.repo;

import java.util.List;

import org.pvrn.jpa.model.album.Album;
import org.pvrn.jpa.model.tags.SearchQuery;
import org.pvrn.jpa.model.user.User;
import org.springframework.data.domain.Pageable;

public interface AlbumSearchRepo {
	public List<Album> searchAlbums(User user, SearchQuery searchQuery, Pageable pageable);
}
