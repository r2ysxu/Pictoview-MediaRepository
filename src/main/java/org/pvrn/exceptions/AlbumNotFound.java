package org.pvrn.exceptions;

import org.pvrn.jpa.model.user.User;

public class AlbumNotFound extends Exception {
	private static final long serialVersionUID = 1L;

	public AlbumNotFound(User user, Long albumId) {
		super("Album(" + albumId + ") with accessed by " + user.getUsername() + " not found");
	}
}
