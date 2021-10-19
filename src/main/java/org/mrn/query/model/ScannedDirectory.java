package org.mrn.query.model;

import org.mrn.filemanager.ThumbnailDirectory;

public class ScannedDirectory implements QueryModel {
	private String path;
	private String name;
	private boolean scanned;

	public ScannedDirectory(ThumbnailDirectory directory, boolean scanned) {
		this.path = directory.getRelativePath();
		this.name = directory.getName();
		this.scanned = scanned;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isScanned() {
		return scanned;
	}

	public void setScanned(boolean scanned) {
		this.scanned = scanned;
	}
}
