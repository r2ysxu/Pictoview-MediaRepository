package org.mrn.jpa.model.album;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.UpdateTimestamp;
import org.mrn.jpa.model.EntityModel;
import org.mrn.jpa.model.tags.MediaTagEntity;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.jpa.model.user.UserEntity;

@Entity
public class MediaEntity implements EntityModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@UpdateTimestamp
	private Date updatedAt;
	@ManyToOne
	private UserEntity owner;
	@Column(nullable = false)
	private String name;
	@Column
	private String source;
	@ManyToOne
	private AlbumEntity album;
	@OneToMany(mappedBy = "medium")
	private List<MediaTagEntity> mediaTags;

	public MediaEntity() {}

	public MediaEntity(UserEntity owner, String source, String name, AlbumEntity album) {
		this.owner = owner;
		this.source = source;
		this.name = name;
		this.album = album;
	}

	public Long getId() {
		return id;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(EndUserEntity owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public AlbumEntity getAlbum() {
		return album;
	}

	public void setAlbum(AlbumEntity album) {
		this.album = album;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

	public List<MediaTagEntity> getMediaTags() {
		return mediaTags;
	}

	public void setMediaTags(List<MediaTagEntity> mediaTags) {
		this.mediaTags = mediaTags;
	}

}
