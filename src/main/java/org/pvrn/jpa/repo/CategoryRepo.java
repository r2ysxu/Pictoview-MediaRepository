package org.pvrn.jpa.repo;

import org.pvrn.jpa.model.tags.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepo extends CrudRepository<Category, Long> {
	public Category findByName(String name);
}
