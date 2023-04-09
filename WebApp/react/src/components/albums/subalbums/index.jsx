import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadMoreAlbums, loadMoreSearchAlbums } from '../../../model/reducers/albumSlice';

import ClipLoader from "react-spinners/ClipLoader";
import ScrollLoader from '../../widgets/scroll_loader';
import Album from '../album';
import '../styles.css';

function SubAlbums({albumId, changeCurrentAlbum}) {
  const dispatch = useDispatch();
  const { albums } = useSelector(selectAlbums);
  const isLoading = useSelector((state) => state.album.isLoading);
  const [isFetching, setFetching] = useState(false);

  const loadMore = () => {
    if (!isLoading && !isFetching) {
      setFetching(true);
      if (albumId === null) {
        dispatch(loadMoreSearchAlbums({page: albums.pageInfo.page + 1, sort: albums.pageInfo.sortedBy }))
            .then( () => setFetching(false));
      } else {
        dispatch(loadMoreAlbums({albumId, page: albums.pageInfo.page + 1, sort: albums.pageInfo.sortedBy }))
            .then( () => setFetching(false));
      }
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
      {isLoading && <div className="album_load_spinner">
        <ClipLoader loading={isLoading} size={50} color={"GREY"} cssOverride={{
          borderWidth: '10px',
        }} />
      </div>}
    </div>
  );
};

export default SubAlbums;