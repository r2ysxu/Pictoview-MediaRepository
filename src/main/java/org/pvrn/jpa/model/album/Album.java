package org.pvrn.jpa.model.album;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.pvrn.jpa.model.tags.AlbumTag;
import org.pvrn.jpa.model.user.User;

@Entity
public class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@Column(nullable = false)
	private String name;
	@Column
	private String description;
	@Column
	private String metaType;
	@ManyToOne
	private User owner;
	@ManyToOne
	private Album parent;
	@ManyToOne(fetch = FetchType.EAGER)
	private ImageMedia coverPhoto;
	@OneToMany
	private List<AlbumTag> albumTags;

	public Album() {
	}

	public Album(User owner, String name, String description) {
		this.owner = owner;
		this.name = name;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMetaType() {
		return metaType;
	}

	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Album getParent() {
		return parent;
	}

	public void setParent(Album parent) {
		this.parent = parent;
	}

	public ImageMedia getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(ImageMedia coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	public List<AlbumTag> getAlbumTags() {
		return albumTags;
	}

	public void setAlbumTags(List<AlbumTag> albumTags) {
		this.albumTags = albumTags;
	}

	public void addAlbumTag(AlbumTag albumTag) {
		if (this.albumTags == null)
			this.albumTags = new ArrayList<>();
		this.albumTags.add(albumTag);
	}
}
