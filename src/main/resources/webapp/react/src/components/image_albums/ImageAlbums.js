import React from 'react';
import { useState, useEffect } from 'react';
import ImageAlbum from './image_album/ImageAlbum';
import ImageMedia from './image_media/ImageMedia';
import './ImageAlbums.css';

async function listAlbums(page, parentId) {
    let searchParams = new URLSearchParams({page, parentId});
    return fetch('/album/image/list?' + searchParams.toString()).then( response => response.json());
}

function ImageAlbums({albumId, albums, setAlbums, setAlbumId}) {
    const [page, setPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [imageIds, setImageIds] = useState([]);

    useEffect(()=> {
        setIsLoading(true);
        listAlbums(0, albumId).then(data => {
            setAlbums(data)
            setImageIds([]);
            setIsLoading(false);
        });
    }, [albumId]);

    return (
        <>
            <div className="image_albums_container">
                {albums.length > 0 && <div><span className="image_albums_prompt_text">Albums</span></div>}
                <div className="image_albums_albums_container">
                    {albums.map( album => <ImageAlbum key={album.id} album={album} onChangeParentId={setAlbumId} /> )}
                </div>
                <ImageMedia albumId={albumId} />
            </div>
        </>
    );
}

export default ImageAlbums;