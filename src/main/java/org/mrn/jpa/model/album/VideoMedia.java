package org.mrn.jpa.model.album;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "mediaid")
public class VideoMedia extends Media {
	public enum Type {
		MP4
	};

	@Enumerated(EnumType.ORDINAL)
	private Type type;
	@ManyToOne
	private MediaAlbum album;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public MediaAlbum getAlbum() {
		return album;
	}

	public void setAlbum(MediaAlbum album) {
		this.album = album;
	}
}
