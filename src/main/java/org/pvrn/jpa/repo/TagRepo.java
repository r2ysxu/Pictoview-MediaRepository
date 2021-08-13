package org.pvrn.jpa.repo;

import org.pvrn.jpa.model.tags.Category;
import org.pvrn.jpa.model.tags.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepo extends CrudRepository<Tag, Long> {
	public Tag findByNameAndCategory(String name, Category category);

	public Tag findFirstByName(String name);
}
