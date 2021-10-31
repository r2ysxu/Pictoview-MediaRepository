package org.mrn.jpa.model.album;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.jpa.model.user.UserEntity;

@Entity
public class MediaEntity implements EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@ManyToOne
	private UserEntity owner;
	@Column(nullable = false)
	private String name;
	@Column
	private String source;
	@ManyToOne
	private AlbumEntity album;

	public MediaEntity() {}

	public MediaEntity(UserEntity owner, String source, String name, AlbumEntity album) {
		this.owner = owner;
		this.source = source;
		this.name = name;
		this.album = album;
	}

	public Long getId() {
		return id;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(EndUserEntity owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public AlbumEntity getAlbum() {
		return album;
	}

	public void setAlbum(AlbumEntity album) {
		this.album = album;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}
}
