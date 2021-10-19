package org.mrn.service.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mrn.jpa.model.tags.AlbumTagEntity;
import org.mrn.jpa.model.tags.TagEntity;
import org.mrn.query.model.AlbumTag;
import org.mrn.query.model.Category;

public class AlbumTagBuilder implements ModelBuilder<AlbumTag, AlbumTagEntity> {

	public static AlbumTag buildFrom(Long albumId, List<AlbumTagEntity> entityList) {
		AlbumTag albumTag = new AlbumTag();
		albumTag.setAlbumId(albumId);
		albumTag.setCategories(generateCategories(entityList));
		return albumTag;
	}

	private static List<Category> generateCategories(List<AlbumTagEntity> entityList) {
		List<TagEntity> tags = entityList.stream().map(albumTag -> albumTag.getTag()).toList();
		Map<Long, Category> tagMap = new HashMap<>();
		tags.forEach(tag -> {
			Long categoryId = tag.getCategory().getId();
			if (!tagMap.containsKey(categoryId)) {
				tagMap.put(categoryId, new Category(categoryId, tag.getCategory().getName()));
			}
			tagMap.get(categoryId).addTag(TagBuilder.buildFrom(tag));
		});
		return new ArrayList<>(tagMap.values());
	}

	private AlbumTagBuilder() {}

	@Override
	public AlbumTag build(AlbumTagEntity entity) {
		throw new UnsupportedOperationException();
	}
}
