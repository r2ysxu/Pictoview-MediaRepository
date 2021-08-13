package org.pvrn.jpa.model.album;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.pvrn.jpa.model.user.User;

@Entity
@PrimaryKeyJoinColumn(name = "albumid")
public class VideoAlbum extends Album {

	@OneToMany
	private List<VideoMedia> videos;
	
	public VideoAlbum() {}

	public VideoAlbum(User owner, String name, String description) {
		super(owner, name, description);
	}

	public List<VideoMedia> getVideos() {
		return videos;
	}

	public void setVideos(List<VideoMedia> videos) {
		this.videos = videos;
	}
}
