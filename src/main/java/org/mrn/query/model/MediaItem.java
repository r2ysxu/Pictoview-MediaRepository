package org.mrn.query.model;

public class MediaItem implements QueryModel {
	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public MediaItem setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaItem setName(String name) {
		this.name = name;
		return this;
	}

}
