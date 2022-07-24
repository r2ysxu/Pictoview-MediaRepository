package org.mrn.service.builder;

import org.mrn.jpa.model.album.music.AudioMediaEntity;
import org.mrn.query.model.AudioMediaItem;

public class AudioMediaItemBuilder implements ModelBuilder<AudioMediaItem, AudioMediaEntity> {

	@Override
	public AudioMediaItem build(AudioMediaEntity entity) {
		AudioMediaItem musicMediaItem = new AudioMediaItem()
				.setTitle(entity.getTitle())
				.setArtist(entity.getArtist().getName())
				.setGenre(entity.getGenre().getName())
				.setTrackNumber(entity.getTrackNumber());
		musicMediaItem.setId(entity.getId()).setName(entity.getName());
		return musicMediaItem;
	}
}
