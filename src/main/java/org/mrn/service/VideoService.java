package org.mrn.service;

import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.jpa.repo.VideoMediaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import reactor.core.publisher.Mono;

@Service
public class VideoService {
	@Value("${app.admin.main.cache.size}")
	private Integer maxCacheItemSize = 1;

	Cache<String, Resource> resourceCache = CacheBuilder
			.newBuilder()
			.maximumSize(maxCacheItemSize)
			.expireAfterAccess(4L, TimeUnit.HOURS)
			.build();

	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private VideoMediaRepo videoMediaRepo;

	public Mono<Resource> fetchVideoStream(UserDetails owner, Long mediaId) {
		Resource resource = resourceCache.getIfPresent(mediaId + "_" + owner.getUsername());
		if (resource == null) {
			final Resource unCachedResource = resourceLoader.getResource("file:" + findVideo(owner, mediaId).getSource());
			resourceCache.put(mediaId + "_" + owner.getUsername(), unCachedResource);
			return Mono.fromSupplier(() -> unCachedResource);
		} else {
			return Mono.fromSupplier(() -> resource);
		}
	}

	@Transactional
	public VideoMediaEntity findVideo(UserDetails owner, Long mediaId) {
		return videoMediaRepo.findByOwner_UsernameAndId(owner.getUsername(), mediaId);
	}
}
