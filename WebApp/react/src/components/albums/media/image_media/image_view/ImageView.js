import React from 'react';
import { useDispatch } from 'react-redux';
import { updateCoverImage } from '../../../../../model/reducers/albumSlice';
import ImageViewControls from './image_view_controls/ImageViewControls';
import './ImageView.css';

function ImageView({albumId, imageItems, selectedIndex, onSelectIndex}) {
    const dispatch = useDispatch();
    const onCloseModal = () => {
        onSelectIndex(null);
    }

    const onChangeCoverImage = () => {
        if (imageItems[selectedIndex] !== null) {
            dispatch(updateCoverImage({
                albumId,
                imageId: imageItems[selectedIndex].id
            }));
        }
    }

    return ((selectedIndex != null && selectedIndex >= 0 && imageItems[selectedIndex]) ?
        <div className="image_view_container">
            <div className="image_view_content">
                <div className="image_view_container_modal" onClick={onCloseModal}></div>
                <img className="image_view_image" src={'/album/image/full?mediaid=' + imageItems[selectedIndex].id} alt={imageItems[selectedIndex].name} title={imageItems[selectedIndex].name} />
                <ImageViewControls
                    imageCount={imageItems.length}
                    selectedIndex={selectedIndex}
                    onIndexChange={onSelectIndex}
                    onChangeCoverImage={onChangeCoverImage} />
            </div>
        </div> : <div />
    );
}

export default ImageView;