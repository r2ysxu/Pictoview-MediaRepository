import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadMoreVideos } from '../../../../model/reducers/albumSlice';
import ScrollLoader from '../../../widgets/scroll_loader/ScrollLoader';
import VideoView from './video_view/VideoView';
import './VideoMedia.css';

function VideoMedia({albumId}) {
    const dispatch = useDispatch();
    const { videos } = useSelector(selectAlbums);
    const isLoading = useSelector((state) => state.album.isLoading);
    const [selectedVideoIndex, setSelectedVideoIndex] = useState(null);

    const loadMore = () => {
        if (!isLoading) dispatch(loadMoreVideos({albumId, page: videos.pageInfo.page + 1}));
    }

    return (
        <div>
            <ScrollLoader isLoading={isLoading} loadMore={loadMore} height={50} hasMore={videos.pageInfo.hasNext}>
                <div className="album_media_list">
                    {videos.items.map( (video, index) =>
                        <div key={video.id} className="video_media_container" onClick={() => {setSelectedVideoIndex(index)} }>
                            <img src="/assets/icons/play-circle.svg" alt={'video ' + video.id} title={video.name} />
                        </div>
                )}
                </div>
            </ScrollLoader>
            <VideoView
                videoItems={videos.items}
                selectedIndex={selectedVideoIndex}
                onSelectIndex={setSelectedVideoIndex} />
        </div>
    );
}

export default VideoMedia;