import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import ScrollLoader from '../../../scroll_loader/ScrollLoader';
import ImagePhoto from './image_photo/ImagePhoto';
import ImageView from './image_full_view/ImageView';
import { selectAlbums, loadMoreImages } from '../../../../model/reducers/albumSlice';
import './ImageMedia.css';

function ImageMedia({albumId}) {
    const dispatch = useDispatch();
    const { imageIds } = useSelector(selectAlbums);
    const isLoading = useSelector((state) => state.album.isLoading);
    const hasMore = useSelector((state) => state.album.imageIdsHasMore);
    const [page, setPage] = useState(0);
    const [selectedImageIndex, setSelectedImageIndex] = useState(null);

    const loadMore = () => {
        if (!isLoading) dispatch(loadMoreImages());
    }

    return (
        <div>
            <ScrollLoader isLoading={isLoading} loadMore={loadMore} height={50} hasMore={hasMore}>
                <div className="image_media_list">
                    {imageIds.map( (imageId, index) =>
                        <ImagePhoto 
                            key={index}
                            index={index}
                            image={imageId}
                            onSelectIndex={setSelectedImageIndex} /> )}
                </div>
            </ScrollLoader>
            <ImageView
                imageIds={imageIds}
                selectedIndex={selectedImageIndex}
                onSelectIndex={setSelectedImageIndex} />
        </div>
    );
}

export default ImageMedia;