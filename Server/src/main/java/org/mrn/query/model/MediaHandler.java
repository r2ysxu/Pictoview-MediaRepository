package org.mrn.query.model;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public class MediaHandler {
	private Long fileSize;
	private Long contentLength;
	private Long startRange;
	private Long endRange;
	private StreamingResponseBody body;

	public MediaHandler() {}

	public MediaHandler(final RandomAccessFile file, Long startRange, Long endRange) throws IOException {
		this.fileSize = file.length();
		this.startRange = startRange;
		this.endRange = Math.min(fileSize - 1, endRange);
		this.contentLength = this.endRange - startRange + 1;

		byte[] buffer = new byte[1024];
		body = os -> {
			file.seek(startRange);
			while (file.getFilePointer() < this.endRange) {
				file.read(buffer);
				os.write(buffer);
			}
			os.flush();
		};
	}

	public String getMediaType() {
		return "video/mp4";
	}

	public Long getFileSize() {
		return fileSize;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public Long getStartRange() {
		return startRange;
	}

	public Long getEndRange() {
		return endRange;
	}

	public StreamingResponseBody getBody() {
		return body;
	}
}
