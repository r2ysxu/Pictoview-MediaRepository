import React from 'react';

function RatingItem({value, onSetLower, onSetUpper}) {

  const onSetValue = (event, ratingValue) => {
    if (event.shiftKey) {
      if (ratingValue < value.lower)
        onSetLower(ratingValue);
      onSetUpper(ratingValue);
    } else {
      if (ratingValue > value.upper)
        onSetUpper(ratingValue);
      onSetLower(ratingValue);
    }
  }

  return (
    <div className="tag_list_rating_container">
      <div className={"tag_list_rating_item tag_list_rating_item_border " + (value.lower === 0 && value.upper >= 0 ? "" : "tag_list_rating_unselected") }
        onClick={(event) => {onSetValue(event, 0)}} title="Rating: no star" />
      <div className={"tag_list_rating_item tag_list_rating_item_border " + (value.lower <= 20 && value.upper >= 20 ? "" : "tag_list_rating_unselected") }
        onClick={(event) => {onSetValue(event, 20)}} title="Rating: *" />
      <div className={"tag_list_rating_item tag_list_rating_item_border " + (value.lower <= 40 && value.upper >= 40 ? "" : "tag_list_rating_unselected") }
        onClick={(event) => {onSetValue(event, 40)}} title="Rating: **" />
      <div className={"tag_list_rating_item tag_list_rating_item_border " + (value.lower <= 60 && value.upper >= 60 ? "" : "tag_list_rating_unselected") }
        onClick={(event) => {onSetValue(event, 60)}} title="Rating: ***" />
      <div className={"tag_list_rating_item tag_list_rating_item_border " + (value.lower <= 80 && value.upper >= 80 ? "" : "tag_list_rating_unselected") }
        onClick={(event) => {onSetValue(event, 80)}} title="Rating: ****" />
      <div className={"tag_list_rating_item " + (value.lower <= 100 && value.upper >= 100 ? "" : "tag_list_rating_unselected") }
        onClick={(event) => {onSetValue(event, 100)}} title="Rating: *****" />
    </div>
  );
}

export default RatingItem;