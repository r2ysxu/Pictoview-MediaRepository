package org.mrn.filemanager;

import java.util.List;

public class ThumbnailDirectories {
	List<ThumbnailDirectory> directories;
	List<String> subDirectoriesAbsolutePaths;

	public ThumbnailDirectories() {
	}

	public ThumbnailDirectories(List<ThumbnailDirectory> directories, List<String> subDirectoriesAbsolutePaths) {
		this.directories = directories;
		this.subDirectoriesAbsolutePaths = subDirectoriesAbsolutePaths;
	}

	public List<ThumbnailDirectory> getDirectories() {
		return directories;
	}

	public void setDirectories(List<ThumbnailDirectory> directories) {
		this.directories = directories;
	}

	public List<String> getSubDirectoriesAbsolutePaths() {
		return subDirectoriesAbsolutePaths;
	}

	public void setSubDirectoriesAbsolutePaths(List<String> subDirectoriesAbsolutePaths) {
		this.subDirectoriesAbsolutePaths = subDirectoriesAbsolutePaths;
	}

}
