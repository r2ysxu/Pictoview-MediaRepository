package org.mrn.jpa.repo;

import java.util.List;
import java.util.Optional;

import org.mrn.jpa.model.tags.Category;
import org.mrn.jpa.model.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepo extends PagingAndSortingRepository<Tag, Long> {
	public Optional<Tag> findById(Long id);
	public Tag findByNameAndCategory(String name, Category category);
	public Tag findFirstByName(String name);
	public List<Tag> findAllByIdIn(List<Long> ids);
	public List<Tag> findByNameContainingIgnoreCase(String name, Pageable pageable);
}