import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadMoreAudio } from '../../../../model/reducers/albumSlice';
import ScrollLoader from '../../../widgets/scroll_loader/ScrollLoader';
import AudioView from './audio_view/AudioView';
import './AudioMedia.css';

function AudioMedia({albumId}) {
    const dispatch = useDispatch();
    const { audios } = useSelector(selectAlbums);
    const isLoading = useSelector((state) => state.album.isLoading);
    const [selectedVideoIndex, setSelectedVideoIndex] = useState(null);

    const loadMore = () => {
        if (!isLoading) dispatch(loadMoreAudio({albumId, page: audios.pageInfo.page + 1}));
    }

    return (
        <div>
            <ScrollLoader isLoading={isLoading} loadMore={loadMore} height={50} hasMore={audios.pageInfo.hasNext}>
                <div className="album_media_list">
                    {audios.items.map( (audio, index) =>
                        <div key={audio.id} className="audio_media_container">
                            <div className="audio_media_info_container">
                                <div className="audio_media_info_container_song">
                                    <h3>{audio.title}</h3>
                                    <h4>{audio.artist}</h4>
                                    <h5>{audio.genre}</h5>
                                </div>
                            </div>
                            <img className="audio_media_play_button"
                                 src="/assets/icons/play-circle.svg"
                                 alt={'video ' + audio.id}
                                 title="Play"
                                 onClick={() => {setSelectedVideoIndex(index)} } />
                        </div>
                )}
                </div>
            </ScrollLoader>
            <AudioView
                audioItems={audios.items}
                selectedIndex={selectedVideoIndex}
                onSelectIndex={setSelectedVideoIndex}
                />
        </div>
    );
}

export default AudioMedia;