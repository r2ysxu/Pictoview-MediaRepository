package org.mrn.service;

import java.util.ArrayList;
import java.util.HashSet;
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

	private void findAndSetPreexistingTags(List<Tag> tags) {
		tags.stream().forEach(tag -> {
			if (tag.getId() == null) {
				TagEntity tagEntity = tagRepo.findByNameAndCategory_Id(tag.getValue(), tag.getCategoryId());
				if (tagEntity != null) {
					tag.setId(tagEntity.getId());
				}
			}
		});
	}

	private List<TagEntity> persistAndFetchTags(List<Tag> newTags) {
		findAndSetPreexistingTags(newTags);
		List<Long> existingTagIds = newTags.stream().filter(tag -> tag.getId() != null).map(Tag::getId).toList();
		List<TagEntity> newTagEntities = newTags.stream().filter(tag -> tag.getId() == null)
				.map(tag -> new TagEntity(new CategoryEntity(tag.getCategoryId()), tag.getValue(), tag.getRelevance()))
				.toList();

		List<TagEntity> existingTags = tagRepo.findAllByIdIn(existingTagIds);
		tagRepo.saveAll(newTagEntities).forEach(tag -> existingTags.add(tag));
		return existingTags;
	}

	private List<Tag> removeDuplicateTags(List<Tag> tags) {
		Set<String> duplicateValues = new HashSet<>();
		Set<String> duplicateIds = new HashSet<>();
		return tags.stream().filter(tag -> {
			String keyId = tag.getCategoryId() + "_" + tag.getId();
			if (duplicateValues.contains(tag.getValue()) || duplicateIds.contains(keyId)) return false;
			if (tag.getId() != null) duplicateIds.add(keyId);
			if (tag.getValue() != null && !tag.getValue().isEmpty()) {
				duplicateValues.add(tag.getValue());
			}
			return true;
		}).collect(Collectors.toList());
	}

	@Transactional
	public void tagAlbum(EndUserEntity user, Long albumId, List<Tag> newTags) throws AlbumNotFound {
		Set<Long> categoryIds = newTags.stream().map(tag -> tag.getCategoryId()).filter(Objects::nonNull)
				.collect(Collectors.toSet());
		AlbumEntity imageAlbum = imageAlbumRepo.findByOwnerAndId(user, albumId);
		if (imageAlbum == null) throw new AlbumNotFound(user, albumId);
		List<TagEntity> tagEntity = persistAndFetchTags(removeDuplicateTags(newTags));

		Map<Long, AlbumTagEntity> existingAlbumTags = albumTagRepo
				.findAllByAlbum_IdAndTag_Category_IdIn(albumId, new ArrayList<>(categoryIds)).stream()
				.collect(Collectors.toMap(existingAlbumTag -> existingAlbumTag.getTag().getId(), Function.identity()));

		List<AlbumTagEntity> newAlbumTags = tagEntity.stream().map(tag -> {
			if (existingAlbumTags.containsKey(tag.getId())) {
				existingAlbumTags.remove(tag.getId());
				return null;
			}
			return new AlbumTagEntity(imageAlbum, tag, tag.getRelevance());
		}).filter(Objects::nonNull).toList();

		// Unset binding
		existingAlbumTags.values().forEach(existingAlbumTag -> {
			existingAlbumTag.setAlbum(null);
			existingAlbumTag.setTag(null);
		});
		albumTagRepo.saveAll(existingAlbumTags.values());
		albumTagRepo.deleteAll(existingAlbumTags.values());
		albumTagRepo.saveAll(newAlbumTags);
	}

	public List<Category> listCategories() {
		return CategoryBuilder.buildList(categoryRepo.findAll());
	}

	public List<Category> searchCategories(String categoryName, Integer pageSize, Integer pageNumber) {
		return CategoryBuilder.buildFrom(categoryRepo.findByNameContainingIgnoreCase(categoryName,
				PageRequest.of(pageNumber, pageSize, Sort.unsorted())));
	}

	public List<Tag> listTags(Long categoryId) {
		return TagBuilder.buildFromList(tagRepo.findAllByCategory_IdOrderByName(categoryId));
	}

	public List<Tag> searchTags(String tagName, Long categoryId, Integer pageSize, Integer pageNumber) {
		return TagBuilder.buildFromList(
				tagRepo.findByNameContainingIgnoreCaseAndCategory_Id(tagName, categoryId, PageRequest.of(pageNumber, pageSize, Sort.unsorted())));
	}
}
