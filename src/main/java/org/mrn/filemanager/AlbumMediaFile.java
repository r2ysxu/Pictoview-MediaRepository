package org.mrn.filemanager;

import org.apache.commons.io.FilenameUtils;
import org.mrn.jpa.model.album.MediaType;

public class AlbumMediaFile {
	public enum Type {
		IMAGE, VIDEO, AUDIO
	};

	private String name;
	private String absolutePath;
	private MediaType mediaType;
	private Type type;

	public String getName() {
		return name;
	}

	public AlbumMediaFile setName(String name) {
		this.name = name;
		return this;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public AlbumMediaFile setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
		return this;
	}

	public String getExt() {
		return FilenameUtils.getExtension(absolutePath);
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public AlbumMediaFile setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
