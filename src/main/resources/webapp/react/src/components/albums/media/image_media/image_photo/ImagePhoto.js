import React from 'react';
import { useState } from 'react';
import './ImagePhoto.css';

function ImagePhoto({index, image, onSelectIndex}) {
    return (
        <div className="image_photo_container" onClick={() => {onSelectIndex(index)} }>
            <img src={'/album/image/thumbnail?mediaid=' + image} alt={image} />
        </div>
    );
}

export default ImagePhoto;