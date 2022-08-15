package org.mrn.service.builder;

import java.util.ArrayList;
import java.util.List;

import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.query.model.Album;

public class AlbumBuilder implements ModelBuilder<Album, AlbumEntity> {

	public static Album buildFrom(AlbumEntity entity) {
		return new AlbumBuilder().build(entity);
	}

	public static List<Album> buildFrom(Iterable<AlbumEntity> entities) {
		List<Album> albums = new ArrayList<>();
		entities.forEach(entity -> {
			albums.add(AlbumBuilder.buildFrom(entity));
		});
		return albums;
	}

	public static List<Album> buildFrom(List<AlbumEntity> entities) {
		return entities.stream().map(entity -> AlbumBuilder.buildFrom(entity)).toList();
	}

	@Override
	public Album build(AlbumEntity entity) {
		ImageMediaEntity coverPhoto = entity.getCoverPhoto();
		return new Album(entity.getId(), entity.getName(), entity.getAltname(), entity.getDescription(), entity.getSubtitle(),
				coverPhoto == null ? 0 : coverPhoto.getId(), entity.getRating(), entity.getMetaType());
	}
}
