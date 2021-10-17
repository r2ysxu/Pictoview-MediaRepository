package org.mrn.query.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

	private Long id;
	private String name;
	private List<Tag> tags;
	
	public static Category createCategory(org.mrn.jpa.model.tags.Category category) {
		return new Category(category.getId(), category.getName());
	}

	public Category() {
	}

	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
		this.tags = new ArrayList<>(0);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

}
