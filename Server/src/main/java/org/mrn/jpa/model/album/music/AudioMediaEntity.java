package org.mrn.jpa.model.album.music;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.MediaEntity;
import org.mrn.jpa.model.album.MediaType;
import org.mrn.jpa.model.user.UserEntity;

@Entity
@PrimaryKeyJoinColumn(name = "mediaid")
public class AudioMediaEntity extends MediaEntity implements EntityModel {

	@Enumerated(EnumType.ORDINAL)
	private MediaType type;
	@OneToOne(fetch = FetchType.EAGER)
	private MusicGenreEntity genre;
	@OneToOne(fetch = FetchType.EAGER)
	private MusicArtistEntity artist;
	@OneToMany(fetch = FetchType.EAGER)
	private List<MusicArtistEntity> featuredArtists;
	@Column
	private String title;
	@Column
	private Integer trackNumber;

	public AudioMediaEntity() {}

	public AudioMediaEntity(UserEntity owner, String source, String name, MediaType type, AlbumEntity album) {
		super(owner, source, name, album);
		this.type = type;
	}

	public MediaType getType() {
		return type;
	}

	public AudioMediaEntity setType(MediaType type) {
		this.type = type;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MusicGenreEntity getGenre() {
		return genre;
	}

	public AudioMediaEntity setGenre(MusicGenreEntity genre) {
		this.genre = genre;
		return this;
	}

	public MusicArtistEntity getArtist() {
		return artist;
	}

	public AudioMediaEntity setArtist(MusicArtistEntity artist) {
		this.artist = artist;
		return this;
	}

	public List<MusicArtistEntity> getFeaturedArtists() {
		return featuredArtists;
	}

	public AudioMediaEntity setFeaturedArtists(List<MusicArtistEntity> featuredArtists) {
		this.featuredArtists = featuredArtists;
		return this;
	}

	public Integer getTrackNumber() {
		return trackNumber;
	}

	public AudioMediaEntity setTrackNumber(Integer trackNumber) {
		this.trackNumber = trackNumber;
		return this;
	}
}
