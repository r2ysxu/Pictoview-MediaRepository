package org.mrn.jpa.model.album;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.user.UserEntity;

@Entity
@PrimaryKeyJoinColumn(name = "albumid")
public class MediaAlbumEntity extends AlbumEntity implements EntityModel {

	@Column
	private String subtitle;
	@Column
	private String description;
	@Column
	private String metaType;
	@ManyToOne(fetch = FetchType.EAGER)
	private ImageMediaEntity coverPhoto;
	@OneToMany
	private List<MediaEntity> media;

	public MediaAlbumEntity() {
	}

	public MediaAlbumEntity(UserEntity owner, String name, String description) {
		super(owner, name);
		this.description = description;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public MediaAlbumEntity setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public MediaAlbumEntity setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getMetaType() {
		return metaType;
	}

	public MediaAlbumEntity setMetaType(String metaType) {
		this.metaType = metaType;
		return this;
	}

	public ImageMediaEntity getCoverPhoto() {
		return coverPhoto;
	}

	public MediaAlbumEntity setCoverPhoto(ImageMediaEntity coverPhoto) {
		this.coverPhoto = coverPhoto;
		return this;
	}

	public List<MediaEntity> getMedia() {
		return media;
	}

	public MediaAlbumEntity setMedia(List<MediaEntity> media) {
		this.media = media;
		return this;
	}
}
