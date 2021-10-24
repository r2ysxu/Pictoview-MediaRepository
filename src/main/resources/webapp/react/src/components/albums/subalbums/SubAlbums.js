import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadCurrentAlbumInfo, loadMoreImages } from '../../../model/reducers/albumSlice';
import Album from '../album/Album';
import '../AlbumsContainer.css';

const Tab = { Album: 'Album', Images: 'Images', Video: 'Video', Music: 'Music' };

function SubAlbums({removeAlbumHistory, changeCurrentAlbum}) {
    const { albums } = useSelector(selectAlbums);

    return (
        <div className="albums_container">
            <div onClick={() => removeAlbumHistory()}>
               <img className="file_manager_file_icon" src="/assets/icons/folder-symlink.svg" alt="" />
            </div>
            {albums.length > 0 && <div><span className="albums_prompt_text">Albums</span></div>}
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