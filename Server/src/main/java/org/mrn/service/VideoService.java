package org.mrn.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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
	private Integer maxCacheItemSize = 5;

	Cache<String, Resource> resourceCache = CacheBuilder.newBuilder().maximumSize(maxCacheItemSize)
			.expireAfterAccess(5L, TimeUnit.MINUTES)
			.build();

	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private VideoMediaRepo videoMediaRepo;

	public Mono<Resource> fetchVideoStream(UserDetails owner, Long mediaId) {
		final Resource resource = resourceCache.getIfPresent(owner.getUsername() + mediaId);
		Mono<Resource> monoResource;
		if (resource == null) {
			Resource newResource = resourceLoader.getResource("file:" + findVideo(owner, mediaId).getSource());
			resourceCache.put(owner.getUsername() + mediaId, newResource);
			monoResource = Mono.fromSupplier(() -> newResource);
		} else {
			monoResource = Mono.fromSupplier(() -> resource);
		}
		return monoResource.timeout(Duration.ofMinutes(1L));
	}

	public VideoMediaEntity findVideo(UserDetails owner, Long mediaId) {
		return videoMediaRepo.findByOwner_UsernameAndId(owner.getUsername(), mediaId);
	}
}

/** Note: ResourceLoader: spaces between square brackets does not encode properly. eg. [word1 word2] **/