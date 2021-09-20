import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import ImageAlbum from './image_album/ImageAlbum';
import ImageMedia from './image_media/ImageMedia';
import { selectAlbums, loadCurrentAlbumInfo } from '../../model/reducers/albumSlice';
import './ImageAlbums.css';

async function listAlbums(page, parentId) {
    let searchParams = new URLSearchParams({page, parentId});
    return fetch('/album/image/list?' + searchParams.toString()).then( response => response.json());
}

function ImageAlbums({albumHistory, setAlbumHistory}) {
    const dispatch = useDispatch();
    const [page, setPage] = useState(0);
    const { albumId, albums } = useSelector(selectAlbums);

    const changeCurrentAlbum = (newAlbumId) => {
        dispatch(loadCurrentAlbumInfo(newAlbumId));
        setAlbumHistory([...albumHistory, newAlbumId]);
    }

    const removeAlbumHistory = () => {
        if (albumHistory.length > 1) {
            albumHistory.pop();
            const lastAlbumId = albumHistory[albumHistory.length - 1];
            setAlbumHistory(albumHistory);
            dispatch(loadCurrentAlbumInfo(lastAlbumId));
        }
    }

    useEffect(()=> {
        dispatch(loadCurrentAlbumInfo(albumId));
    }, [albumId]);

    return (
        <>
            <div className="image_albums_container">
                <div onClick={() => removeAlbumHistory()}>
                   <img className="file_manager_file_icon" src="/assets/icons/folder-symlink.svg" alt="" />
                </div>
                {albums.length > 0 && <div><span className="image_albums_prompt_text">Albums</span></div>}
                <div className="image_albums_albums_container">
                    {albums.map( album => <ImageAlbum key={album.id} album={album} onChangeAlbum={changeCurrentAlbum} /> )}
                </div>
                <ImageMedia albumId={albumId} />
            </div>
        </>
    );
}

export default ImageAlbums;