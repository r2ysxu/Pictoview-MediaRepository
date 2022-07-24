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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

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
import org.springframework.data.domain.Sort;

public class AlbumSearchRepoImpl implements AlbumSearchRepo {

	@PersistenceContext
	private EntityManager entityManager;

	private Predicate generateFilters(CriteriaBuilder cb,
			@SuppressWarnings("rawtypes") CriteriaQuery query,
			Root<AlbumEntity> album, UserEntity user, SearchQuery searchQuery) {
		List<Predicate> whereTags = new ArrayList<>();

		if (!StringUtils.isEmpty(searchQuery.getName())) {
			whereTags.add(cb.like(cb.upper(album.get("name")), '%' + searchQuery.getName().toUpperCase() + '%'));
		}
		if (!searchQuery.getTags().isEmpty()) {
			Map<String, List<String>> categoryTags = searchQuery.getTags();
			for (Entry<String, List<String>> tags : categoryTags.entrySet()) {
				for (String tag : tags.getValue()) {
					Subquery<Long> tagQuery = generateSubQueryTagFilter(cb, query.subquery(Long.class), tags.getKey(), tag);
					whereTags.add(cb.in(album.get("id")).value(tagQuery));
				}
			}
		}

		return cb.and(
				cb.equal(album.get("owner"), user),
				cb.and(whereTags.toArray(new Predicate[whereTags.size()]))
			);
	}

	private Subquery<Long> generateSubQueryTagFilter(CriteriaBuilder cb, Subquery<Long> tagQuery, String categoryName, String tagName) {
		Root<AlbumTagEntity> albumTag = tagQuery.from(AlbumTagEntity.class);
		Join<AlbumTagEntity, TagEntity> tagPath = albumTag.join("tag");
		Join<TagEntity, CategoryEntity> categoryPath = tagPath.join("category");

		Path<String> tagNamePath = tagPath.get("name");
		Path<String> categoryNamePath = categoryPath.get("name");
		return tagQuery.select(albumTag.get("album").get("id")).where(
				cb.and(
					cb.equal(cb.upper(categoryNamePath), categoryName.toUpperCase()),
					cb.equal(cb.upper(tagNamePath), tagName.toUpperCase())
				)
			);
	}

	private CriteriaQuery<AlbumEntity> createSearchQuery(final CriteriaBuilder cb, UserEntity user, SearchQuery searchQuery, Sort sortable) {
		CriteriaQuery<AlbumEntity> query = cb.createQuery(AlbumEntity.class);
		Root<AlbumEntity> album = query.from(AlbumEntity.class);

		List<Order> orderable = sortable.get().map( sort -> {
			if (sort.isDescending()) return cb.desc(album.get(sort.getProperty()));
			else return cb.asc(album.get(sort.getProperty()));
		}).toList();

		query.select(album).where(generateFilters(cb, query, album, user, searchQuery)).orderBy(orderable);
		return query;
	}

	private CriteriaQuery<Long> createSearchCountQuery(CriteriaBuilder cb, UserEntity user, SearchQuery searchQuery) {
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<AlbumEntity> countAlbum = countQuery.from(AlbumEntity.class);
		countQuery
			.select(cb.count(countAlbum))
			.where(generateFilters(cb, countQuery, countAlbum, user, searchQuery));
		return countQuery;
	}

	@Override
	public Page<AlbumEntity> searchAlbums(UserEntity user, SearchQuery searchQuery, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		List<AlbumEntity> items = entityManager
				.createQuery(createSearchQuery(cb, user, searchQuery, pageable.getSort()))
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		Long count = entityManager
				.createQuery(createSearchCountQuery(cb, user, searchQuery))
				.getSingleResult();
		return new PageImpl<AlbumEntity>(items, pageable, count);
	}
}
