package org.mrn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mrn.exceptions.AlbumNotFound;
import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.tags.AlbumTagEntity;
import org.mrn.jpa.model.tags.CategoryEntity;
import org.mrn.jpa.model.tags.TagEntity;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.jpa.repo.AlbumRepo;
import org.mrn.jpa.repo.AlbumTagRepo;
import org.mrn.jpa.repo.CategoryRepo;
import org.mrn.jpa.repo.TagRepo;
import org.mrn.query.model.AlbumTag;
import org.mrn.query.model.Category;
import org.mrn.query.model.Tag;
import org.mrn.service.builder.AlbumTagBuilder;
import org.mrn.service.builder.CategoryBuilder;
import org.mrn.service.builder.TagBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

	@Autowired
	private AlbumRepo imageAlbumRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private TagRepo tagRepo;
	@Autowired
	private AlbumTagRepo albumTagRepo;

	public Category createCategory(String categoryName) {
		CategoryEntity category = categoryRepo.findByName(categoryName);
		if (category == null) {
			category = categoryRepo.save(new CategoryEntity(categoryName));
		}
		return CategoryBuilder.buildFrom(category);
	}

	public AlbumTag listAlbumTags(Long albumId) {
		return AlbumTagBuilder.buildFrom(albumId, albumTagRepo.findAllByAlbum_Id(albumId));
	}

	private List<TagEntity> persistTags(List<Category> categories) {
		List<Long> existingTagIds = new ArrayList<>();
		List<TagEntity> newTags = new ArrayList<>();
		for (Category categoryQuery : categories) {
			CategoryEntity category = categoryRepo.findById(categoryQuery.getId()).get();
			for (Tag tagQuery : categoryQuery.getTags()) {
				if (tagQuery.getId() == null) {
					TagEntity tag = tagRepo.findByNameAndCategory(tagQuery.getValue(), category);
					if (tag == null) {
						newTags.add(new TagEntity(category, tagQuery.getValue()));
					} else {
						tagQuery.setId(tag.getId());
						existingTagIds.add(tagQuery.getId());
					}
				} else {
					existingTagIds.add(tagQuery.getId());
				}
			}
		}
		List<TagEntity> tags = tagRepo.findAllByIdIn(existingTagIds);
		tagRepo.saveAll(newTags).forEach(tag -> tags.add(tag));
		return tags;
	}

	@Transactional
	public void tagAlbum(EndUserEntity user, Long albumId, List<Category> categories) throws AlbumNotFound {
		AlbumEntity imageAlbum = imageAlbumRepo.findByOwnerAndId(user, albumId);
		if (imageAlbum == null) throw new AlbumNotFound(user, albumId);
		Set<Long> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toSet());
		List<TagEntity> tags = persistTags(categories);

		Map<Long, AlbumTagEntity> existingAlbumTags = albumTagRepo.findAllByAlbum_Id(albumId).stream()
				.filter(existingAlbumTag -> categoryIds.contains(existingAlbumTag.getTag().getCategory().getId()))
				.collect(Collectors.toMap(existingAlbumTag -> existingAlbumTag.getTag().getId(), Function.identity()));

		List<AlbumTagEntity> albumTags = tags.stream().map(tag -> {
			if (existingAlbumTags.containsKey(tag.getId())) {
				existingAlbumTags.remove(tag.getId());
				return null;
			}
			return new AlbumTagEntity(imageAlbum, tag);
		}).filter(Objects::nonNull).collect(Collectors.toList());

		// Unset binding
		existingAlbumTags.values().forEach(existingAlbumTag -> {
			existingAlbumTag.setAlbum(null);
			existingAlbumTag.setTag(null);
		});
		albumTagRepo.saveAll(existingAlbumTags.values());
		albumTagRepo.deleteAll(existingAlbumTags.values());
		albumTagRepo.saveAll(albumTags);
	}

	public List<Category> listCategories() {
		return CategoryBuilder.buildFrom(categoryRepo.findAll());
	}

	public List<Category> searchCategories(String categoryName, Integer pageSize, Integer pageNumber) {
		return CategoryBuilder.buildFrom(categoryRepo.findByNameContainingIgnoreCase(categoryName,
				PageRequest.of(pageNumber, pageSize, Sort.unsorted())));
	}

	@Transactional
	public List<Tag> listTags(Long categoryId) {
		CategoryEntity category = categoryRepo.findById(categoryId).get();
		return TagBuilder.buildFromList(category.getTags());
	}

	public List<Tag> searchTags(String tagName, Integer pageSize, Integer pageNumber) {
		return TagBuilder.buildFromList(tagRepo.findByNameContainingIgnoreCase(tagName, PageRequest.of(pageNumber, pageSize, Sort.unsorted())));
	}
}
