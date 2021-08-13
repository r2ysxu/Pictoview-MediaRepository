package org.pvrn.jpa.repo;

import org.pvrn.jpa.model.album.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepo extends CrudRepository<Album, Long>, AlbumSearchRepo {

}
