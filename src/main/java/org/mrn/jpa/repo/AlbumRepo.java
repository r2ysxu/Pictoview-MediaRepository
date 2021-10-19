package org.mrn.jpa.repo;

import org.mrn.jpa.model.album.AlbumEntity;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepo extends CrudRepository<AlbumEntity, Long>, AlbumSearchRepo {

}
