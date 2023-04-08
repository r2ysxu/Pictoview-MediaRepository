import React from 'react';
import { useState } from 'react';
import './styles.css';

function RatingSlider({ ratings, onChange, tickMarkId, className = "" }) {
  const [rating, setRating] = useState(ratings);
  const [isEditRating, setEditRating] = useState(false);

  const computeRatingClass = (rating) => {
    if (parseInt(rating) === 0) return 'album_rating_no_rating';
    else return 'rating_slider_tier'+ Math.round(rating / 20);
  }

  const onEditRating = (event) => {
    setEditRating(!isEditRating);
    event.preventDefault();
    return false;
  }

  const onChangeRating = (event) => {
    setRating(event.target.value);
    event.preventDefault();
  }

  const onChangeRatingDone = (event) => {
    onChange(rating);
    setEditRating(false);
  }

  return (
    <div className={`rating_slider_container ${className} ${computeRatingClass(rating)} ${isEditRating ? "rating_slider_container_open" : ""}`}
          onClick={onEditRating} >
      {isEditRating && <>
        <input className="rating_slider_slider" type="range" min="0" max="100" step="20" value={rating} onChange={onChangeRating} onBlur={onChangeRatingDone} list={"rating_tickmarks_" + tickMarkId} />
        <datalist id={"rating_tickmarks_" + tickMarkId}>
          <option value="0"></option>
          <option value="20"></option>
          <option value="40"></option>
          <option value="60"></option>
          <option value="80"></option>
          <option value="100"></option>
        </datalist>
      </>}
    </div>
  );
}

export default RatingSlider;