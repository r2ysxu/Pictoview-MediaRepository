package org.mrn.jpa.repo;

import java.util.List;
import java.util.Optional;

import org.mrn.jpa.model.tags.TagEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepo extends PagingAndSortingRepository<TagEntity, Long> {
	public Optional<TagEntity> findById(Long id);
	public TagEntity findByNameAndCategory_Id(String name, Long categoryId);
	public TagEntity findFirstByName(String name);
	public List<TagEntity> findAllByCategory_IdOrderByName(Long categoryId);
	public List<TagEntity> findAllByIdIn(List<Long> ids);
	public List<TagEntity> findByNameContainingIgnoreCaseAndCategory_Id(String name, Long categoryId, Pageable pageable);
}