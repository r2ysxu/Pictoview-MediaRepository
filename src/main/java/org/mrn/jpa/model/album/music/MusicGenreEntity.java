package org.mrn.jpa.model.album.music;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.mrn.jpa.model.EntityModel;

@Entity
public class MusicGenreEntity implements EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@Column(nullable = false)
	private String name;
	@OneToOne
	private MusicGenreEntity parent;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public MusicGenreEntity setName(String name) {
		this.name = name;
		return this;
	}

	public MusicGenreEntity getParent() {
		return parent;
	}

	public MusicGenreEntity setParent(MusicGenreEntity parent) {
		this.parent = parent;
		return this;
	}
}
