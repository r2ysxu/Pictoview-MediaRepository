import React from 'react';
import { useEffect } from 'react';
import './styles.css';

function ImageViewControls({imageCount, selectedIndex, onIndexChange, onChangeCoverImage}) {

  const onPrevIndex = () => {
    if (selectedIndex > 0) onIndexChange(selectedIndex - 1);
  }

  const onNextIndex = () => {
    if (selectedIndex < imageCount - 1) onIndexChange(selectedIndex + 1);
  }

  const onCancel = () => {
    onIndexChange(null);
  }

  const useKeyControls = (event) => {
    if (event.keyCode === 39 || event.keyCode === 32) { // Left Arrow or Space
      onNextIndex();
    } else if (event.keyCode === 37) { // Right Arrow
      onPrevIndex();
    } else if (event.keyCode === 38 || event.keyCode === 40 || event.keyCode === 27) { // Up or Down Arrow or Escape
      onIndexChange(null);
    }
    event.preventDefault();
    return false;
  };

  const preventKeyDown = (event) => {
    if (event.keyCode === 32 || event.keyCode === 37 || event.keyCode === 38 || event.keyCode === 39 || event.keyCode === 40) {
      event.preventDefault();
    }
  }

  useEffect( () => {
    document.addEventListener('keyup', useKeyControls);
    document.addEventListener('keydown', preventKeyDown);
    return () => {
      document.removeEventListener('keyup', useKeyControls);
      document.removeEventListener('keydown', preventKeyDown);
    }
  });

  return (
    <>
      <div className="image_view_update_controls_container">
        <img className="image_view_controls_icons image_view_controls_icons_medium"
          src="/assets/icons/journal-richtext.svg" alt=""
          onClick={(event) => {
              onChangeCoverImage();
              onIndexChange(null);
              event.preventDefault();
          }} />
      </div>
      <div className="image_view_controls_container">
        <img className={'image_vie_controls_icons image_view_controls_icons_small' + (selectedIndex > 0 ? ' ' : ' image_view_controls_hidden')}
          src="/assets/icons/arrow-left-circle.svg" alt=""
          onClick={onPrevIndex} />
        <img className="image_view_controls_icons image_view_controls_icons_large"
          src="/assets/icons/x-circle-fill.svg" alt=""
          onClick={onCancel}/>
        <img className={'image_view_controls_icons image_view_controls_icons_small' + (selectedIndex < imageCount - 1 ? ' ' : ' image_view_controls_hidden')}
          src="/assets/icons/arrow-right-circle.svg" alt=""
          onClick={onNextIndex} />
      </div>
    </>
  );
}

export default ImageViewControls;