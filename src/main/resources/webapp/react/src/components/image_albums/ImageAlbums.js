import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux'
import ImageAlbum from './image_album/ImageAlbum';
import ImageMedia from './image_media/ImageMedia';
import { selectAlbums, fetchAlbums } from '../../model/reducers/albumSlice';
import './ImageAlbums.css';

async function listAlbums(page, parentId) {
    let searchParams = new URLSearchParams({page, parentId});
    return fetch('/album/image/list?' + searchParams.toString()).then( response => response.json());
}

function ImageAlbums({albumHistory, setAlbums, setAlbumHistory}) {
    const dispatch = useDispatch()
    const [page, setPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const albums = useSelector(selectAlbums);
    console.log('albums', albums);
    const [albumId, setAlbumId] = useState(albumHistory.length ? albumHistory[0] : 0);

    const addAlbumHistory = (newAlbumId) => {
        setAlbumHistory([...albumHistory, newAlbumId]);
        setAlbumId(newAlbumId);
    }

    const removeAlbumHistory = () => {
        if (albumHistory.length > 1) {
            albumHistory.pop();
            const lastAlbumId = albumHistory[albumHistory.length - 1];
            setAlbumHistory(albumHistory);
            setAlbumId(lastAlbumId);
            loadAlbums(lastAlbumId);
        }
    }

    const loadAlbums = (albumId) => {
        setIsLoading(true);
        listAlbums(0, albumId).then(data => {
            setAlbums(data)
            setIsLoading(false);
        });
    }

    useEffect(()=> {
        //loadAlbums(albumId);
        dispatch(fetchAlbums(albumId));
    }, [albumId]);

    return (
        <>
            <div className="image_albums_container">
                <div onClick={() => removeAlbumHistory()}>
                   <img className="file_manager_file_icon" src="/assets/icons/folder-symlink.svg" alt="" />
                </div>
                {albums.length > 0 && <div><span className="image_albums_prompt_text">Albums</span></div>}
                <div className="image_albums_albums_container">
                    {albums.map( album => <ImageAlbum key={album.id} album={album} onChangeParentId={addAlbumHistory} /> )}
                </div>
                <ImageMedia albumId={albumId} />
            </div>
        </>
    );
}

export default ImageAlbums;