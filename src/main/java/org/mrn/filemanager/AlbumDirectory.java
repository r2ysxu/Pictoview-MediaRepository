package org.mrn.filemanager;

import java.util.ArrayList;
import java.util.List;

public class AlbumDirectory {

	private String absolutePath;
	private String infoJson;
	private List<AlbumDirectory> subAlbums;
	private List<AlbumMediaFile> mediaFiles;

	public AlbumDirectory(String absolutePath) {
		this.absolutePath = absolutePath;
		this.subAlbums = new ArrayList<>();
		this.mediaFiles = new ArrayList<>();
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public AlbumDirectory setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
		return this;
	}

	public String getInfoJson() {
		return infoJson;
	}

	public void setInfoJson(String infoJson) {
		this.infoJson = infoJson;
	}

	public List<AlbumDirectory> getSubAlbums() {
		return subAlbums;
	}

	public AlbumDirectory setSubAlbums(List<AlbumDirectory> subAlbums) {
		this.subAlbums = subAlbums;
		return this;
	}

	public List<AlbumMediaFile> getMediaFiles() {
		return mediaFiles;
	}

	public AlbumDirectory setMediaFiles(List<AlbumMediaFile> mediaFiles) {
		this.mediaFiles = mediaFiles;
		return this;
	}

	public AlbumDirectory addAlbumDirectory(AlbumDirectory albumFolder) {
		subAlbums.add(albumFolder);
		return this;
	}
	
	public AlbumDirectory addMediaFile(AlbumMediaFile albumMediaFile) {
		mediaFiles.add(albumMediaFile);
		return this;
	}
}
