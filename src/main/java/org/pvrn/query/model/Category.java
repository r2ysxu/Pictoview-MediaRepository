package org.pvrn.query.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

	private Long id;
	private String name;
	private List<String> tags;

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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
