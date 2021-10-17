package org.mrn.jpa.model.album;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.mrn.jpa.model.user.User;

@Entity
@PrimaryKeyJoinColumn(name = "mediaid")
public class ImageMedia extends Media {
	public enum Type {
		JPG, GIF, PNG
	};

	@Enumerated(EnumType.ORDINAL)
	private Type type;
	@Column
	private String thumbnailSource;
	@ManyToOne
	private MediaAlbum album;

	public ImageMedia() {
	}

	public ImageMedia(User owner, String source, String name, Type type, MediaAlbum album) {
		super(owner, source, name);
		this.album = album;
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
	public String getTypeExtension() {
		if (type == null) return "";
		switch(type) {
		case JPG: return ".jpg";
		case GIF: return ".gif";
		case PNG: return ".png";
		default: return "";
		}
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getThumbnailSource() {
		return thumbnailSource;
	}

	public void setThumbnailSource(String thumbnailSource) {
		this.thumbnailSource = thumbnailSource;
	}

	public MediaAlbum getAlbum() {
		return album;
	}

	public void setAlbum(MediaAlbum album) {
		this.album = album;
	}
}
