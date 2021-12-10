import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadMoreAlbums, loadMoreSearchAlbums } from '../../../model/reducers/albumSlice';
import ScrollLoader from '../../widgets/scroll_loader/ScrollLoader';
import Album from '../album/Album';
import '../AlbumsContainer.css';

function SubAlbums({albumId, changeCurrentAlbum}) {
    const dispatch = useDispatch();
    const { albums } = useSelector(selectAlbums);
    const isLoading = useSelector((state) => state.album.isLoading);

    const loadMore = () => {
        if (!isLoading) {
            if (albumId === null) dispatch(loadMoreSearchAlbums({page: albums.pageInfo.page + 1}));
            else dispatch(loadMoreAlbums({albumId, page: albums.pageInfo.page + 1}));
        }
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