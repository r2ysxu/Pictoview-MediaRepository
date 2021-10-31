import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadMoreAlbums } from '../../../model/reducers/albumSlice';
import ScrollLoader from '../../scroll_loader/ScrollLoader';
import Album from '../album/Album';
import '../AlbumsContainer.css';

const Tab = { Album: 'Album', Images: 'Images', Video: 'Video', Music: 'Music' };

function SubAlbums({changeCurrentAlbum}) {
    const dispatch = useDispatch();
    const { albums } = useSelector(selectAlbums);
    const isLoading = useSelector((state) => state.album.isLoading);

    const loadMore = () => {
        if (!isLoading) dispatch(loadMoreAlbums());
    }

    return (
        <div className="albums_container">
            <ScrollLoader isLoading={isLoading} loadMore={loadMore} height={50} hasMore={albums.pageInfo.hasNext}>
                <div className="albums_albums_container">
                    {(albums.items || []).map( album => <Album
                        key={album.id}
                        album={album}
                        onChangeAlbum={changeCurrentAlbum}
                    />)}
                </div>
            </ScrollLoader>
        </div>
    );
};

export default SubAlbums;