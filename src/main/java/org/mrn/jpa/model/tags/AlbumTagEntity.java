package org.mrn.jpa.model.tags;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.album.AlbumEntity;

@Entity
public class AlbumTagEntity implements EntityModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	@OneToOne(cascade = CascadeType.ALL)
	private TagEntity tag;
	@OneToOne(cascade = CascadeType.ALL)
	private AlbumEntity album;
	@Column
	private int relevance;
	
	public AlbumTagEntity() {}
	
	public AlbumTagEntity(AlbumEntity album, TagEntity tag) {
		this.album = album;
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

	public AlbumEntity getAlbum() {
		return album;
	}

	public void setAlbum(AlbumEntity album) {
		this.album = album;
	}

	public int getRelevance() {
		return relevance;
	}

	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
}
