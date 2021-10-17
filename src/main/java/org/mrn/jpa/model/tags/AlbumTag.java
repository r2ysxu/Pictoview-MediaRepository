package org.mrn.jpa.model.tags;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.mrn.jpa.model.album.Album;

@Entity
public class AlbumTag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	@OneToOne(cascade = CascadeType.ALL)
	private Tag tag;
	@OneToOne(cascade = CascadeType.ALL)
	private Album album;
	@Column
	private int revalance;
	
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

	public int getRevalance() {
		return revalance;
	}

	public void setRevalance(int revalance) {
		this.revalance = revalance;
	}
}
