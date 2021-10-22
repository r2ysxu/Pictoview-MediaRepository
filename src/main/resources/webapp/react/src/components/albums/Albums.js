import React from 'react';
import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadCurrentAlbumInfo, loadMoreImages } from '../../model/reducers/albumSlice';
import Album from './album/Album';
import ImageMedia from './media/image_media/ImageMedia';
import VideoMedia from './media/video_media/VideoMedia';
import './Albums.css';

function Albums({albumHistory, setAlbumHistory}) {
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
                <ImageMedia albumId={albumId} />
                <VideoMedia albumId={albumId} />
            </div>
        </>
    );
}

export default Albums;