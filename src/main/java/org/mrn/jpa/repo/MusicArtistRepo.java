package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.album.music.MusicArtistEntity;
import org.springframework.data.repository.CrudRepository;

public interface MusicArtistRepo extends CrudRepository<MusicArtistEntity, Long> {
	List<MusicArtistEntity> findByNameIn(List<String> name);
	MusicArtistEntity findByName(String name);
}
