package org.mrn.jpa.model.album;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.user.UserEntity;

@Entity
@PrimaryKeyJoinColumn(name = "mediaid")
public class VideoMediaEntity extends MediaEntity implements EntityModel {

	@Enumerated(EnumType.ORDINAL)
	private MediaType type;
	
	public VideoMediaEntity() {}
	
	public VideoMediaEntity(UserEntity owner, String source, String name, MediaType type, MediaAlbumEntity album) {
		super(owner, source, name, album);
		this.type = type;
	}

	public MediaType getType() {
		return type;
	}

	public void setType(MediaType type) {
		this.type = type;
	}
}
