package org.mrn.jpa.model.file;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.mrn.jpa.model.album.Album;

@Entity
public class DirectoryAdded {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	@Column
	private String absolutePath;
	@OneToOne
	private Album album;

	public DirectoryAdded() {
	}

	public DirectoryAdded(String absolutePath, Album album) {
		this.absolutePath = absolutePath;
		this.album = album;
	}

	public long getId() {
		return id;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

}
