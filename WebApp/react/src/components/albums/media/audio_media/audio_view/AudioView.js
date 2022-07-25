import React from 'react';
import { useRef, useState, useEffect } from 'react';
import './AudioView.css';

function AudioView({audioItems, selectedIndex}) {
    const audioPlayerRef = useRef();
    const audioSliderRef = useRef();
    const [audioDuration, setAudioDuration] = useState(0);
    const [audioCurrentTime, setAudioCurrentTime] = useState(0);
    const [audioTotalTime, setAudioTotalTime] = useState(0);
    const [isPaused, setPaused] = useState(true);

    const MAX_VALUE = 100;

    const onChangeTrackTime = (event) => {
        const audioPlayer = audioPlayerRef.current;
        const percentage = event.target.value;
        audioPlayer.currentTime = Math.floor(percentage * audioPlayer.duration / 100);
    }

    useEffect( () => {
        const onChangeTrack = (audioPlayer) => {
            if (audioPlayer === null || audioPlayer === undefined) return;
            audioPlayer.load();
            audioPlayer.play();
            setPaused(false);
        }

        const displayBuffered = (audioPlayer, audioSlider) => {
            const bufferedAmount = Math.floor(audioPlayer.buffered.end(audioPlayer.buffered.length - 1));
            console.log(bufferedAmount);
            audioSlider.style.setProperty('--buffered-width', `${(bufferedAmount / 100) * 100}%`);
        }

        const displayTrack = (audioPlayer, audioSlider) => {
            if (audioPlayer === null || audioPlayer === undefined) return;
            audioPlayer.addEventListener('loadedmetadata', () => {
                displayBuffered(audioPlayer, audioSlider);
            });
            audioPlayer.addEventListener('timeupdate', () => {
                const percentage = Math.floor(audioPlayer.currentTime * MAX_VALUE / audioPlayer.duration) || 0;
                setAudioDuration(percentage);
                setAudioTotalTime(audioPlayer.duration);
                setAudioCurrentTime(audioPlayer.currentTime);
                audioSlider.style.setProperty('--seek-before-width', `${percentage}%`);
            });
        }

        const audioPlayer = audioPlayerRef.current;
        const audioSlider = audioSliderRef.current;
        onChangeTrack(audioPlayer);
        displayTrack(audioPlayer, audioSlider);
    }, [audioPlayerRef, selectedIndex]);

    const onTogglePlay = () => {
        const audioPlayer = audioPlayerRef.current;
        if (audioPlayer.paused) {
            audioPlayer.play();
            setPaused(false);
        } else {
            audioPlayer.pause();
            setPaused(true);
        }
    }

    return ((selectedIndex != null && selectedIndex >= 0 && audioItems[selectedIndex]) ?
        <div className="audio_view_container">
            <div className="audio_view_audio_info">
            </div>
            <div className="audio_view_audio_content_container">
                <div className="audio_view_content_slider">
                    <audio className="audio_view_audio_tag" autoPlay ref={audioPlayerRef}>
                        <source src={'/album/audio?mediaid=' + audioItems[selectedIndex].id} />
                    </audio>
                    <input type="range" onChange={onChangeTrackTime} max={MAX_VALUE} value={audioDuration} ref={audioSliderRef} />
                </div>
                <div className="audio_view_audio_content_left_info">
                    <span>{audioCurrentTime}</span>
                </div>
                <div className="audio_view_audio_content_right_info">
                    <span>{audioTotalTime}</span>
                </div>
                <input className="audio_view_content_play_button" type="image" alt="play" src={isPaused ? "/assets/icons/play-circle.svg" : "/assets/icons/pause-circle.svg"} onClick={onTogglePlay} />
            </div>
        </div> : <div />
    );
}

export default AudioView;