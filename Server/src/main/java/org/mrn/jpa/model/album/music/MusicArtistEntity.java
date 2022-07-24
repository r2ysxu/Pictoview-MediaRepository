package org.mrn.jpa.model.album.music;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.mrn.jpa.model.EntityModel;

@Entity
public class MusicArtistEntity implements EntityModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@Column(nullable = false)
	private String name;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public MusicArtistEntity setName(String name) {
		this.name = name;
		return this;
	}
}
