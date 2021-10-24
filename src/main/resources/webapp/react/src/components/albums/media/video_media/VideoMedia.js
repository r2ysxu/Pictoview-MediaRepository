import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import ScrollLoader from '../../../scroll_loader/ScrollLoader';
import { selectAlbums, loadMoreVideos } from '../../../../model/reducers/albumSlice';
import VideoView from './video_view/VideoView';
import './VideoMedia.css';

function VideoMedia({albumId}) {
    const dispatch = useDispatch();
    const { videoIds } = useSelector(selectAlbums);
    const isLoading = useSelector((state) => state.album.isLoading);
    const hasMore = useSelector((state) => state.album.videoIdsHasMore);
    const [page, setPage] = useState(0);
    const [selectedVideoIndex, setSelectedVideoIndex] = useState(null);

    const loadMore = () => {
        if (!isLoading) dispatch(loadMoreVideos());
    }

    return (
        <div>
            <ScrollLoader isLoading={isLoading} loadMore={loadMore} height={50} hasMore={hasMore}>
                <div className="image_media_list">
                    {videoIds.map( (videoId, index) =>
                        <div key={videoId} className="video_media_container" onClick={() => {setSelectedVideoIndex(index)} }>
                            <img src="/assets/icons/film.svg" alt={'video ' + videoId} />
                        </div>
                )}
                </div>
            </ScrollLoader>
            <VideoView
                videoIds={videoIds}
                selectedIndex={selectedVideoIndex}
                onSelectIndex={setSelectedVideoIndex} />
        </div>
    );
}

export default VideoMedia;