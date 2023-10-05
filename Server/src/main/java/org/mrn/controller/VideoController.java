package org.mrn.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.mrn.exceptions.UnauthenticatedUserException;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.query.model.MediaHandler;
import org.mrn.query.model.MediaItem;
import org.mrn.query.model.PageItems;
import org.mrn.service.AlbumService;
import org.mrn.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class VideoController extends BaseController {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private VideoService videoService;
	@Autowired
	private AlbumService mediaAlbumService;

	@ResponseBody
	@GetMapping(value = "/album/videos/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public PageItems<MediaItem> listVideos(@RequestParam(name = "albumId") Long albumId,
			@RequestParam(name = "page") Integer page) throws UnauthenticatedUserException {
		EndUserEntity user = getUser();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.unsorted());
		return mediaAlbumService.listVideoMedia(user, albumId, pageable);
	}

	private static String generateContentRangeString(MediaHandler handler) {
		return "bytes " + handler.getStartRange() + "-" + handler.getEndRange() + "/" + handler.getFileSize();
	}

	public static Long[] parseRange(String rangeHeader) {
		if (rangeHeader == null) return null;
		Long startRange = 0L;
		Long endRange = Long.MAX_VALUE;
		if (rangeHeader != null && rangeHeader.length() > 6) {
			String[] range = rangeHeader.substring("bytes=".length()).split("-");
			startRange = Long.parseLong(range[0]);
			if (range.length > 1) endRange = Long.parseLong(range[1]);
		}
		return new Long[] { startRange, endRange };
	}

	@GetMapping(value = "/album/video", produces = "video/mp4")
	@ResponseBody
	public ResponseEntity<StreamingResponseBody> getVideo(@RequestParam("mediaId") Long mediaId,
			@RequestHeader(value = "Range", required = false) String rangeHeader)
			throws UnauthenticatedUserException, FileNotFoundException, IOException {
		if (mediaId < 1) return ResponseEntity.noContent().build();
		return generateMediaStreamingResponse(mediaId, rangeHeader);
	}

	@GetMapping(value = "/album/audio", produces = "video/mp4")
	public ResponseEntity<StreamingResponseBody> getAudio(@RequestParam("mediaid") long mediaId,
			@RequestHeader(value = "Range", required = false) String rangeHeader)
			throws UnauthenticatedUserException, FileNotFoundException, IOException {
		return generateMediaStreamingResponse(mediaId, null);
	}

	public ResponseEntity<StreamingResponseBody> generateMediaStreamingResponse(Long mediaId, String rangeHeader)
			throws UnauthenticatedUserException, FileNotFoundException, IOException {
		Long[] ranges = parseRange(rangeHeader);
		MediaHandler handler = videoService.fetchVideoStream(getUserDetails(), mediaId, ranges[0], ranges[1]);
		if (handler == null) return ResponseEntity.notFound().build();
		final HttpHeaders responseHeaders = new HttpHeaders();
		if (rangeHeader == null) {
			responseHeaders.add("Content-Type", handler.getMediaType());
			responseHeaders.add("Content-Length", handler.getContentLength().toString());
			return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(handler.getBody());
		} else {
			responseHeaders.add("Content-Type", handler.getMediaType());
			responseHeaders.add("Content-Length", handler.getContentLength().toString());
			responseHeaders.add("Accept-Ranges", "bytes");
			responseHeaders.add("Content-Range", generateContentRangeString(handler));
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(responseHeaders).body(handler.getBody());
		}
	}
}
