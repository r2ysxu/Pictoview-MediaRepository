
package org.mrn.exceptions;

public class InvalidMediaAlbumException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidMediaAlbumException(Long albumId, Long mediaId) {
		super("Media Id " + mediaId + " does not belong to the album " + albumId);
	}
}
