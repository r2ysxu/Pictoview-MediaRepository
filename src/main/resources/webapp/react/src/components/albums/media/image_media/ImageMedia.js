import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import ScrollLoader from '../../../scroll_loader/ScrollLoader';
import ImagePhoto from './image_photo/ImagePhoto';
import ImageView from './image_view/ImageView';
import { selectAlbums, loadMoreImages } from '../../../../model/reducers/albumSlice';
import './ImageMedia.css';

function ImageMedia({albumId}) {
    const dispatch = useDispatch();
    const { images } = useSelector(selectAlbums);
    const isLoading = useSelector((state) => state.album.isLoading);
    const [selectedImageIndex, setSelectedImageIndex] = useState(null);

    const loadMore = () => {
        if (!isLoading) dispatch(loadMoreImages());
    }

    return (
        <div>
            <ScrollLoader isLoading={isLoading} loadMore={loadMore} height={50} hasMore={images.pageInfo.hasNext}>
                <div className="image_media_list">
                    {images.items.map( (image, index) =>
                        <ImagePhoto 
                            key={index}
                            index={index}
                            image={image}
                            onSelectIndex={setSelectedImageIndex} /> )}
                </div>
            </ScrollLoader>
            <ImageView
                imageItems={images.items}
                selectedIndex={selectedImageIndex}
                onSelectIndex={setSelectedImageIndex} />
        </div>
    );
}

export default ImageMedia;