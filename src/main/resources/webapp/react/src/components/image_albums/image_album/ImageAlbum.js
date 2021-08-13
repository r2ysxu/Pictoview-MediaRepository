import React from 'react';
import { useState } from 'react';
import './ImageAlbum.css';

function ImageAlbum({album, onChangeParentId}) {
    return (
        <div className="image_album_container">
            <div className="image_album_title">
                <h3>{album.name}</h3>
            </div>
            <div className="image_album_wrapper" onClick={() => onChangeParentId(album.id)}>
                <img className="image_album_image" src={'/album/image/cover?albumid=' + album.id} alt="" />
            </div>
            <div className="image_album_banner">
                <h4>{album.subtitle}</h4>
            </div>
        </div>
    );
}

export default ImageAlbum;