package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.album.music.MusicGenreEntity;
import org.springframework.data.repository.CrudRepository;

public interface MusicGenreRepo extends CrudRepository<MusicGenreEntity, Long> {
	List<MusicGenreEntity> findByNameIn(List<String> name);
	MusicGenreEntity findByName(String name);
}
