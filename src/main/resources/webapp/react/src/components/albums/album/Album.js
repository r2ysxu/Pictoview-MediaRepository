import React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { loadAlbumTags, updateAlbum } from '../../../model/reducers/albumSlice';
import AlbumInfoTagView from './album_info/album_info_tags/album_info_tags_view/AlbumInfoTagsView';
import './Album.css';

function Album({album, onChangeAlbum, isEditting, setEditting}) {
    const dispatch = useDispatch();
    const [showMoreInfo, setShowMoreInfo] = useState(false);
    const [isEditing, setEditing] = useState(false);
    const [isEditRating, setEditRating] = useState(false);
    const [rating, setRating] = useState(album.rating);
    const [currentAlbum, setCurrentAlbum] = useState(album);

    const onShowMoreInfo = (albumId) => {
        if (showMoreInfo === false) {
            setShowMoreInfo(true);
            loadTags(albumId);
        } else if (isEditing === false) {
            setShowMoreInfo(false);
        }
    }

    const onEditRating = (event) => {
        setEditRating(!isEditRating);
        event.preventDefault();
        return false;
    }

    const onChangeRating = (event) => {
        setRating(event.target.value);
    }

    const onChangeRatingDone = (event) => {
        dispatch(updateAlbum({
            id: currentAlbum.id,
            rating
        }));
        setEditRating(false);
    }

    const loadTags = (albumId) => {
        dispatch(loadAlbumTags(albumId));
    }

    const onUpdateAlbum = () => {
        dispatch(updateAlbum({
            id: currentAlbum.id,
            name: currentAlbum.name,
            publisher: currentAlbum.publisher,
            description: currentAlbum.description,
        }));
        setEditing(false);
    }

    let albumNameSizeClass = "";
    if (currentAlbum.name.length > 80) albumNameSizeClass = "album_title_40";
    else if (currentAlbum.name.length > 20) albumNameSizeClass = "album_title_20";

    return (
        <div className="album_container">
            <div className="album_title">
                {!isEditing && <h2 className={albumNameSizeClass}>{currentAlbum.name}</h2>}
                {isEditing && <input className="album_text_field album_name_text_field" type="text" placeholder="Name" value={currentAlbum.name} onChange={ (event) => setCurrentAlbum({...currentAlbum, name: event.target.value}) } />}
            </div>
            <div className="album_center_container">
                <div className="album_wrapper" onClick={() => onChangeAlbum(album.id)}>
                    {album.id === 0 ? 
                        <imge className="album_image" src="/assets/icons/image.svg" /> :
                        <img className="album_image" src={'/album/image/cover?albumid=' + album.id} alt="" />}
                </div>
                {showMoreInfo && <div className="album_info_container">
                    {!isEditing && <img className="album_info_edit_icon"
                        src="/assets/icons/pencil.svg" alt=""
                        onClick={() => {
                            setEditing(!isEditing);
                        }}
                    />}
                    {isEditing && <div className="album_info_edit_buttons">
                        <img className="album_info_edit_buttons_save" src="/assets/icons/check.svg" alt="" onClick={onUpdateAlbum} />
                        <img className="album_info_edit_buttons_cancel" src="/assets/icons/x.svg" alt="" onClick={onUpdateAlbum} />
                    </div>}
                    <div className="album_info_description_container">
                        <h3>Description</h3>
                        {!isEditing && <div className="album_info_description_text">{currentAlbum.description}</div>}
                        {isEditing && <textarea className="album_info_description_text" placeholder="Description" value={currentAlbum.description} onChange={ (event) => setCurrentAlbum({...currentAlbum, description: event.target.value}) } />}
                    </div>
                    <AlbumInfoTagView
                        albumId={album.id}
                        tags={album.tags}
                        isEditing={isEditing}
                    />
                </div>}
            </div>
            <div className="album_banner" onClick={() => onShowMoreInfo(album.id)}>
                {!isEditing && <h4>{album.publisher}</h4>}
                {isEditing && <input className="album_text_field album_publisher_text_field" placeholder="Publisher" value={currentAlbum.publisher} onChange={ (event) => setCurrentAlbum({...currentAlbum, publisher: event.target.value}) } />}
                <div className="album_banner_rating" style={{ backgroundColor: 'rgb(' + (album.ratings < 50 ? '0' : '255') + ', '+ Math.min(255, (200 - album.rating * 2)) +', 0)' }}
                     onClick={onEditRating} />
                {isEditRating && <input className="album_banner_rating_slider" type="range" min="0" max="100" value={rating} onChange={onChangeRating} onBlur={onChangeRatingDone} />}
            </div>
        </div>
    );
}

export default Album;