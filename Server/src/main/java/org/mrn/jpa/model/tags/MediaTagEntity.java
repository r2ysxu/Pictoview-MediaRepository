package org.mrn.jpa.model.tags;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.album.MediaEntity;

@Entity
public class MediaTagEntity implements EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	@OneToOne(cascade = CascadeType.ALL)
	private TagEntity tag;
	@OneToOne(cascade = CascadeType.ALL)
	private MediaEntity medium;
	@Column
	private int relevance;

	public MediaTagEntity() {}

	public MediaTagEntity(MediaEntity medium, TagEntity tag) {
		this.medium = medium;
		this.tag = tag;
	}

	public Long getId() {
		return id;
	}

	public TagEntity getTag() {
		return tag;
	}

	public void setTag(TagEntity tag) {
		this.tag = tag;
	}

	public MediaEntity getMedium() {
		return medium;
	}

	public void setMedium(MediaEntity medium) {
		this.medium = medium;
	}

	public int getRelevance() {
		return relevance;
	}

	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
}
