import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadCurrentAlbumInfo, loadMoreImages } from '../../model/reducers/albumSlice';
import SubAlbums from './subalbums/SubAlbums';
import ImageMedia from './media/image_media/ImageMedia';
import VideoMedia from './media/video_media/VideoMedia';
import TabSelector from '../tab_selector/TabSelector';
import Breadcrumbs from '../breadcrumbs/Breadcrumbs';
import './AlbumsContainer.css';

function AlbumsContainer({albumHistory, setAlbumHistory}) {
    const dispatch = useDispatch();
    const { albumId, images, videos } = useSelector(selectAlbums);

    const changeAlbum = (id) => {
        dispatch(loadCurrentAlbumInfo(id));
    }

    const changeCurrentAlbum = ({id, name}) => {
        changeAlbum(id);
        setAlbumHistory([...albumHistory, {id, name}]);
    }

    useEffect(()=> {
        dispatch(loadCurrentAlbumInfo(albumId));
        dispatch(loadMoreImages({page: 1, albumId}));
    }, [dispatch, albumId]);

    return (
        <TabSelector
            tabs={[
                {label: "Albums"},
                {label: "Images", badgeLabel: images.pageInfo.total},
                {label: "Videos", badgeLabel: videos.pageInfo.total},
                {label: "Music", disabled: true},
            ]}
            headerContent={
                <Breadcrumbs
                  initHistory={{id: 0, name: ""}}
                  path={albumHistory}
                  setHistory={setAlbumHistory}
                  onChange={changeAlbum} />
            }>
            <SubAlbums changeCurrentAlbum={changeCurrentAlbum}/>
            <ImageMedia albumId={albumId} />
            <VideoMedia albumId={albumId} />
        </TabSelector>
    );
}

export default AlbumsContainer;