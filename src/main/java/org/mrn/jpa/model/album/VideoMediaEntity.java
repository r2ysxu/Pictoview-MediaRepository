package org.mrn.jpa.model.album;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.mrn.jpa.model.EntityModel;

@Entity
@PrimaryKeyJoinColumn(name = "mediaid")
public class VideoMediaEntity extends MediaEntity implements EntityModel {
	public enum Type {
		MP4
	};

	@Enumerated(EnumType.ORDINAL)
	private Type type;
	@ManyToOne
	private MediaAlbumEntity album;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public MediaAlbumEntity getAlbum() {
		return album;
	}

	public void setAlbum(MediaAlbumEntity album) {
		this.album = album;
	}
}
