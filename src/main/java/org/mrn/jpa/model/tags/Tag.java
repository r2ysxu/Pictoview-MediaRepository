package org.mrn.jpa.model.tags;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	@Column(nullable = false)
	private String name;
	@ManyToOne
	private Category category;

	public Tag() {
	}

	public Tag(Category category, String name) {
		this.category = category;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Tag setName(String name) {
		this.name = name;
		return this;
	}

	public Category getCategory() {
		return category;
	}

	public Tag setCategory(Category category) {
		this.category = category;
		return this;
	}
}
