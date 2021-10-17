package org.mrn.query.model;

import java.util.List;

public class AlbumTag {
	private Long albumId;
	private List<Category> categories;

	public AlbumTag() {
	}

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

}
