package org.mrn.service.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mrn.jpa.model.tags.AlbumTagEntity;
import org.mrn.jpa.model.tags.TagEntity;
import org.mrn.query.model.AlbumTag;
import org.mrn.query.model.Category;
import org.mrn.query.model.Tag;

public class AlbumTagBuilder implements ModelBuilder<AlbumTag, AlbumTagEntity> {

	public static AlbumTag buildFrom(Long albumId, List<AlbumTagEntity> entityList) {
		AlbumTag albumTag = new AlbumTag();
		albumTag.setAlbumId(albumId);
		Map<Long, Category> tagMap = new HashMap<>();

		List<Tag> tags = entityList.stream().map(tagAlbum -> {
			TagEntity tag = tagAlbum.getTag();
			Long categoryId = tag.getCategory().getId();
			if (!tagMap.containsKey(categoryId)) {
				tagMap.put(categoryId, new Category(categoryId, tag.getCategory().getName()));
			}
			return TagBuilder.buildFrom(tag, tagAlbum.getRelevance());
		}).toList();
		albumTag.setTags(tags);
		albumTag.setCategories(new ArrayList<>(tagMap.values()));
		return albumTag;
	}

	private AlbumTagBuilder() {
	}

	@Override
	public AlbumTag build(AlbumTagEntity entity) {
		throw new UnsupportedOperationException();
	}
}
