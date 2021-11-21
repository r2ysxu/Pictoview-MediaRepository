package org.mrn.query.model;

public class Album implements QueryModel {

	private Long id;
	private String name;
	private String publisher;
	private String description;
	private Long coverPhotoId;
	private Integer rating;

	public Album() {}

	public Album(Long id, String name, String description, String subtitle, Long coverPhotoId, Integer rating) {
		this.id = id;
		this.name = name;
		this.publisher = subtitle;
		this.description = description;
		this.coverPhotoId = coverPhotoId;
		this.rating = rating;
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

	public Album setName(String name) {
		this.name = name;
		return this;
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

	public Album setDescription(String description) {
		this.description = description;
		return this;
	}

	public Long getCoverPhotoId() {
		return coverPhotoId;
	}

	public Album setCoverPhotoId(Long coverPhotoId) {
		this.coverPhotoId = coverPhotoId;
		return this;
	}

	public Integer getRating() {
		return rating;
	}

	public Album setRating(Integer rating) {
		this.rating = rating;
		return this;
	}

}
