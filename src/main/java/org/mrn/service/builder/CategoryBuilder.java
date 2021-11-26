package org.mrn.service.builder;

import java.util.ArrayList;
import java.util.List;

import org.mrn.jpa.model.tags.CategoryEntity;
import org.mrn.query.model.Category;

public class CategoryBuilder implements ModelBuilder<Category, CategoryEntity> {

	public static Category buildFrom(CategoryEntity entity) {
		return new CategoryBuilder().build(entity);
	}

	public static List<Category> buildFrom(Iterable<CategoryEntity> entities) {
		List<Category> categories = new ArrayList<>();
		entities.forEach(category -> categories.add(CategoryBuilder.buildFrom(category)));
		return categories;
	}

	private CategoryBuilder() {}

	@Override
	public Category build(CategoryEntity entity) {
		Category category = new Category(entity.getId(), entity.getName());
		category.setTags(TagBuilder.buildFromList(entity.getTags()));
		return category;
	}
}
