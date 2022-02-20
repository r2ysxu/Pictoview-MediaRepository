import React from 'react';
import { useRef, useState, useEffect } from 'react';
import './AudioView.css';

function AudioView({audioItems, selectedIndex}) {
    const audioPlayerRef = useRef();
    const [audioDuration, setAudioDuration] = useState(0);

    const MAX_VALUE = 100;

    const onChangeTrackTime = (event) => {
        const audioPlayer = audioPlayerRef.current;
        const percentage = event.target.value;
        const newTime = Math.floor(percentage * audioPlayer.duration / 100);
        setAudioDuration(newTime);
        audioPlayer.currentTime = newTime;
    }

    useEffect( () => {
        const onChangeTrack = (audioPlayer) => {
            if (audioPlayer === null || audioPlayer === undefined) return;
            audioPlayer.load();
            audioPlayer.play();
        }

        const displayBuffered = (audio) => {
            const bufferedAmount = Math.floor(audio.buffered.end(audio.buffered.length - 1));
            console.log(bufferedAmount);
            audio.style.setProperty('--buffered-width', `${(bufferedAmount / 100) * 100}%`);
        }

        const displayTrack = (audioPlayer) => {
            if (audioPlayer === null || audioPlayer === undefined) return;
            audioPlayer.addEventListener('loadedmetadata', () => {
                displayBuffered(audioPlayer);
            });
            audioPlayer.addEventListener('timeupdate', () => {
                setAudioDuration(Math.floor(audioPlayer.currentTime * MAX_VALUE / audioPlayer.duration) || 0);
            });
        }

        const audioPlayer = audioPlayerRef.current;
        onChangeTrack(audioPlayer);
        displayTrack(audioPlayer);
    }, [audioPlayerRef, selectedIndex]);

    const onTogglePlay = () => {
        const audioPlayer = audioPlayerRef.current;
        if (audioPlayer.paused) audioPlayer.play();
        else audioPlayer.pause();
    }

    return ((selectedIndex != null && selectedIndex >= 0 && audioItems[selectedIndex]) ?
        <div className="audio_view_container">
            <div className="audio_view_audio_info">
            </div>
            <div className="audio_view_audio_content_container">
                <audio className="audio_view_audio_tag" autoPlay ref={audioPlayerRef}>
                    <source src={'/album/audio?mediaid=' + audioItems[selectedIndex].id} />
                </audio>
                    <input type="image" src="/assets/icons/play-circle.svg" onClick={onTogglePlay} />
                    <input type="range" onChange={onChangeTrackTime} max={MAX_VALUE} value={audioDuration} />
            </div>
        </div> : <div />
    );
}

export default AudioView;