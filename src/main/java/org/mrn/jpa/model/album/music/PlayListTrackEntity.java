package org.mrn.jpa.model.album.music;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.album.MediaEntity;

@Entity
public class PlayListTrackEntity implements EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@ManyToOne
	PlayListEntity playlist;
	@OneToOne
	MediaEntity track;
	@Column
	private Integer trackNumber;

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PlayListEntity getPlaylist() {
		return playlist;
	}

	public void setPlaylist(PlayListEntity playlist) {
		this.playlist = playlist;
	}

	public MediaEntity getTrack() {
		return track;
	}

	public void setTrack(MediaEntity track) {
		this.track = track;
	}

	public Integer getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(Integer trackNumber) {
		this.trackNumber = trackNumber;
	}

}
