package org.mrn.filemanager;

public class ThumbnailDirectory {

	private String relativePath;
	private String name;

	public ThumbnailDirectory(String relativePath, String name) {
		super();
		this.relativePath = relativePath;
		this.name = name;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
