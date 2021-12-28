import React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { updateAlbum } from '../../../../model/reducers/albumSlice';
import './AlbumRating.css';

function AlbumRating({album}) {
    const dispatch = useDispatch();
    const [rating, setRating] = useState(album.rating);
    const [isEditRating, setEditRating] = useState(false);

    const computeRatingClass = (rating) => {
        if (parseInt(rating) === 0) return 'album_rating_no_rating';
        else return 'album_rating_tier'+ Math.round(rating / 20);
    }

    const onEditRating = (event) => {
        setEditRating(!isEditRating);
        event.preventDefault();
        return false;
    }

    const onChangeRating = (event) => {
        setRating(event.target.value);
    }

    const onChangeRatingDone = (event) => {
        dispatch(updateAlbum({
            id: album.id,
            rating
        }));
        setEditRating(false);
    }

    return (
        <>
            <div className={"album_banner_rating " + computeRatingClass(rating)}
                onClick={onEditRating} />
            {isEditRating && 
                <>
                    <input className="album_banner_rating_slider" type="range" min="0" max="100" step="20" value={rating} onChange={onChangeRating} onBlur={onChangeRatingDone} list={"rating_tickmarks_" + album.id} />
                    <datalist id={"rating_tickmarks_" + album.id}>
                        <option value="0"></option>
                        <option value="20"></option>
                        <option value="40"></option>
                        <option value="60"></option>
                        <option value="80"></option>
                        <option value="100"></option>
                    </datalist>
                </>}
        </>
    );
}

export default AlbumRating;