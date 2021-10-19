package org.mrn.service.builder;

import java.util.ArrayList;
import java.util.List;

import org.mrn.jpa.model.album.MediaAlbumEntity;
import org.mrn.query.model.Album;

public class AlbumBuilder implements ModelBuilder<Album, MediaAlbumEntity> {

	public static Album buildFrom(MediaAlbumEntity entity) {
		return new AlbumBuilder().build(entity);
	}

	public static List<Album> buildFrom(Iterable<MediaAlbumEntity> entities) {
		List<Album> albums = new ArrayList<>();
		entities.forEach(entity -> {
			albums.add(AlbumBuilder.buildFrom(entity));
		});
		return albums;
	}

	public static List<Album> buildFrom(List<MediaAlbumEntity> entities) {
		return entities.stream().map(entity -> AlbumBuilder.buildFrom(entity)).toList();
	}
	
	private AlbumBuilder() {}

	@Override
	public Album build(MediaAlbumEntity entity) {
		return new Album(entity.getId(), entity.getName(), entity.getDescription(), entity.getSubtitle(),
				entity.getCoverPhoto().getId());
	}
}
