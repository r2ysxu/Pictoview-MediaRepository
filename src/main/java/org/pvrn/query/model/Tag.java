package org.pvrn.query.model;

public class Tag {

	private Long categoryId;
	private String name;

	public Tag() {}

	public Tag(String name) {
		this.categoryId = 0L;
		this.name = name;
	}

	public Tag(Long categoryId, String name) {
		this.categoryId = categoryId;
		this.name = name;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
