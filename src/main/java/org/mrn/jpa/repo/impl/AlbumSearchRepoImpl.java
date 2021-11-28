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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.tags.AlbumTagEntity;
import org.mrn.jpa.model.tags.CategoryEntity;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.tags.TagEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.mrn.jpa.repo.AlbumSearchRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class AlbumSearchRepoImpl implements AlbumSearchRepo {

	@PersistenceContext
	private EntityManager entityManager;

	private Predicate generateFilters(CriteriaBuilder cb, Root<AlbumEntity> album, UserEntity user, SearchQuery searchQuery) {
		Join<AlbumEntity, AlbumTagEntity> albumTag = album.join("albumTags", JoinType.LEFT);
		Join<AlbumTagEntity, TagEntity> tagPath = albumTag.join("tag", JoinType.LEFT);
		Join<TagEntity, CategoryEntity> categoryPath = tagPath.join("category", JoinType.LEFT);

		Path<String> tagName = tagPath.get("name");
		Path<String> categoryName = categoryPath.get("name");

		List<Predicate> whereTags = new ArrayList<>();
		Map<String, List<String>> categoryTags = searchQuery.getTags();
		for (Entry<String, List<String>> tags : categoryTags.entrySet()) {
			for (String tag : tags.getValue())
				whereTags.add(cb.and(cb.equal(categoryName, tags.getKey()), cb.equal(tagName, tag)));
		}
		String namePattern = StringUtils.isEmpty(searchQuery.getName()) ? "%" : '%' + searchQuery.getName().toUpperCase() + '%';

		return cb.and(
				cb.equal(album.get("owner"), user),
				cb.or(
					cb.like(cb.upper(album.get("name")), namePattern),
					cb.or(whereTags.toArray(new Predicate[whereTags.size()]))
				)
			);
	}

	private CriteriaQuery<AlbumEntity> createSearchQuery(CriteriaBuilder cb, UserEntity user, SearchQuery searchQuery) {
		CriteriaQuery<AlbumEntity> query = cb.createQuery(AlbumEntity.class);
		Root<AlbumEntity> album = query.from(AlbumEntity.class);

		query.select(album).where(generateFilters(cb, album, user, searchQuery));
		return query;
	}
	
	private CriteriaQuery<Long> createSearchCountQuery(CriteriaBuilder cb, UserEntity user, SearchQuery searchQuery) {
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<AlbumEntity> countAlbum = countQuery.from(AlbumEntity.class);
		countQuery
			.select(cb.count(countAlbum))
			.where(generateFilters(cb, countAlbum, user, searchQuery));
		return countQuery;
	}

	@Override
	public Page<AlbumEntity> searchAlbums(UserEntity user, SearchQuery searchQuery, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		List<AlbumEntity> items = entityManager
				.createQuery(createSearchQuery(cb, user, searchQuery))
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		Long count = entityManager
				.createQuery(createSearchCountQuery(cb, user, searchQuery))
				.getSingleResult();
		return new PageImpl<AlbumEntity>(items, pageable, count);
	}
}
