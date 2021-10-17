package org.mrn.filemanager;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.common.io.Files;

public class AlbumFileManager {

	public static boolean createAlbumDirectory(String path) {
		return false;
	}

	public static void createCoverPhotoFile(String source, String destination) throws IOException {
		File sourceFile = new File(source);
		createMediumPhoto(sourceFile, new File(destination));
	}

	private static void createMediumPhoto(File sourceFile, File destinationFile) throws IOException {
		String ext = Files.getFileExtension(sourceFile.getName());
		BufferedImage scaledImageBuffer = scaleImageRatio(sourceFile, 800, 600);
		ImageIO.write(scaledImageBuffer, ext, destinationFile);
	}

	private static BufferedImage scaleImage(File sourceFile, int width, int height) throws IOException {
		// Scale Image
		BufferedImage image = ImageIO.read(sourceFile);
		Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		int imageType = image.getType();
		if (imageType == 0)
			imageType = BufferedImage.TYPE_INT_ARGB;
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
		if (imageType == 0)
			imageType = BufferedImage.TYPE_INT_ARGB;
		BufferedImage retImage = new BufferedImage((int) scaleW, (int) scaleH, imageType);
		retImage.getGraphics().drawImage(scaledImage, 0, 0, null);
		return retImage;
	}

	public static void createPhotoThumbnail(String source, String destination) throws IOException {
		File sourceFile = new File(source);
		String ext = Files.getFileExtension(source);
		File thumbnailFile = new File(destination);
		if (!thumbnailFile.getParentFile().exists())
			thumbnailFile.getParentFile().mkdirs();

		BufferedImage scaledImageBuffer = scaleImage(sourceFile, 256, 256);
		ImageIO.write(scaledImageBuffer, ext, thumbnailFile);
	}
}