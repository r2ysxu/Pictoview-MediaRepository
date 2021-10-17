package org.mrn.jpa.model.album;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.mrn.jpa.model.user.User;

@Entity
@PrimaryKeyJoinColumn(name = "albumid")
public class MediaAlbum extends Album {

	@Column
	private String subtitle;
	@Column
	private String description;
	@Column
	private String metaType;
	@ManyToOne(fetch = FetchType.EAGER)
	private ImageMedia coverPhoto;
	@OneToMany
	private List<Media> media;

	public MediaAlbum() {
	}

	public MediaAlbum(User owner, String name, String description) {
		super(owner, name);
		this.description = description;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public MediaAlbum setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public MediaAlbum setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getMetaType() {
		return metaType;
	}

	public MediaAlbum setMetaType(String metaType) {
		this.metaType = metaType;
		return this;
	}

	public ImageMedia getCoverPhoto() {
		return coverPhoto;
	}

	public MediaAlbum setCoverPhoto(ImageMedia coverPhoto) {
		this.coverPhoto = coverPhoto;
		return this;
	}

	public List<Media> getMedia() {
		return media;
	}

	public MediaAlbum setMedia(List<Media> media) {
		this.media = media;
		return this;
	}
}
