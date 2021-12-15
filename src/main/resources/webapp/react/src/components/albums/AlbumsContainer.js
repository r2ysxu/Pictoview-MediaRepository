import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadCurrentAlbumInfo } from '../../model/reducers/albumSlice';
import Modal from '../widgets/modal/Modal';
import TabSelector from '../widgets/tab_selector/TabSelector';
import Breadcrumbs from '../breadcrumbs/Breadcrumbs';
import SubAlbums from './subalbums/SubAlbums';
import ImageMedia from './media/image_media/ImageMedia';
import VideoMedia from './media/video_media/VideoMedia';
import AudioMedia from './media/audio_media/AudioMedia';
import CreateMedia  from './new_media/CreateMedia';
import './AlbumsContainer.css';

function AlbumsContainer({albumId, history}) {
    const dispatch = useDispatch();
    const [showAddMedia, setShowAddMedia] = useState(false);
    const { metaType, albums, images, videos, audios} = useSelector(selectAlbums);

    const changeCurrentAlbum = (id) => {
        const newHistory = history.length === 0 ? [] : history.split(',');
        newHistory.push(albumId);
        window.location = '/album?albumId=' + id + '&history=' + newHistory.join(',');
    }

    useEffect(()=> {
        if (albumId !== null) dispatch(loadCurrentAlbumInfo(albumId));
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
                <Breadcrumbs history={history} />}
            sideContent={
                <div>
                    <img className="albums_add_media_button" src="/assets/icons/plus-circle-fill.svg" alt=""
                        onClick={() => setShowAddMedia(true)} />
                    <Modal
                        isShown={showAddMedia}
                        onHide={() => setShowAddMedia(false)}
                        content={<CreateMedia onDone={() => setShowAddMedia(false)} />}
                    />
                </div>
            }>
            <SubAlbums albumId={albumId} changeCurrentAlbum={changeCurrentAlbum}/>
            <ImageMedia albumId={albumId} />
            <VideoMedia albumId={albumId} />
            <AudioMedia albumId={albumId} />
        </TabSelector>
    );
}

export default AlbumsContainer;