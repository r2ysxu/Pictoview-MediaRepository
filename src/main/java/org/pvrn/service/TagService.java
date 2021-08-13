package org.pvrn.service;

import java.util.ArrayList;
import java.util.List;

import org.pvrn.jpa.model.album.ImageAlbum;
import org.pvrn.jpa.model.tags.AlbumTag;
import org.pvrn.jpa.model.tags.Category;
import org.pvrn.jpa.model.tags.Tag;
import org.pvrn.jpa.repo.AlbumTagRepo;
import org.pvrn.jpa.repo.CategoryRepo;
import org.pvrn.jpa.repo.ImageAlbumRepo;
import org.pvrn.jpa.repo.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

	public void tagAlbum(org.pvrn.query.model.AlbumTag albumTag) {
		ImageAlbum imageAlbum = imageAlbumRepo.findById(albumTag.getAlbumId()).get();
		for (org.pvrn.query.model.Category categoryQuery : albumTag.getCategories()) {
			Category category = categoryRepo.findById(categoryQuery.getId()).get();
			for (String tagName : categoryQuery.getTags()) {
				Tag tag = tagRepo.findByNameAndCategory(tagName, category);
				if (tag == null) {
					tag = tagRepo.save(new Tag(category, tagName));
				}
				albumTagRepo.save(new AlbumTag(imageAlbum, tag));
			}
		}
	}

	public List<Category> listCategories() {
		List<Category> categories = new ArrayList<>();
		categoryRepo.findAll().forEach(categories::add);
		return categories;
	}

	@Transactional
	public List<Tag> listTags(Long categoryId) {
		Category category = categoryRepo.findById(categoryId).get();
		category.getTags().size();
		return category.getTags();
	}
}
