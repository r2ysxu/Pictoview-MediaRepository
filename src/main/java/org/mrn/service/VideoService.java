package org.mrn.service;

import javax.transaction.Transactional;

import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.jpa.repo.VideoMediaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class VideoService {

	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private VideoMediaRepo videoMediaRepo;

	public Mono<Resource> fetchVideoStream(UserDetails owner, Long mediaId) {
		return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + findVideo(owner, mediaId).getSource()));
	}

	@Transactional
	public VideoMediaEntity findVideo(UserDetails owner, Long mediaId) {
		return videoMediaRepo.findByOwner_UsernameAndId(owner.getUsername(), mediaId);
	}
}
