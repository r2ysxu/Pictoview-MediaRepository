import React from 'react';
import { useState, useEffect } from 'react';

function VideoView({videoItems, selectedIndex, onSelectIndex}) {

    return ((selectedIndex != null && selectedIndex >= 0) ?
        <div>
            <video controls>
                <source src={'/album/video?mediaid=' + videoItems[selectedIndex].id} />
            </video>
        </div> : <div />
    );
}

export default VideoView;