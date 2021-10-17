package org.mrn.query.model;

public class Tag {

	private Long id;
	private Long categoryId;
	private String value;
	private Integer relevance;

	public Tag() {}

	public static Tag createTag(org.mrn.jpa.model.tags.Tag tag) {
		return new Tag(tag.getId(), tag.getCategory().getId(), tag.getName());
	}

	public Tag(Long id, String value) {
		this.id = id;
		this.categoryId = 0L;
		this.value = value;
	}

	public Tag(Long id, Long categoryId, String value) {
		this.id = id;
		this.categoryId = categoryId;
		this.value = value;
	}
	
	public Tag(Long id, Long categoryId, String value, Integer relevance) {
		this.id = id;
		this.categoryId = categoryId;
		this.value = value;
		this.relevance = relevance;
	}

	public Long getId() {
		return id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getRelevance() {
		return relevance;
	}

	public void setRelevance(Integer relevance) {
		this.relevance = relevance;
	}
}
