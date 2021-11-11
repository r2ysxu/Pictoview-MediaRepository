package org.mrn.query.model;

public class AudioMediaItem extends MediaItem implements QueryModel {

	private String title;
	private String artist;
	private String genre;
	private Integer trackNumber;

	public String getTitle() {
		return title;
	}

	public AudioMediaItem setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getArtist() {
		return artist;
	}

	public AudioMediaItem setArtist(String artist) {
		this.artist = artist;
		return this;
	}

	public String getGenre() {
		return genre;
	}

	public AudioMediaItem setGenre(String genre) {
		this.genre = genre;
		return this;
	}

	public Integer getTrackNumber() {
		return trackNumber;
	}

	public AudioMediaItem setTrackNumber(Integer trackNumber) {
		this.trackNumber = trackNumber;
		return this;
	}
}
