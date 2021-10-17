package org.pvrn.jpa.model.album;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.pvrn.jpa.model.tags.AlbumTag;
import org.pvrn.jpa.model.user.User;

@Entity
public abstract class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@Column(nullable = false)
	private String name;
	@ManyToOne
	private User owner;
	@ManyToOne
	private Album parent;
	@OneToMany
	private List<AlbumTag> albumTags;

	protected Album() {
	}

	protected Album(User owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Album setName(String name) {
		this.name = name;
		return this;
	}

	public User getOwner() {
		return owner;
	}

	public Album setOwner(User owner) {
		this.owner = owner;
		return this;
	}

	public Album getParent() {
		return parent;
	}

	public Album setParent(Album parent) {
		this.parent = parent;
		return this;
	}

	public List<AlbumTag> getAlbumTags() {
		return albumTags;
	}

	public Album setAlbumTags(List<AlbumTag> albumTags) {
		this.albumTags = albumTags;
		return this;
	}

	public Album addAlbumTag(AlbumTag albumTag) {
		if (this.albumTags == null)
			this.albumTags = new ArrayList<>();
		this.albumTags.add(albumTag);
		return this;
	}
}
