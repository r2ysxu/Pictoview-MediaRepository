import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadCurrentAlbumInfo, loadMoreImages } from '../../model/reducers/albumSlice';
import SubAlbums from './subalbums/SubAlbums';
import ImageMedia from './media/image_media/ImageMedia';
import VideoMedia from './media/video_media/VideoMedia';
import TabSelector from '../tab_selector/TabSelector';
import './AlbumsContainer.css';

function AlbumsContainer({albumHistory, setAlbumHistory}) {
    const dispatch = useDispatch();
    const { albumId } = useSelector(selectAlbums);

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
        <TabSelector tabs={[
                {label: "Albums"},
                {label: "Images"},
                {label: "Videos"},
                {label: "Music", disabled: true},
            ]}>
            <SubAlbums
                removeAlbumHistory={removeAlbumHistory}
                changeCurrentAlbum={changeCurrentAlbum}/>
            <ImageMedia albumId={albumId} />
            <VideoMedia albumId={albumId} />
        </TabSelector>
    );
}

export default AlbumsContainer;