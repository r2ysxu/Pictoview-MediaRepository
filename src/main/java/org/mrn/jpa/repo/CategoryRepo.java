package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.tags.CategoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepo extends PagingAndSortingRepository<CategoryEntity, Long> {
	public CategoryEntity findByName(String name);
	public List<CategoryEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
