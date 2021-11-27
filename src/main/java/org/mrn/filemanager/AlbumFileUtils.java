package org.mrn.filemanager;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mrn.filemanager.AlbumMediaFile.Type;
import org.mrn.jpa.model.album.MediaType;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class AlbumFileUtils {

	private static final String INFO_FILE = "albuminfo.json";
	private static Set<String> imageExtensions = new HashSet<>(List.of("jpg", "jpeg", "png", "gif"));
	private static Set<String> audioExtensions = new HashSet<>(List.of("mp3"));
	private static Set<String> videoExtensions = new HashSet<>(List.of("mp4"));

	public static boolean createAlbumDirectory(String path) {
		return false;
	}

	public static void createCoverPhotoFile(String source, String destination) throws IOException {
		File sourceFile = new File(source);
		createMediumPhoto(sourceFile, new File(destination));
	}

	private static void createMediumPhoto(File sourceFile, File destinationFile) throws IOException {
		String ext = FilenameUtils.getExtension(sourceFile.getName());
		BufferedImage scaledImageBuffer = scaleImageRatio(sourceFile, 800, 600);
		ImageIO.write(scaledImageBuffer, ext, destinationFile);
	}

	private static BufferedImage scaleImage(File sourceFile, int width, int height) throws IOException {
		// Scale Image
		BufferedImage image = ImageIO.read(sourceFile);
		Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		int imageType = image.getType();
		if (imageType == 0) imageType = BufferedImage.TYPE_INT_ARGB;
		BufferedImage retImage = new BufferedImage(width, height, imageType);
		retImage.getGraphics().drawImage(scaledImage, 0, 0, null);
		return retImage;
	}

	private static BufferedImage scaleImageRatio(File sourceFile, int width, int height) throws IOException {
		BufferedImage image = ImageIO.read(sourceFile);

		// Find scale dimensions
		double scaleH = height, scaleW = width;
		if (image.getWidth() > 800 || image.getHeight() > 600) {
			double ratio = 1;
			if (image.getHeight() > image.getWidth()) {
				ratio = ((double) height) / ((double) image.getHeight());
				scaleH = height;
				scaleW = image.getWidth() * ratio;
			} else {
				ratio = ((double) width) / ((double) image.getWidth());
				scaleW = width;
				scaleH = (double) image.getHeight() * ratio;
			}
		}

		Image scaledImage = image.getScaledInstance((int) scaleW, (int) scaleH, Image.SCALE_SMOOTH);
		int imageType = image.getType();
		if (imageType == 0) imageType = BufferedImage.TYPE_INT_ARGB;
		BufferedImage retImage = new BufferedImage((int) scaleW, (int) scaleH, imageType);
		retImage.getGraphics().drawImage(scaledImage, 0, 0, null);
		return retImage;
	}

	public static void createPhotoThumbnail(String source, String destination) throws IOException {
		File sourceFile = new File(source);
		String ext = FilenameUtils.getExtension(source);
		File thumbnailFile = new File(destination);
		if (!thumbnailFile.getParentFile().exists()) thumbnailFile.getParentFile().mkdirs();

		BufferedImage scaledImageBuffer = scaleImage(sourceFile, 256, 256);
		ImageIO.write(scaledImageBuffer, ext, thumbnailFile);
	}

	public static void unzipFolder(String sourceStr, String targetStr) throws IOException {
		Path source = Paths.get(sourceStr);
		Path target = Paths.get(targetStr);
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				Path newPath = zipSlipProtect(zipEntry, target);

				if (zipEntry.isDirectory()) {
					Files.createDirectories(newPath);
				} else {
					if (newPath.getParent() != null) {
						if (Files.notExists(newPath.getParent())) {
							Files.createDirectories(newPath.getParent());
						}
					}
					Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
				}
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
		}
	}

	private static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {
		Path targetDirResolved = targetDir.resolve(zipEntry.getName());
		Path normalizePath = targetDirResolved.normalize();
		if (!normalizePath.startsWith(targetDir)) {
			throw new IOException("Bad zip entry: " + zipEntry.getName());
		}
		return normalizePath;
	}
	
	private static MediaType getMediaTypeFromPath(String filePath) {
		String extension = FilenameUtils.getExtension(filePath);
		switch(extension.toLowerCase()) {
			case "jpeg":
			case "jpg": return MediaType.JPG;
			case "png": return MediaType.PNG;
			case "gif": return MediaType.GIF;
			case "mp4": return MediaType.MP4;
			default: return null;
		}
	}

	public static AlbumMediaFile generateAlbumMediaFile(File file) {
		AlbumMediaFile albumMediaFile = new AlbumMediaFile()
				.setAbsolutePath(file.getAbsolutePath())
				.setName(file.getName())
				.setMediaType(getMediaTypeFromPath(file.getAbsolutePath()));
		if (FilenameUtils.isExtension(file.getName(), imageExtensions)) {
			albumMediaFile.setType(Type.IMAGE);
		} else if (FilenameUtils.isExtension(file.getName(), videoExtensions)) {
			albumMediaFile.setType(Type.VIDEO);
		} else if(FilenameUtils.isExtension(file.getName(), audioExtensions)) {
			albumMediaFile.setType(Type.AUDIO);
		} else {
			return null;
		}
		return albumMediaFile;
	}

	public static AudioMediaFile parseAudioFile(String filePath) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = new Mp3File(filePath);
		Integer track;
		try {
			track = Integer.parseInt(mp3File.getId3v2Tag().getTrack());
		} catch(Exception ex) {
			track = 0;
		}
		return new AudioMediaFile()
				.setTitle(mp3File.getId3v1Tag().getTitle())
				.setArtist(mp3File.getId3v2Tag().getArtist())
				.setGenre(mp3File.getId3v1Tag().getGenreDescription())
				.setTrackNumber(track);
	}

	public static AlbumDirectory generateAlbumFolder(String filePath, Boolean loadMetadata) throws IOException {
		AlbumDirectory albumFolder = new AlbumDirectory(filePath);
		File albumFile = new File(filePath);
		if (loadMetadata) albumFolder.setInfoJson(FileUtils.readFileToString(new File(filePath + "/" + INFO_FILE), "UTF-8"));
		List<File> subFiles = Arrays.asList(albumFile.listFiles());
		for (File file : subFiles) {
			if (file.isDirectory()) {
				albumFolder.addAlbumDirectory(generateAlbumFolder(file.getAbsolutePath(), loadMetadata));
			} else if (INFO_FILE.equals(file.getName())) {
			} else {
				AlbumMediaFile mediaFile = generateAlbumMediaFile(file);
				if (mediaFile != null) albumFolder.addMediaFile(mediaFile);
			}
		}
		return albumFolder;
	}
}