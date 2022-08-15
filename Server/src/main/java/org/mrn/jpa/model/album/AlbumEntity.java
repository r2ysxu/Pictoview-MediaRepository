package org.mrn.jpa.model.album;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.UpdateTimestamp;
import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.tags.AlbumTagEntity;
import org.mrn.jpa.model.user.UserEntity;

@Entity
public class AlbumEntity implements EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@UpdateTimestamp
	private Date updatedAt;
	@Column(nullable = false)
	private String name;
	@Column
	private String altname;
	@ManyToOne
	private UserEntity owner;
	@ManyToOne
	private AlbumEntity parent;
	@OneToMany(mappedBy = "album")
	private List<AlbumTagEntity> albumTags;
	@Column
	private String subtitle;
	@Column
	private String description;
	@Column
	private Integer rating;
	@Column
	private String metaType;
	@ManyToOne(fetch = FetchType.EAGER)
	private ImageMediaEntity coverPhoto;
	@OneToMany(mappedBy = "album")
	private List<MediaEntity> media;

	protected AlbumEntity() {
	}

	protected AlbumEntity(UserEntity owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	public AlbumEntity(UserEntity owner, String name, String altname, String subtitle, String description, Integer rating) {
		this(owner, name);
		this.altname = altname;
		this.subtitle = subtitle;
		this.description = description;
		this.rating = rating;
	}

	public AlbumEntity(UserEntity owner, String name, String altname, String subtitle, String description) {
		this(owner, name, altname, subtitle, description, null);
	}

	public Long getId() {
		return id;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public AlbumEntity setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
		return this;
	}

	public String getName() {
		return name;
	}

	public AlbumEntity setName(String name) {
		this.name = name;
		return this;
	}

	public String getAltname() {
		return altname;
	}

	public AlbumEntity setAltname(String altname) {
		this.altname = altname;
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

	public String getSubtitle() {
		return subtitle;
	}

	public AlbumEntity setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public AlbumEntity setDescription(String description) {
		this.description = description;
		return this;
	}

	public Integer getRating() {
		return rating;
	}

	public AlbumEntity setRating(Integer rating) {
		this.rating = rating;
		return this;
	}

	public String getMetaType() {
		return metaType;
	}

	public AlbumEntity setMetaType(String metaType) {
		this.metaType = metaType;
		return this;
	}

	public ImageMediaEntity getCoverPhoto() {
		return coverPhoto;
	}

	public AlbumEntity setCoverPhoto(ImageMediaEntity coverPhoto) {
		this.coverPhoto = coverPhoto;
		return this;
	}

	public List<MediaEntity> getMedia() {
		return media;
	}

	public AlbumEntity setMedia(List<MediaEntity> media) {
		this.media = media;
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
