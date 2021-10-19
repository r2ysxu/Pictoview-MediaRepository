package org.mrn.jpa.repo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.tags.AlbumTagEntity;
import org.mrn.jpa.model.tags.CategoryEntity;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.tags.TagEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.mrn.jpa.repo.AlbumSearchRepo;
import org.springframework.data.domain.Pageable;

public class AlbumSearchRepoImpl implements AlbumSearchRepo {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<AlbumEntity> searchAlbums(UserEntity user, SearchQuery searchQuery, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AlbumEntity> query = cb.createQuery(AlbumEntity.class);
		Root<AlbumEntity> album = query.from(AlbumEntity.class);

		Join<AlbumEntity, AlbumTagEntity> albumTag = album.join("albumTags");
		Join<AlbumTagEntity, TagEntity> tagPath = albumTag.join("tag");
		Join<TagEntity, CategoryEntity> categoryPath = tagPath.join("category");

		Path<String> tagName = tagPath.get("name");
		Path<String> categoryName = categoryPath.get("name");

		List<Predicate> whereTags = new ArrayList<>();
		Map<String, List<String>> categoryTags = searchQuery.getTags();
		for (Entry<String, List<String>> tags : categoryTags.entrySet()) {
			for (String tag : tags.getValue())
				whereTags.add(cb.and(cb.equal(categoryName, tags.getKey()), cb.equal(tagName, tag)));
		}
		query.select(album).where(
				cb.and(cb.equal(album.get("owner"), user), cb.or(whereTags.toArray(new Predicate[whereTags.size()]))));

		return entityManager.createQuery(query).setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize()).getResultList();
	}
}
