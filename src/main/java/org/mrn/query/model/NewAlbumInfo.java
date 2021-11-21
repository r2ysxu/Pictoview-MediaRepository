package org.mrn.query.model;

import java.util.ArrayList;
import java.util.List;

public class NewAlbumInfo implements QueryModel {

	private String name;
	private String subtitle;
	private String description;
	private String coverPhotoName;
	private Integer rating;
	private List<Category> categories;

	public NewAlbumInfo() {
	}

	public NewAlbumInfo(String name, String description, String subtitle, String coverPhotoName, Integer rating) {
		this.name = name;
		this.description = description;
		this.subtitle = subtitle;
		this.coverPhotoName = coverPhotoName;
		this.rating = rating;
		this.categories = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCoverPhotoName() {
		return coverPhotoName;
	}

	public void setCoverPhotoName(String coverPhotoName) {
		this.coverPhotoName = coverPhotoName;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
