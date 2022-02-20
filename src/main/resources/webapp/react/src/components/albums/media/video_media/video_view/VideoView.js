import React from 'react';
import { useEffect, useRef } from 'react';
import './VideoView.css';

function VideoView({videoItems, selectedIndex, onSelectIndex}) {
    const videoRef = useRef();

    const onCloseModal = () => {
        onSelectIndex(null);
    }

    const onSkipTime = (seconds) => {
        videoRef.current.currentTime += seconds;
    }

    const useKeyControls = (event) => {
        if (selectedIndex === null) return;
        if (event.keyCode === 67) { // C
            onSkipTime(60);
        } else if (event.keyCode === 99) { // c
            onSkipTime(10);
        } else if (event.keyCode === 88) { // X
            onSkipTime(-60);
        } else if (event.keyCode === 120) { // x
            onSkipTime(-10);
        }
        event.preventDefault();
    };

    useEffect( () => {
        document.addEventListener('keypress', useKeyControls);
        return () => document.removeEventListener('keypress', useKeyControls);
    });

    return ((selectedIndex != null && selectedIndex >= 0 && videoItems[selectedIndex]) ?
        <div className="video_view_container">
            <div className="video_view_container_modal">
                <div className="video_view_container_modal_background" onClick={onCloseModal} />
                <video className="video_view_video_content" controls autoPlay ref={videoRef}>
                    <source src={'/album/video?mediaid=' + videoItems[selectedIndex].id} />
                </video>
            </div>
        </div> : <div />
    );
}

export default VideoView;