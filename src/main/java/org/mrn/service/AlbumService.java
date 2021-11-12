package org.mrn.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mrn.exceptions.AlbumNotFound;
import org.mrn.exceptions.InvalidMediaAlbumException;
import org.mrn.filemanager.AlbumDirectory;
import org.mrn.filemanager.AlbumFileUtils;
import org.mrn.filemanager.AlbumMediaFile;
import org.mrn.filemanager.AudioMediaFile;
import org.mrn.jpa.model.album.AlbumEntity;
import org.mrn.jpa.model.album.ImageMediaEntity;
import org.mrn.jpa.model.album.MediaEntity;
import org.mrn.jpa.model.album.VideoMediaEntity;
import org.mrn.jpa.model.album.music.AudioMediaEntity;
import org.mrn.jpa.model.album.music.MusicArtistEntity;
import org.mrn.jpa.model.album.music.MusicGenreEntity;
import org.mrn.jpa.model.tags.SearchQuery;
import org.mrn.jpa.model.user.EndUserEntity;
import org.mrn.jpa.model.user.UserEntity;
import org.mrn.jpa.repo.AlbumRepo;
import org.mrn.jpa.repo.AudioMediaRepo;
import org.mrn.jpa.repo.ImageMediaRepo;
import org.mrn.jpa.repo.MusicArtistRepo;
import org.mrn.jpa.repo.MusicGenreRepo;
import org.mrn.jpa.repo.VideoMediaRepo;
import org.mrn.query.model.Album;
import org.mrn.query.model.AudioMediaItem;
import org.mrn.query.model.MediaItem;
import org.mrn.query.model.PageItems;
import org.mrn.service.builder.AlbumBuilder;
import org.mrn.service.builder.ImageMediaItemBuilder;
import org.mrn.service.builder.AudioMediaItemBuilder;
import org.mrn.service.builder.PageItemBuilder;
import org.mrn.service.builder.VideoMediaItemBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

@Service
public class AlbumService {

	@Value("${app.admin.main.source}")
	private String adminSource;
	@Value("${app.admin.main.thumbnail.source}")
	private String adminThumbnailSource;
	@Value("${app.admin.main.cover.source}")
	private String adminCoverSource;

	private static final String ALBUMS_DIRECTORY = "albums/";
	private static final String ZIPPED_DIRECTORY = "zips/";

	@Autowired
	private AlbumRepo mediaAlbumRepo;
	@Autowired
	private ImageMediaRepo imageMediaRepo;
	@Autowired
	private VideoMediaRepo videoMediaRepo;
	@Autowired
	private AudioMediaRepo audioMediaRepo;
	@Autowired
	private MusicGenreRepo musicGenreRepo;
	@Autowired
	private MusicArtistRepo musicArtistRepo;

	public PageItems<Album> searchMediaAlbum(UserEntity user, SearchQuery searchQuery, Pageable pageable) {
		Page<AlbumEntity> albums = mediaAlbumRepo.searchAlbums(user, searchQuery, pageable);
		return new PageItemBuilder<Album, AlbumEntity>().build(albums, new AlbumBuilder());
	}

	public PageItems<Album> listMediaAlbums(UserEntity user, Long parentId, Pageable pageable) {
		Page<AlbumEntity> albums;
		if (parentId == null || parentId < 1) albums = mediaAlbumRepo.findAllByOwner(user, pageable);
		else albums = mediaAlbumRepo.findAllByOwnerAndParent_Id(user, parentId, pageable);
		return new PageItemBuilder<Album, AlbumEntity>().build(albums, new AlbumBuilder());
	}

	public PageItems<MediaItem> listImageMedia(UserEntity user, Long albumId, Pageable pageable) {
		Page<ImageMediaEntity> imageMedia = imageMediaRepo.findAllByOwnerAndAlbum_Id(user, albumId, pageable);
		return new PageItemBuilder<MediaItem, ImageMediaEntity>().build(imageMedia, new ImageMediaItemBuilder());
	}

	public PageItems<MediaItem> listVideoMedia(UserEntity user, Long albumId, Pageable pageable) {
		Page<VideoMediaEntity> videoMedia = videoMediaRepo.findAllByOwnerAndAlbum_Id(user, albumId, pageable);
		return new PageItemBuilder<MediaItem, VideoMediaEntity>().build(videoMedia, new VideoMediaItemBuilder());
	}

	public PageItems<AudioMediaItem> listAudioMedia(UserEntity user, Long albumId, Pageable pageable) {
		Page<AudioMediaEntity> musicMedia = audioMediaRepo.findAllByOwnerAndAlbum_Id(user, albumId, pageable);
		return new PageItemBuilder<AudioMediaItem, AudioMediaEntity>().build(musicMedia, new AudioMediaItemBuilder());
	}

	public AlbumDirectory uploadAlbumMedia(EndUserEntity user, Long albumId, MultipartFile file, Boolean loadMetadata)
			throws IllegalStateException, IOException {
		final String albumFolderPath = adminSource + ALBUMS_DIRECTORY + file.getOriginalFilename().replace(".zip", "");
		File zippedFile = new File(adminSource + ZIPPED_DIRECTORY + file.getOriginalFilename());
		file.transferTo(zippedFile);
		AlbumFileUtils.unzipFolder(zippedFile.getAbsolutePath(), albumFolderPath);
		return AlbumFileUtils.generateAlbumFolder(albumFolderPath, loadMetadata);
	}

	public Album createAlbum(EndUserEntity user, String name, String subtitle, String description) {
		AlbumEntity albumEntity = mediaAlbumRepo.save(new AlbumEntity(user, name, subtitle, description));
		return new AlbumBuilder().build(albumEntity);
	}

	public Album updateAlbum(EndUserEntity user, Long albumId, String name, String subtitle, String description)
			throws AlbumNotFound {
		AlbumEntity albumEntity = mediaAlbumRepo.findById(albumId).get();
		if (albumEntity != null) {
			if (name != null) albumEntity.setName(name);
			if (subtitle != null) albumEntity.setSubtitle(subtitle);
			if (description != null) albumEntity.setDescription(description);
			albumEntity = mediaAlbumRepo.save(albumEntity);
			return new AlbumBuilder().build(albumEntity);
		} else {
			throw new AlbumNotFound(user, albumId);
		}
	}

	private String generateThumbnailPath(AlbumEntity album, ImageMediaEntity image) {
		return adminThumbnailSource + album.getId() + "/" + image.getId() + image.getTypeExtension();
	}

	private void populateMusicGenreByName(Map<String, List<AudioMediaEntity>> musicEntities) {
		List<MusicGenreEntity> musicGenreEntities = musicGenreRepo.findByNameIn(new ArrayList<>(musicEntities.keySet()));
		Map<String, MusicGenreEntity> musicGenreMap = musicGenreEntities.stream()
				.collect(Collectors.toMap(MusicGenreEntity::getName, Function.identity()));
		musicEntities.forEach( (genreName, sameGenreSong) -> {
			sameGenreSong.stream().forEach(song -> {
				MusicGenreEntity musicGenre = musicGenreMap.get(genreName);
				if (musicGenre == null) musicGenre = musicGenreRepo.save(new MusicGenreEntity().setName(genreName));
				song.setGenre(musicGenre);
			});
		});
	}

	private void populateMusicArtistByName(Map<String, List<AudioMediaEntity>> musicEntities) {
		List<MusicArtistEntity> musicArtistEntities = musicArtistRepo.findByNameIn(new ArrayList<>(musicEntities.keySet()));
		Map<String, MusicArtistEntity> musicArtistMap = musicArtistEntities.stream()
				.collect(Collectors.toMap(MusicArtistEntity::getName, Function.identity()));
		musicEntities.forEach( (artistName, sameArtistSong) -> {
			sameArtistSong.stream().forEach( song -> {
				MusicArtistEntity musicArtist = musicArtistMap.get(artistName);
				if (musicArtist == null) musicArtist = musicArtistRepo.save(new MusicArtistEntity().setName(artistName));
				song.setArtist(musicArtist);
			});
		});
	}

	private void createImageThumbnails(AlbumEntity albumEntity, List<ImageMediaEntity> imageMedia) throws IOException {
		for (ImageMediaEntity image : imageMedia) {
			image.setThumbnailSource(generateThumbnailPath(albumEntity, image));
			AlbumFileUtils.createPhotoThumbnail(image.getSource(), image.getThumbnailSource());
		}
	}

	public List<MediaEntity> createMediaFromFile(EndUserEntity user, Long albumId, AlbumDirectory albumDirectory)
			throws AlbumNotFound, IOException, UnsupportedTagException, InvalidDataException {
		AlbumEntity albumEntity = mediaAlbumRepo.findById(albumId).orElseThrow(() -> new AlbumNotFound(user, albumId));
		List<ImageMediaEntity> imageMedia = new ArrayList<>();
		List<VideoMediaEntity> videoMedia = new ArrayList<>();
		List<AudioMediaEntity> musicMedia = new ArrayList<>();

		Map<String, List<AudioMediaEntity>> musicGenres = new HashMap<>();
		Map<String, List<AudioMediaEntity>> musicArtists = new HashMap<>();

		for (AlbumMediaFile file : albumDirectory.getMediaFiles()) {
			switch (file.getType()) {
			case IMAGE:
				ImageMediaEntity image = new ImageMediaEntity(user, file.getAbsolutePath(), file.getName(), file.getMediaType(),
						albumEntity);
				imageMedia.add(image);
				break;
			case VIDEO:
				videoMedia.add(
						new VideoMediaEntity(user, file.getAbsolutePath(), file.getName(), file.getMediaType(), albumEntity));
				break;
			case AUDIO:
				AudioMediaFile audioFile = AlbumFileUtils.parseAudioFile(file.getAbsolutePath());
				AudioMediaEntity musicEntity = new AudioMediaEntity(user, file.getAbsolutePath(), file.getName(), file.getMediaType(), albumEntity);
				musicEntity.setTrackNumber(audioFile.getTrackNumber());
				if (audioFile != null) {
					musicEntity.setTitle(audioFile.getTitle());
					if (!musicGenres.containsKey(audioFile.getGenre())) {
						musicGenres.put(audioFile.getGenre(), new ArrayList<>());
					}
					if (!musicArtists.containsKey(audioFile.getArtist())) {
						musicArtists.put(audioFile.getArtist(), new ArrayList<>());
					}
					musicGenres.get(audioFile.getGenre()).add(musicEntity);
					musicArtists.get(audioFile.getArtist()).add(musicEntity);
				}
				musicMedia.add(musicEntity);
				break;
			default:
			}
		}

		populateMusicArtistByName(musicArtists);
		populateMusicGenreByName(musicGenres);

		List<MediaEntity> savedMedia = new ArrayList<>(albumDirectory.getMediaFiles().size());
		imageMediaRepo.saveAll(imageMedia).forEach(savedMedia::add);
		videoMediaRepo.saveAll(videoMedia).forEach(savedMedia::add);
		audioMediaRepo.saveAll(musicMedia).forEach(savedMedia::add);
		createImageThumbnails(albumEntity, imageMedia);
		return savedMedia;
	}

	public AlbumEntity setCoverPhotoByName(EndUserEntity user, Long albumId, String name) throws IOException, AlbumNotFound {
		AlbumEntity albumEntity = mediaAlbumRepo.findById(albumId).orElseThrow(() -> new AlbumNotFound(user, albumId));
		ImageMediaEntity imageMedia = imageMediaRepo.findFirstByAlbumAndName(albumEntity, name);
		if (imageMedia == null) return null;
		AlbumFileUtils.createCoverPhotoFile(imageMedia.getSource(),
				ImageService.generateCoverPhotoPath(adminCoverSource, albumEntity, imageMedia));
		albumEntity.setCoverPhoto(imageMedia);
		return mediaAlbumRepo.save(albumEntity);
	}

	public Album setCoverPhotoById(EndUserEntity user, Long albumId, Long imageId)
			throws AlbumNotFound, InvalidMediaAlbumException, IOException {
		AlbumEntity albumEntity = mediaAlbumRepo.findById(albumId).orElseThrow(() -> new AlbumNotFound(user, albumId));
		ImageMediaEntity imageMedia = imageMediaRepo.findByOwnerAndId(user, imageId);
		if (imageMedia == null || imageMedia.getAlbum().getId() != albumEntity.getId())
			throw new InvalidMediaAlbumException(albumId, imageId);
		AlbumFileUtils.createCoverPhotoFile(imageMedia.getSource(),
				ImageService.generateCoverPhotoPath(adminCoverSource, albumEntity, imageMedia));
		albumEntity.setCoverPhoto(imageMedia);
		return new AlbumBuilder().build(mediaAlbumRepo.save(albumEntity));
	}

	public AlbumEntity setFirstAlbumCoverPhoto(EndUserEntity user, Long albumId) throws IOException, AlbumNotFound {
		AlbumEntity albumEntity = mediaAlbumRepo.findById(albumId).orElseThrow(() -> new AlbumNotFound(user, albumId));
		ImageMediaEntity imageMedia = imageMediaRepo.findFirstByAlbumOrderByNameAsc(albumEntity);
		if (imageMedia == null) return null;
		AlbumFileUtils.createCoverPhotoFile(imageMedia.getSource(),
				ImageService.generateCoverPhotoPath(adminCoverSource, albumEntity, imageMedia));
		albumEntity.setCoverPhoto(imageMedia);
		return mediaAlbumRepo.save(albumEntity);
	}
}
