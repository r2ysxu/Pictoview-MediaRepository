import React from 'react';
import { useEffect, useCallback } from 'react';
import './ImageViewControls.css';

function ImageViewControls({imageCount, selectedIndex, onIndexChange, onChangeCoverImage}) {

    const onNextIndex = () => {
        if (selectedIndex > 0) onIndexChange(selectedIndex - 1);
    }

    const onPrevIndex = () => {
        if (selectedIndex < imageCount - 1) onIndexChange(selectedIndex + 1);
    }

    const onCancel = () => {
        onIndexChange(null);
    }

    const useKeyControls = useCallback( event => {
        if (event.keyCode === 39) { // Left Arrow
            onPrevIndex();
        } else if (event.keyCode === 37) { // Right Arrow
            onNextIndex();
        } else if (event.keyCode === 39 || event.keyCode === 40) { // Up or Down Arrow
            onIndexChange(null);
        }
    }, [selectedIndex, onIndexChange]);

    useEffect( () => {
        document.addEventListener('keyup', useKeyControls);
        return () => document.removeEventListener('keyup', useKeyControls);
    }, [useKeyControls]);

    return (
        <>
            <div className="image_view_update_controls_container">
                <img className="image_view_controls_icons image_view_controls_icons_medium"
                    src="/assets/icons/journal-richtext.svg" alt=""
                    onClick={() => {
                        onChangeCoverImage();
                        onIndexChange(null);
                    }} />
            </div>
            <div className="image_view_controls_container">
                <img className={'image_vie_controls_icons image_view_controls_icons_small' + (selectedIndex > 0 ? ' ' : ' image_view_controls_hidden')}
                    src="/assets/icons/arrow-left-circle.svg" alt=""
                    onClick={onNextIndex} />
                <img className="image_view_controls_icons image_view_controls_icons_large"
                    src="/assets/icons/x-circle-fill.svg" alt=""
                    onClick={onCancel}/>
                <img className={'image_view_controls_icons image_view_controls_icons_small' + (selectedIndex < imageCount - 1 ? ' ' : ' image_view_controls_hidden')}
                    src="/assets/icons/arrow-right-circle.svg" alt=""
                    onClick={onPrevIndex} />
            </div>
        </>
    );
}

export default ImageViewControls;