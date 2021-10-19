package org.mrn.jpa.model.tags;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.mrn.jpa.model.EntityModel;

@Entity
public class CategoryEntity implements EntityModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	@Column(unique = true, nullable = false)
	private String name;
	@OneToMany
	private List<TagEntity> tags;
	
	public CategoryEntity() {}
	
	public CategoryEntity(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TagEntity> getTags() {
		return tags;
	}

	public void setTags(List<TagEntity> tags) {
		this.tags = tags;
	}
}
