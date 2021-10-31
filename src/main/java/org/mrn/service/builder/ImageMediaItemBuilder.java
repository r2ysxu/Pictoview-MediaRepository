package org.mrn.service.builder;

import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.query.model.MediaItem;

public class ImageMediaItemBuilder implements ModelBuilder<MediaItem, ImageMediaEntity> {

	@Override
	public MediaItem build(ImageMediaEntity entity) {
		return new MediaItem()
				.setId(entity.getId())
				.setName(entity.getName());
	}
}
