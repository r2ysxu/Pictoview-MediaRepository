package org.mrn.query.model;

import java.util.List;

public class AlbumTag implements QueryModel {
	private Long albumId;
	private List<Tag> tags;
	private List<Category> categories;

	public AlbumTag() {
	}

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
