package org.pvrn.filemanager;

import org.pvrn.jpa.model.album.ImageMedia;
import org.pvrn.jpa.model.album.ImageMedia.Type;

public class ThumbnailFile {
	private String absoluteFile;
	private String name;
	private ImageMedia.Type type;

	public ThumbnailFile(String absoluteFile, String name, String type) {
		this.absoluteFile = absoluteFile;
		this.name = name;
		if ("JPG".equalsIgnoreCase(type) || "JPEG".equalsIgnoreCase(type)) {
			this.type = Type.JPG;
		} else if ("PNG".equalsIgnoreCase(type)) {
			this.type = Type.PNG;
		} else if ("GIF".equalsIgnoreCase(type)) {
			this.type = Type.GIF;
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

	public ImageMedia.Type getType() {
		return type;
	}

	public void setType(ImageMedia.Type type) {
		this.type = type;
	}
}
