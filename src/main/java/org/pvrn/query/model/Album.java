package org.pvrn.query.model;

import org.pvrn.jpa.model.album.ImageAlbum;

public class Album {

	private static final String ImageMedia = "Image";
	// private static final String VideoMedia = "Video";

	private Long id;
	private String name;
	private String publisher;
	private String description;
	private Long coverPhotoId;
	private String mediaType;

	public static Album createImageAlbum(ImageAlbum imageAlbum) {
		return new Album(imageAlbum.getId(), imageAlbum.getName(), imageAlbum.getDescription(),
				imageAlbum.getSubtitle(), imageAlbum.getCoverPhoto().getId(), ImageMedia);
	}

	private Album(Long id, String name, String description, String subtitle, Long coverPhotoId, String mediaType) {
		this.id = id;
		this.name = name;
		this.publisher = subtitle;
		this.description = description;
		this.coverPhotoId = coverPhotoId;
		this.mediaType = mediaType;
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

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
}
