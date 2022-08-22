package org.mrn.exceptions;

public class ImageNotFound extends Exception {
	private static final long serialVersionUID = 1L;

	public ImageNotFound(Long mediaId) {
		super("Image not found with media id " + mediaId);
	}
}
