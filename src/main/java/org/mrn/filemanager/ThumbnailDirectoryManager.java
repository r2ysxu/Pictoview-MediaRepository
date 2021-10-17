package org.mrn.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

public class ThumbnailDirectoryManager {

	private String rootDirectoryPath;

	private static Set<String> imageExtensions = new HashSet<>(List.of("jpg", "jpeg", "png", "gif"));

	public ThumbnailDirectoryManager(String rootDirectoryPath) {
		this.rootDirectoryPath = rootDirectoryPath;
	}

	public ThumbnailDirectories listSubdirectories(String currentPath) {
		String rootPath = new File(rootDirectoryPath).getAbsolutePath().replace(":\\", ":\\\\") + "\\\\";
		File current = new File(rootDirectoryPath + currentPath);
		if (!current.exists() || !current.isDirectory())
			return new ThumbnailDirectories();
		List<File> subDirectories = Arrays.asList(current.listFiles());

		List<ThumbnailDirectory> directories = new ArrayList<>(subDirectories.size());
		List<String> subDirectoriesAbsolutePaths = new ArrayList<>(directories.size());
		for (File file : subDirectories) {
			if (file.isDirectory()) {
				subDirectoriesAbsolutePaths.add(file.getAbsolutePath());
				ThumbnailDirectory directory = new ThumbnailDirectory(
						file.getAbsolutePath().replaceFirst(rootPath, "/"), file.getName());
				directories.add(directory);
			}
		}

		return new ThumbnailDirectories(directories, subDirectoriesAbsolutePaths);
	}

	public List<ThumbnailFile> listImageFiles(String currentPath) {
		File current = new File(rootDirectoryPath + currentPath);
		List<File> subFiles = Arrays.asList(current.listFiles());

		List<ThumbnailFile> imageFiles = new ArrayList<>();
		for (File file : subFiles) {
			if (file.isFile() && FilenameUtils.isExtension(file.getAbsolutePath(), imageExtensions)) {
				String absolutePath = file.getAbsolutePath();
				ThumbnailFile thumbnailFile = new ThumbnailFile(absolutePath, file.getName(),
						FilenameUtils.getExtension(absolutePath));
				imageFiles.add(thumbnailFile);
			}
		}
		return imageFiles;
	}
}
