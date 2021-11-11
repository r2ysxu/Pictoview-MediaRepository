import React from 'react';
import { useState, useEffect } from 'react';
import './AudioView.css';

function AudioView({audioItems, selectedIndex, onSelectIndex}) {

    return ((selectedIndex != null && selectedIndex >= 0 && audioItems[selectedIndex]) ?
        <div className="audio_view_container">
            <div className="audio_view_audio_info">
            </div>
            <div className="audio_view_audio_content_container">
                <audio className="audio_view_audio_content" controls>
                    <source src={'/album/audio?mediaid=' + audioItems[selectedIndex].id} />
                </audio>
            </div>
        </div> : <div />
    );
}

export default AudioView;