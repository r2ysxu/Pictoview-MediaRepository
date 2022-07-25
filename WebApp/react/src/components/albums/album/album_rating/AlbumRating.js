import React from 'react';
import { useDispatch } from 'react-redux';
import { updateAlbum } from '../../../../model/reducers/albumSlice';
import RatingSlider from '../../..//widgets/rating_slider/RatingSlider';
import './AlbumRating.css';

function AlbumRating({album}) {
    const dispatch = useDispatch();

    const onChangeRatingDone = (rating) => {
        dispatch(updateAlbum({
            id: album.id,
            rating
        }));
    }

    return (
        <RatingSlider ratings={album.rating} onChange={onChangeRatingDone} tickMarkId={album.id} className="album_rating_container" />
    );
}

export default AlbumRating;