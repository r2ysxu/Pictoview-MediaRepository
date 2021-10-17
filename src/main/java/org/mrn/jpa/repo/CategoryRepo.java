package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.tags.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepo extends PagingAndSortingRepository<Category, Long> {
	public Category findByName(String name);
	public List<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
