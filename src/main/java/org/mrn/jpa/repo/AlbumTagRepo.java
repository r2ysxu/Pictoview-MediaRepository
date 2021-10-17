package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.tags.AlbumTag;
import org.springframework.data.repository.CrudRepository;

public interface AlbumTagRepo extends CrudRepository<AlbumTag, Long> {
	public List<AlbumTag> findAllByAlbum_Id(Long albumId);
}
