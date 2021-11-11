package org.mrn.service;

import org.mrn.jpa.model.album.music.AudioMediaEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.mrn.jpa.repo.AudioMediaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class AudioService {

	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private AudioMediaRepo musicMediaRepo;

	public Mono<Resource> fetchAudioStream(UserEntity owner, Long mediaId) {
		AudioMediaEntity musicMedia = musicMediaRepo.findByOwnerAndId(owner, mediaId);
		return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + musicMedia.getSource()));
	}
}
