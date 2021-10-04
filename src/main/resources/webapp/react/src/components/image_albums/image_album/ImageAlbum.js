import React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { loadAlbumTags } from '../../../model/reducers/albumSlice';
import ImageAlbumInfoTagView from './image_album_info/image_album_info_tags/image_album_info_tags_view/ImageAlbumInfoTagsView';
import './ImageAlbum.css';

function ImageAlbum({album, onChangeAlbum, isEditting, setEditting}) {
    const dispatch = useDispatch();
    const [showMoreInfo, setShowMoreInfo] = useState(false);
    const [isEditingTags, setEditingTags] = useState(false);

    const onShowMoreInfo = (albumId) => {
        if (showMoreInfo === false) {
            setShowMoreInfo(true);
            loadTags(albumId);
        } else {
            setShowMoreInfo(false);
        }
    }

    const loadTags = (albumId) => {
        dispatch(loadAlbumTags(albumId));
    }

    return (
        <div className="image_album_container">
            <div className="image_album_title">
                <h2>{album.name}</h2>
            </div>
            <div className="image_album_center_container">
                <div className="image_album_wrapper" onClick={() => onChangeAlbum(album.id)}>
                    <img className="image_album_image" src={'/album/image/cover?albumid=' + album.id} alt="" />
                </div>
                {showMoreInfo && <div className="image_album_info_container">
                    <div className="image_album_info_description_container">
                        <h3>Description</h3>
                    </div>
                    <ImageAlbumInfoTagView
                        albumId={album.id}
                        tags={album.tags}
                        isEditing={isEditingTags}
                        setEditing={setEditingTags}
                    />
                </div>}
            </div>
            <div className="image_album_banner" onClick={() => onShowMoreInfo(album.id)}>
                <h4>{album.publisher}</h4>
            </div>
        </div>
    );
}

export default ImageAlbum;