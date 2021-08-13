import React from 'react';
import ImageViewControls from './image_view_controls/ImageViewControls';
import './ImageView.css';

function ImageView({imageIds, selectedIndex, onSelectIndex}) {

	const onCloseModal = () => {
		onSelectIndex(null);
	}

	return ((selectedIndex != null && selectedIndex >= 0) ?
		<div className="image_view_container">
			<div className="image_view_image_container">
	            <div className="image_view_container_modal" onClick={onCloseModal}></div>
				<img className="image_view_image" src={'/album/image/full?mediaid=' + imageIds[selectedIndex]} />
	        </div>
	        <ImageViewControls imageCount={imageIds.length} selectedIndex={selectedIndex} onIndexChange={onSelectIndex} />
		</div> : <div />
	);
}

export default ImageView;