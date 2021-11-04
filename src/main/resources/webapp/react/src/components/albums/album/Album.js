import React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { loadAlbumTags } from '../../../model/reducers/albumSlice';
import AlbumInfoTagView from './album_info/album_info_tags/album_info_tags_view/AlbumInfoTagsView';
import './Album.css';

function Album({album, onChangeAlbum, isEditting, setEditting}) {
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
        <div className="album_container">
            <div className="album_title">
                <h2>{album.name}</h2>
            </div>
            <div className="album_center_container">
                <div className="album_wrapper" onClick={() => onChangeAlbum({id: album.id, name: album.name})}>
                    {album.id === 0 ? 
                        <imge className="album_image" src="/assets/icons/image.svg" /> :
                        <img className="album_image" src={'/album/image/cover?albumid=' + album.id} alt="" />}
                </div>
                {showMoreInfo && <div className="album_info_container">
                    <div className="album_info_description_container">
                        <h3>Description</h3>
                    </div>
                    <AlbumInfoTagView
                        albumId={album.id}
                        tags={album.tags}
                        isEditing={isEditingTags}
                        setEditing={setEditingTags}
                    />
                </div>}
            </div>
            <div className="album_banner" onClick={() => onShowMoreInfo(album.id)}>
                <h4>{album.publisher}</h4>
            </div>
        </div>
    );
}

export default Album;