package org.mrn.query.model;

import java.util.List;

public class NewAlbumInfo implements QueryModel {

	private String name;
	private String altname;
	private String subtitle;
	private String description;
	private String coverPhotoName;
	private Integer rating;
	private String metaType;
	private List<Tag> tags;

	public NewAlbumInfo() {
	}

	public NewAlbumInfo(String name, String description, String subtitle, String coverPhotoName, Integer rating) {
		this.name = name;
		this.description = description;
		this.subtitle = subtitle;
		this.coverPhotoName = coverPhotoName;
		this.rating = rating;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAltname() {
		return altname;
	}

	public void setAltname(String altname) {
		this.altname = altname;
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

	public String getMetaType() {
		return metaType;
	}

	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
}
