import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadMoreAudio } from '../../../../model/reducers/albumSlice';
import ScrollLoader from '../../../widgets/scroll_loader';
import AudioView from './audio_view';
import AudioMediaItem from './AudioMediaItem';
import './styles.css';

function AudioMedia({albumId}) {
  const dispatch = useDispatch();
  const { audios } = useSelector(selectAlbums);
  const isLoading = useSelector((state) => state.album.isLoading);
  const [selectedVideoIndex, setSelectedVideoIndex] = useState(null);
  const [isFetching, setFetching] = useState(false);

  const loadMore = () => {
    if (!isLoading && isFetching) {
      setFetching(true);
      dispatch(loadMoreAudio({albumId, page: audios.pageInfo.page + 1}))
        .then( () => setFetching(false));
    }
  }

  return (
    <div>
      <ScrollLoader isLoading={isLoading} loadMore={loadMore} height={50} hasMore={audios.pageInfo.hasNext}>
        <div className="album_media_list">
          {audios.items.map( (audio, index) => <AudioMediaItem key={index} albumId={albumId} audio={audio} trackNumber={index} onSelect={() => setSelectedVideoIndex(index)} />
        )}
        </div>
      </ScrollLoader>
      <AudioView
          audioItems={audios.items}
          selectedIndex={selectedVideoIndex}
          onSelectIndex={setSelectedVideoIndex} />
    </div>
  );
}

export default AudioMedia;