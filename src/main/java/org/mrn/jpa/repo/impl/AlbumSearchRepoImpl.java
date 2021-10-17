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

import org.mrn.jpa.model.album.Album;
import org.mrn.jpa.model.tags.AlbumTag;
import org.mrn.jpa.model.tags.Category;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.tags.Tag;
import org.mrn.jpa.model.user.User;
import org.mrn.jpa.repo.AlbumSearchRepo;
import org.springframework.data.domain.Pageable;

public class AlbumSearchRepoImpl implements AlbumSearchRepo {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Album> searchAlbums(User user, SearchQuery searchQuery, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Album> query = cb.createQuery(Album.class);
		Root<Album> album = query.from(Album.class);

		Join<Album, AlbumTag> albumTag = album.join("albumTags");
		Join<AlbumTag, Tag> tagPath = albumTag.join("tag");
		Join<Tag, Category> categoryPath = tagPath.join("category");

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
