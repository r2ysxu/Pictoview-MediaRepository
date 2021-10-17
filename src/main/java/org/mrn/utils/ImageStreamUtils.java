package org.mrn.utils;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.stream.ImageInputStream;

public class ImageStreamUtils {
	public static void writeImageStreamToResponse(ImageInputStream is, OutputStream out) {
		try {
			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = is.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			is.close();
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
