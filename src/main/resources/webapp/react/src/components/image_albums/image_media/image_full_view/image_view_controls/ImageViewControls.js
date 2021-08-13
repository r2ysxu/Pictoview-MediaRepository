import React from 'react';
import { useState } from 'react';
import './ImageViewControls.css';

function ImageViewControls({imageCount, selectedIndex, onIndexChange}) {
    return (
        <div className="image_view_controls_container">
            <img className={'image_vie_controls_icons image_view_controls_icons_small' + (selectedIndex > 0 ? ' ' : ' image_view_controls_hidden')}
                src="/assets/icons/arrow-left-circle.svg" alt=""
                onClick={() => {
                    if (selectedIndex > 0) onIndexChange(selectedIndex - 1);
                }} />
            <img className="image_view_controls_icons image_view_controls_icons_large"
                src="/assets/icons/x-circle-fill.svg" alt=""
                onClick={() => onIndexChange(null)}/>
            <img className={'image_view_controls_icons  image_view_controls_icons_small' + (selectedIndex < imageCount - 1 ? ' ' : ' image_view_controls_hidden')}
                src="/assets/icons/arrow-right-circle.svg" alt=""
                onClick={() => {
                    if (selectedIndex < imageCount - 1) onIndexChange(selectedIndex + 1);
                }} />
        </div>
    );
}

export default ImageViewControls;