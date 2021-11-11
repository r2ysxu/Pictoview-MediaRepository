package org.mrn.filemanager;

public class AudioMediaFile {
	private String title;
	private String genre;
	private String artist;
	private Integer trackNumber;

	public String getTitle() {
		return title;
	}

	public AudioMediaFile setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getGenre() {
		return genre;
	}

	public AudioMediaFile setGenre(String genre) {
		this.genre = genre;
		return this;
	}

	public String getArtist() {
		return artist;
	}

	public AudioMediaFile setArtist(String artist) {
		this.artist = artist;
		return this;
	}

	public Integer getTrackNumber() {
		return trackNumber;
	}

	public AudioMediaFile setTrackNumber(Integer trackNumber) {
		this.trackNumber = trackNumber;
		return this;
	}
	
	public String toString() {
		return trackNumber + ". " + title + " - " + artist + " (" + genre + ")";
	}
}
