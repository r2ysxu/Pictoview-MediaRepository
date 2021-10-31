package org.mrn.service.builder;

import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.query.model.MediaItem;

public class VideoMediaItemBuilder implements ModelBuilder<MediaItem, VideoMediaEntity> {

	@Override
	public MediaItem build(VideoMediaEntity entity) {
		return new MediaItem()
				.setId(entity.getId())
				.setName(entity.getName());
	}
}
