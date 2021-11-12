import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadMoreImages } from '../../../../model/reducers/albumSlice';
import ScrollLoader from '../../../widgets/scroll_loader/ScrollLoader';
import ImagePhoto from './image_photo/ImagePhoto';
import ImageView from './image_view/ImageView';
import './ImageMedia.css';

function ImageMedia({albumId}) {
    const dispatch = useDispatch();
    const { images } = useSelector(selectAlbums);
    const isLoading = useSelector((state) => state.album.isLoading);
    const [selectedImageIndex, setSelectedImageIndex] = useState(null);

    const loadMore = () => {
        if (!isLoading) dispatch(loadMoreImages({albumId, page: images.pageInfo.page + 1}));
    }

    return (
        <div>
            <ScrollLoader isLoading={isLoading} loadMore={loadMore} height={50} hasMore={images.pageInfo.hasNext}>
                <div className="album_media_list">
                    {images.items.map( (image, index) =>
                        <ImagePhoto 
                            key={index}
                            index={index}
                            image={image}
                            onSelectIndex={setSelectedImageIndex} /> )}
                </div>
            </ScrollLoader>
            <ImageView
                albumId={albumId}
                imageItems={images.items}
                selectedIndex={selectedImageIndex}
                onSelectIndex={setSelectedImageIndex} />
        </div>
    );
}

export default ImageMedia;