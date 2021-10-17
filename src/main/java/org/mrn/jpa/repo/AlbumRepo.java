package org.mrn.jpa.repo;

import org.mrn.jpa.model.album.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepo extends CrudRepository<Album, Long>, AlbumSearchRepo {

}
