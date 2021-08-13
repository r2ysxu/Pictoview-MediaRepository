package org.pvrn.query.model;

import java.util.List;

public class AlbumTag {
	private Long albumId;
	private List<Category> Categories;

	public AlbumTag() {
	}

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	public List<Category> getCategories() {
		return Categories;
	}

	public void setCategories(List<Category> categories) {
		Categories = categories;
	}

}
