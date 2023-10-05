package org.mrn.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.jpa.repo.VideoMediaRepo;
import org.mrn.query.model.MediaHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Service
public class VideoService {

	@Value("${app.admin.main.cache.size}")
	private Integer maxCacheItemSize = 5;

	Cache<String, String> sourceCache = CacheBuilder.newBuilder().maximumSize(maxCacheItemSize)
			.expireAfterAccess(10L, TimeUnit.MINUTES).build();

	@Autowired
	private VideoMediaRepo videoMediaRepo;

	public MediaHandler fetchVideoStream(UserDetails owner, Long mediaId, Long startRange, Long endRange)
			throws FileNotFoundException, IOException {
		String source = sourceCache.getIfPresent(owner.getUsername() + "-" + mediaId);
		if (source == null) {
			VideoMediaEntity entity = findVideo(owner, mediaId);
			if (entity == null) return null;
			sourceCache.put(owner.getUsername() + "-" + mediaId, entity.getSource());
		}
		return new MediaHandler(new RandomAccessFile(source, "r"), startRange, endRange);
	}

	private VideoMediaEntity findVideo(UserDetails owner, Long mediaId) {
		return videoMediaRepo.findByOwner_UsernameAndId(owner.getUsername(), mediaId);
	}
}

/** Note: ResourceLoader: spaces between square brackets does not encode properly. eg. [word1 word2] **/