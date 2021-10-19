package org.mrn.jpa.model.album;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.tags.AlbumTagEntity;
import org.mrn.jpa.model.user.UserEntity;

@Entity
public abstract class AlbumEntity implements EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@Column(nullable = false)
	private String name;
	@ManyToOne
	private UserEntity owner;
	@ManyToOne
	private AlbumEntity parent;
	@OneToMany
	private List<AlbumTagEntity> albumTags;

	protected AlbumEntity() {
	}

	protected AlbumEntity(UserEntity owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public AlbumEntity setName(String name) {
		this.name = name;
		return this;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public AlbumEntity setOwner(UserEntity owner) {
		this.owner = owner;
		return this;
	}

	public AlbumEntity getParent() {
		return parent;
	}

	public AlbumEntity setParent(AlbumEntity parent) {
		this.parent = parent;
		return this;
	}

	public List<AlbumTagEntity> getAlbumTags() {
		return albumTags;
	}

	public AlbumEntity setAlbumTags(List<AlbumTagEntity> albumTags) {
		this.albumTags = albumTags;
		return this;
	}

	public AlbumEntity addAlbumTag(AlbumTagEntity albumTag) {
		if (this.albumTags == null)
			this.albumTags = new ArrayList<>();
		this.albumTags.add(albumTag);
		return this;
	}
}
