package org.mrn.jpa.model.file;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.album.AlbumEntity;

@Entity
public class DirectoryAddedEntity implements EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	@Column
	private String absolutePath;
	@OneToOne
	private AlbumEntity album;

	public DirectoryAddedEntity() {
	}

	public DirectoryAddedEntity(String absolutePath, AlbumEntity album) {
		this.absolutePath = absolutePath;
		this.album = album;
	}

	public Long getId() {
		return id;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public AlbumEntity getAlbum() {
		return album;
	}

	public void setAlbum(AlbumEntity album) {
		this.album = album;
	}

}
