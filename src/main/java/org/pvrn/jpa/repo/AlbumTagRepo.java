package org.pvrn.jpa.repo;

import org.pvrn.jpa.model.tags.AlbumTag;
import org.springframework.data.repository.CrudRepository;

public interface AlbumTagRepo extends CrudRepository<AlbumTag, Long> {
	
}
