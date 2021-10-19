package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.tags.AlbumTagEntity;
import org.springframework.data.repository.CrudRepository;

public interface AlbumTagRepo extends CrudRepository<AlbumTagEntity, Long> {
	public List<AlbumTagEntity> findAllByAlbum_Id(Long albumId);
}
