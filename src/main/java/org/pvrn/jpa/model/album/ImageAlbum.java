package org.pvrn.jpa.model.album;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.pvrn.jpa.model.user.User;

@Entity
@PrimaryKeyJoinColumn(name = "albumid")
public class ImageAlbum extends Album {

	@Column
	private String subtitle;
	@OneToMany
	private List<ImageMedia> photos;

	public ImageAlbum() {
	}

	public ImageAlbum(User owner, String name, String description) {
		super(owner, name, description);
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public List<ImageMedia> getPhotos() {
		return photos;
	}

	public void setPhotos(List<ImageMedia> photos) {
		this.photos = photos;
	}
}
