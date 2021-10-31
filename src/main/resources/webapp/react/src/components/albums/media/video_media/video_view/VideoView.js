import React from 'react';
import { useState, useEffect } from 'react';
import './VideoView.css';

function VideoView({videoItems, selectedIndex, onSelectIndex}) {

    const onCloseModal = () => {
        onSelectIndex(null);
    }

    return ((selectedIndex != null && selectedIndex >= 0) ?
        <div className="video_view_container">
            <div className="video_view_container_modal" onClick={onCloseModal}>
                <div className="video_view_video_container">
                    <video className="video_view_video_content" controls>
                        <source src={'/album/video?mediaid=' + videoItems[selectedIndex].id} />
                    </video>
                </div>
            </div>

        </div> : <div />
    );
}

export default VideoView;