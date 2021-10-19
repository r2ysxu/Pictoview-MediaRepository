package org.mrn.jpa.model.tags;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.mrn.jpa.model.EntityModel;

@Entity
public class TagEntity implements EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	@Column(nullable = false)
	private String name;
	@ManyToOne
	private CategoryEntity category;

	public TagEntity() {
	}

	public TagEntity(CategoryEntity category, String name) {
		this.category = category;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public TagEntity setName(String name) {
		this.name = name;
		return this;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public TagEntity setCategory(CategoryEntity category) {
		this.category = category;
		return this;
	}
}
