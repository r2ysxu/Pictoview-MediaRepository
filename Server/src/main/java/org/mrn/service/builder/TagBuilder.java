package org.mrn.service.builder;

import java.util.List;
import java.util.stream.Collectors;

import org.mrn.jpa.model.tags.TagEntity;
import org.mrn.query.model.Tag;

public class TagBuilder extends TagEntity implements ModelBuilder<Tag, TagEntity> {

	public static Tag buildFrom(TagEntity entity, Integer relevance) {
		entity.setRelevance(relevance);
		return new TagBuilder().build(entity);
	}

	public static List<Tag> buildFromList(List<TagEntity> entities) {
		if (entities == null) return null;
		return entities.stream().map(entity -> TagBuilder.buildFrom(entity, null)).collect(Collectors.toList());
	}

	private TagBuilder() {
	}

	@Override
	public Tag build(TagEntity entity) {
		return new Tag(entity.getId(), entity.getCategory().getId(), entity.getName(), entity.getRelevance());
	}
}
