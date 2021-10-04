package org.pvrn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.pvrn.jpa.model.album.ImageAlbum;
import org.pvrn.jpa.model.tags.AlbumTag;
import org.pvrn.jpa.model.tags.Category;
import org.pvrn.jpa.model.tags.Tag;
import org.pvrn.jpa.repo.AlbumTagRepo;
import org.pvrn.jpa.repo.CategoryRepo;
import org.pvrn.jpa.repo.ImageAlbumRepo;
import org.pvrn.jpa.repo.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

	@Autowired
	private ImageAlbumRepo imageAlbumRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private TagRepo tagRepo;
	@Autowired
	private AlbumTagRepo albumTagRepo;

	public Category createCategory(String categoryName) {
		Category category = categoryRepo.findByName(categoryName);
		if (category == null) {
			category = categoryRepo.save(new Category(categoryName));
		}
		return category;
	}

	public org.pvrn.query.model.AlbumTag listAlbumTags(Long albumId) {
		org.pvrn.query.model.AlbumTag albumTags = new org.pvrn.query.model.AlbumTag();
		albumTags.setAlbumId(albumId);

		List<AlbumTag> albumTagList = albumTagRepo.findAllByAlbum_Id(albumId);
		List<Tag> tags = albumTagList.stream().map(albumTag -> albumTag.getTag()).toList();
		Map<Long, org.pvrn.query.model.Category> tagMap = new HashMap<>();
		tags.forEach(tag -> {
			Long categoryId = tag.getCategory().getId();
			if (!tagMap.containsKey(categoryId)) {
				tagMap.put(categoryId, new org.pvrn.query.model.Category(categoryId, tag.getCategory().getName()));
			}
			tagMap.get(categoryId).addTag(org.pvrn.query.model.Tag.createTag(tag));
		});

		albumTags.setCategories(new ArrayList<>(tagMap.values()));
		return albumTags;
	}

	private List<Tag> persistTags(org.pvrn.query.model.AlbumTag albumTag) {
		List<Long> existingTagIds = new ArrayList<>();
		List<Tag> newTags = new ArrayList<>();
		for (org.pvrn.query.model.Category categoryQuery : albumTag.getCategories()) {
			Category category = categoryRepo.findById(categoryQuery.getId()).get();
			for (org.pvrn.query.model.Tag tagQuery : categoryQuery.getTags()) {
				if (tagQuery.getId() == null) {
					newTags.add(new Tag(category, tagQuery.getValue()));
				} else {
					existingTagIds.add(tagQuery.getId());
				}
			}
		}
		List<Tag> tags = tagRepo.findAllByIdIn(existingTagIds);
		tagRepo.saveAll(newTags).forEach(tag -> tags.add(tag));
		return tags;
	}

	@Transactional
	public void tagAlbum(org.pvrn.query.model.AlbumTag albumTag) {
		ImageAlbum imageAlbum = imageAlbumRepo.findById(albumTag.getAlbumId()).get();
		Set<Long> categoryIds = albumTag.getCategories().stream().map(org.pvrn.query.model.Category::getId)
				.collect(Collectors.toSet());
		List<Tag> tags = persistTags(albumTag);

		Map<Long, AlbumTag> existingAlbumTags = albumTagRepo.findAllByAlbum_Id(albumTag.getAlbumId()).stream()
				.filter(existingAlbumTag -> categoryIds.contains(existingAlbumTag.getTag().getCategory().getId()))
				.collect(Collectors.toMap(existingAlbumTag -> existingAlbumTag.getTag().getId(), Function.identity()));

		List<AlbumTag> albumTags = tags.stream().map(tag -> {
			if (existingAlbumTags.containsKey(tag.getId())) {
				existingAlbumTags.remove(tag.getId());
				return null;
			}
			return new AlbumTag(imageAlbum, tag);
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
		List<Category> categories = new ArrayList<>();
		categoryRepo.findAll().forEach(categories::add);
		return categories;
	}

	public List<Category> searchCategories(String categoryName, Integer pageSize, Integer pageNumber) {
		return categoryRepo.findByNameContainingIgnoreCase(categoryName, PageRequest.of(pageNumber, pageSize, Sort.unsorted()));
	}

	@Transactional
	public List<Tag> listTags(Long categoryId) {
		Category category = categoryRepo.findById(categoryId).get();
		category.getTags().size();
		return category.getTags();
	}

	public List<Tag> searchTags(String tagName, Integer pageSize, Integer pageNumber) {
		return tagRepo.findByNameContainingIgnoreCase(tagName, PageRequest.of(pageNumber, pageSize, Sort.unsorted()));
	}
}
