package org.pvrn.jpa.model.tags;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.pvrn.jpa.model.album.Album;

@Entity
public class AlbumTag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	@OneToOne(cascade = CascadeType.DETACH)
	private Tag tag;
	@OneToOne(cascade = CascadeType.DETACH)
	private Album album;
	
	public AlbumTag() {}
	
	public AlbumTag(Album album, Tag tag) {
		this.album = album;
		this.tag = tag;
	}

	public long getId() {
		return id;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
}
