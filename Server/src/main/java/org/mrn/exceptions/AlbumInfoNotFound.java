package org.mrn.exceptions;

public class AlbumInfoNotFound extends Exception  {
	private static final long serialVersionUID = 1L;

	public AlbumInfoNotFound(String path) {
		super("Album info file albuminfo.json is not found with this path " + path);
	}
}
