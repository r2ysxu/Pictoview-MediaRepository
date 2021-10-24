import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadCurrentAlbumInfo, loadMoreImages } from '../../../model/reducers/albumSlice';
import Album from '../album/Album';
import '../AlbumsContainer.css';

const Tab = { Album: 'Album', Images: 'Images', Video: 'Video', Music: 'Music' };

function SubAlbums({changeCurrentAlbum}) {
    const { albums } = useSelector(selectAlbums);

    return (
        <div className="albums_container">
            <div className="albums_albums_container">
                {albums.map( album => <Album
                    key={album.id}
                    album={album}
                    onChangeAlbum={changeCurrentAlbum}
                />)}
            </div>
        </div>
    );
};

export default SubAlbums;