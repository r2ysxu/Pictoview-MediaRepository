package org.mrn.query.model;


public class CoverImage implements QueryModel {
	private Long albumId;
	private Long imageId;

	public Long getAlbumId() {
		return albumId;
	}
	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}
	public Long getImageId() {
		return imageId;
	}
	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}
}
