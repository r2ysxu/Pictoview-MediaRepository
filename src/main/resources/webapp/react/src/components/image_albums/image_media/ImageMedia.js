import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import ScrollLoader from '../../scroll_loader/ScrollLoader';
import ImagePhoto from './image_photo/ImagePhoto';
import ImageView from './image_full_view/ImageView';
import { selectAlbums } from '../../../model/reducers/albumSlice';
import './ImageMedia.css';

async function listImages(page, albumId) {
    if (albumId < 0) return [];
    let searchParams = new URLSearchParams({page, albumId});
    return fetch('/album/image/photos/list?' + searchParams.toString()).then( response => response.json());
}

function ImageMedia({albumId}) {
    const dispatch = useDispatch();
    const { imageIds } = useSelector(selectAlbums);
    const [page, setPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [selectedImageIndex, setSelectedImageIndex] = useState(null);
    const [hasNext, setHasNext] = useState(true);

    const onLoadMore = (albumId) => {
        console.log("load more... " + page);
        if (hasNext) {
            setIsLoading(true);
            listImages(page, albumId).then( data => {
                console.log('albumId: ' + albumId + " at " + page);
                console.log('data', data);
                if (data.length === 0) {
                    //setHasNext(false);
                } else {
                    //setImageIds([...imageIds, ...data]);
                    setPage(page + 1);
                }
                setIsLoading(false); 
            });
        }
    }

    return (
        <div>
            {imageIds.length > 0 && <div><span className="image_media_prompt_text">Images</span></div>}
            <ScrollLoader loading={isLoading} onLoad={() => {}} height={50} hasNext={hasNext}>
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