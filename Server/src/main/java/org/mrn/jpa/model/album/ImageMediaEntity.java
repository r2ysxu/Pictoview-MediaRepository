package org.mrn.jpa.model.album;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.user.UserEntity;

@Entity
public class ImageMediaEntity extends MediaEntity implements EntityModel {
	@Enumerated(EnumType.ORDINAL)
	private MediaType type;
	@Column
	private String thumbnailSource;

	public ImageMediaEntity() {
	}

	public ImageMediaEntity(UserEntity owner, String source, String name, MediaType type, AlbumEntity album) {
		super(owner, source, name, album);
		this.type = type;
	}

	public MediaType getType() {
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

	public void setType(MediaType type) {
		this.type = type;
	}

	public String getThumbnailSource() {
		return thumbnailSource;
	}

	public void setThumbnailSource(String thumbnailSource) {
		this.thumbnailSource = thumbnailSource;
	}
}
