import React from 'react';
import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadCurrentAlbumInfo } from '../../model/reducers/albumSlice';
import TabSelector from '../widgets/tab_selector/TabSelector';
import SubAlbums from './subalbums/SubAlbums';
import ImageMedia from './media/image_media/ImageMedia';
import VideoMedia from './media/video_media/VideoMedia';
import AudioMedia from './media/audio_media/AudioMedia';
import Breadcrumbs from '../breadcrumbs/Breadcrumbs';
import './AlbumsContainer.css';

function AlbumsContainer({albumHistory, setAlbumHistory}) {
    const dispatch = useDispatch();
    const { albumId, metaType, albums, images, videos , audios} = useSelector(selectAlbums);

    const changeAlbum = (id) => {
        dispatch(loadCurrentAlbumInfo(id));
    }

    const changeCurrentAlbum = ({id, name}) => {
        changeAlbum(id);
        setAlbumHistory([...albumHistory, {id, name}]);
    }

    useEffect(()=> {
        dispatch(loadCurrentAlbumInfo(albumId));
    }, [dispatch, albumId]);

    const tabs = [
        {label: "Albums", value: "albums", badgeLabel: albums.pageInfo.total},
        {label: "Images", value: "images", badgeLabel: images.pageInfo.total},
        {label: "Videos", value: "videos", badgeLabel: videos.pageInfo.total},
        {label: "Music",  value: "music", badgeLabel: audios.pageInfo.total},
    ]

    return (
        <TabSelector
            tabs={tabs}
            defaultTab={metaType}
            headerContent={
                <Breadcrumbs
                  initHistory={{id: 0, name: ""}}
                  path={albumHistory}
                  setHistory={setAlbumHistory}
                  onChange={changeAlbum} />
            }>
            <SubAlbums albumId={albumId} changeCurrentAlbum={changeCurrentAlbum}/>
            <ImageMedia albumId={albumId} />
            <VideoMedia albumId={albumId} />
            <AudioMedia albumId={albumId} />
        </TabSelector>
    );
}

export default AlbumsContainer;