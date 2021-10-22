import React from 'react';
import { useState, useEffect } from 'react';

function VideoView({videoIds, selectedIndex, onSelectIndex}) {

    return ((selectedIndex != null && selectedIndex >= 0) ?
        <div>
            <video controls>
                <source src={'/album/video?mediaid=' + videoIds[selectedIndex]} />
            </video>
        </div> : <div />
    );
}

export default VideoView;