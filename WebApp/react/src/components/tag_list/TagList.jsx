import React from 'react';
import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { loadCategories, loadTagsByCategory, selectCategories } from '../../model/reducers/tagSlice';
import TagListItem from './TagListItem';
import RatingItem from './RatingItem';
import './TagList.css';

function TagList({tagQuery}) {
  const dispatch = useDispatch();
  const { categories } = useSelector(selectCategories);
  const [searchRating, setSearchRating] = useState({lower: 0, upper: 100});


  const onSelectCategory = (categoryId) => {
    dispatch(loadTagsByCategory({categoryId}));
  }

  const onSelectTag = (category, tag, type) => {
    let tagQueryMap;
    let delimiter;
    switch(type) {
      case 'NOT':
        tagQueryMap = tagQuery.not;
        delimiter = ':!:';
        break;
      case 'OR':
        tagQueryMap = tagQuery.or;
        delimiter = ':^:';
        break;
      default:
        tagQueryMap = tagQuery.and;
        delimiter = ':&:';
        break;
    }
    if (tagQueryMap.has(category.id + '_' + tag.id)) {
      tagQueryMap.delete(category.id + '_' + tag.id);
    } else {
      let tagValue = tag.value;
      let categoryValue = category.name;
      if (tagValue.indexOf(' ') !== -1) tagValue = '"' + tagValue + '"';
      if (categoryValue.indexOf(' ') !== -1) categoryValue = '"' + categoryValue + '"';
      tagQueryMap.set(category.id + '_' + tag.id, delimiter + categoryValue + "::" + tagValue);
    }
  }

  const onSelectRatingLower = (value) => {
    setSearchRating({ ...searchRating, lower: value });
  }

  const onSelectRatingUpper = (value) => {
    setSearchRating({ ...searchRating, upper: value });
  }

  const onSearch = () => {
    const searchQuery =
      Array.from(tagQuery.and.values()).join(" ") + " " +
      Array.from(tagQuery.not.values()).join(" ") + " " +
      Array.from(tagQuery.or.values()).join(" ") + " " +
      "^^" + searchRating.lower + "-" + searchRating.upper + " ";
    const url = '/album?searchQuery=' + encodeURIComponent(searchQuery);
    window.location = url;
  }

  useEffect(()=> {
    dispatch(loadCategories());
  }, [dispatch]);

  return (
    <div>
      <div className="tag_list_rating_wrapper">
        <RatingItem value={searchRating} onSetLower={onSelectRatingLower} onSetUpper={onSelectRatingUpper} />
      </div>
      {(categories || []).map( (category, index) => 
        <TagListItem
          key={category.id}
          category={category}
          onSelectCategory={onSelectCategory}
          onSelectTag={onSelectTag} />
      )}
      <button className="button_form_submit tag_list_search_button" onClick={onSearch}>
        <span>Search</span>
      </button>
    </div>
  );
}

export default TagList;