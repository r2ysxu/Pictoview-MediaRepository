import React from 'react';
import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import ImageAlbum from './image_album/ImageAlbum';
import ImageMedia from './image_media/ImageMedia';
import { selectAlbums, loadCurrentAlbumInfo, loadMoreImages } from '../../model/reducers/albumSlice';
import './ImageAlbums.css';

function ImageAlbums({albumHistory, setAlbumHistory}) {
    const dispatch = useDispatch();
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
        dispatch(loadMoreImages({page: 1, albumId}));
    }, [dispatch, albumId]);

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