import React from 'react';
import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadCurrentAlbumInfo } from '../../model/reducers/albumSlice';
import TabSelector from '../widgets/tab_selector/TabSelector';
import SortField from '../widgets/sort_field/SortField';
import Breadcrumbs from '../breadcrumbs/Breadcrumbs';
import SubAlbums from './subalbums/SubAlbums';
import ImageMedia from './media/image_media/ImageMedia';
import VideoMedia from './media/video_media/VideoMedia';
import AudioMedia from './media/audio_media/AudioMedia';
import CreateMediaButton  from './new_media/CreateMediaButton';
import './AlbumsContainer.css';

function AlbumsContainer({albumId, history}) {
    const dispatch = useDispatch();
    const { metaType, albums, images, videos, audios} = useSelector(selectAlbums);

    const changeCurrentAlbum = (id, openNew) => {
        const newHistory = history.length === 0 ? [] : history.split(',');
        newHistory.push(albumId);
        const url = '/album?albumId=' + id + '&history=' + newHistory.join(',');
        if(!openNew) window.location = url;
        else window.open(url, '_blank');
        return false;
    }

    const onSortField = (sort) => {
        dispatch(loadCurrentAlbumInfo({ albumId, sort }));
    }

    useEffect(()=> {
        if (albumId !== null) dispatch(loadCurrentAlbumInfo({ albumId }));
    }, [dispatch, albumId]);

    const tabs = [
        {label: "Albums", value: "albums", badgeLabel: albums.pageInfo.total},
        {label: "Images", value: "images", badgeLabel: images.pageInfo.total},
        {label: "Videos", value: "videos", badgeLabel: videos.pageInfo.total},
        {label: "Music",  value: "music", badgeLabel: audios.pageInfo.total},
    ]

    const sortFields = [
        {name: "Unsorted", value: "unsorted"},
        {name: "Name", value: "name"},
    ]

    return (
        <TabSelector
            tabs={tabs}
            defaultTab={metaType}
            headerContent={<Breadcrumbs history={history} />}
            sideContent={
                <div>
                    {albumId === 0 && <SortField
                        iconClass="albums_side_button albums_side_button_sort"
                        dropdownClass="album_side_button_dropdown"
                        onSortField={onSortField}
                        values={sortFields} />}
                    {albumId > 0 && <CreateMediaButton iconClass="albums_side_button albums_side_button_add" />}
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