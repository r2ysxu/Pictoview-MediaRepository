import React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { loadAlbumTags } from '../../../model/reducers/albumSlice';
import './ImageAlbum.css';

function ImageAlbum({album, onChangeAlbum}) {
    const dispatch = useDispatch();
    const [showMoreInfo, setShowMoreInfo] = useState(true);

    const onShowMoreInfo = (albumId) => {
        setShowMoreInfo(true);
        loadTags(albumId);
    }

    const loadTags = (albumId) => {
        dispatch(loadAlbumTags(albumId));
    }

    return (
        <div className="image_album_container">
            <div className="image_album_title">
                <h3>{album.name}</h3>
            </div>
            <div className="image_album_wrapper" onClick={() => onChangeAlbum(album.id)}>
                <img className="image_album_image" src={'/album/image/cover?albumid=' + album.id} alt="" />
            </div>
            <div className="image_album_banner" onClick={() => onShowMoreInfo(album.id)}>
                <h4>{album.publisher}</h4>
            </div>
            {showMoreInfo && <div className="image_album_info_container">
                <div className="image_album_info_tag_container">
                    {album.tags && album.tags.categories.map( category => {
                        const tags = category.tags.map( (tag, index) => <span key={category.id + '-' + index} >{tag}</span> );
                        return (
                            <div key={category.id} >
                                <span>{category.name}</span>
                                {tags}
                            </div>
                        );
                    })}
                </div>
            </div>}
        </div>
    );
}

export default ImageAlbum;