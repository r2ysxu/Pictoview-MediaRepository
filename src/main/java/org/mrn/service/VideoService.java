package org.mrn.service;

import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.mrn.jpa.repo.VideoMediaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class VideoService {

	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private VideoMediaRepo videoMediaRepo;

	public Mono<Resource> fetchVideoStream(UserEntity owner, Long mediaId) {
		VideoMediaEntity videoMedia = videoMediaRepo.findByOwnerAndId(owner, mediaId);
		return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + videoMedia.getSource()));
	}
}
