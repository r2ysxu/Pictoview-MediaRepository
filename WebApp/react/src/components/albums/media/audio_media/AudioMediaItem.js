import React from 'react';
import './AudioMedia.css';

function AudioMedia({albumId, audio, trackNumber, onSelect}) {
    return (
        <div key={audio.id} className="audio_media_container" 
                onClick={onSelect}>
            <div className="audio_media_info_container">
                <div className="audio_media_track_number">
                    <h3>{trackNumber}</h3>
                </div>
                <div className="audio_media_album_cover_container">
                    <img className="audio_media_info_album_icon" src={'/album/image/cover?albumid=' + albumId} alt="" />
                    <img className="audio_media_play_button" src="/assets/icons/play-circle.svg" alt={'video ' + audio.id} title="Play" />
                </div>
                <div className="audio_media_info_container_song">
                    <h3>{audio.title}</h3>
                    <h4>{audio.artist}</h4>
                    <h5>{audio.genre}</h5>
                </div>
            </div>
        </div>
    );
}

export default AudioMedia;