package org.mrn.filemanager;

import org.mrn.jpa.model.album.MediaType;

public class ThumbnailFile {
	private String absoluteFile;
	private String name;
	private MediaType type;

	public ThumbnailFile(String absoluteFile, String name, String type) {
		this.absoluteFile = absoluteFile;
		this.name = name;
		if ("JPG".equalsIgnoreCase(type) || "JPEG".equalsIgnoreCase(type)) {
			this.type = MediaType.JPG;
		} else if ("PNG".equalsIgnoreCase(type)) {
			this.type = MediaType.PNG;
		} else if ("GIF".equalsIgnoreCase(type)) {
			this.type = MediaType.GIF;
		} else if ("MP4".equalsIgnoreCase(type)) {
			this.type = MediaType.MP4;
		}
	}

	public String getAbsoluteFile() {
		return absoluteFile;
	}

	public void setAbsoluteFile(String absoluteFile) {
		this.absoluteFile = absoluteFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MediaType getType() {
		return type;
	}

	public void setType(MediaType type) {
		this.type = type;
	}
}
