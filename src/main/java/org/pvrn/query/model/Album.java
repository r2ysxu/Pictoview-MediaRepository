package org.pvrn.query.model;

import org.pvrn.jpa.model.album.MediaAlbum;

public class Album {

	private Long id;
	private String name;
	private String publisher;
	private String description;
	private Long coverPhotoId;

	public static Album createImageAlbum(MediaAlbum imageAlbum) {
		return new Album(imageAlbum.getId(), imageAlbum.getName(), imageAlbum.getDescription(),
				imageAlbum.getSubtitle(), imageAlbum.getCoverPhoto().getId());
	}

	private Album(Long id, String name, String description, String subtitle, Long coverPhotoId) {
		this.id = id;
		this.name = name;
		this.publisher = subtitle;
		this.description = description;
		this.coverPhotoId = coverPhotoId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCoverPhotoId() {
		return coverPhotoId;
	}

	public void setCoverPhotoId(Long coverPhotoId) {
		this.coverPhotoId = coverPhotoId;
	}
}
