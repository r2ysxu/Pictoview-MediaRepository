import React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { updateCategoryTags } from 'model/reducers/albumSlice';
import { get_searchTags } from 'model/apis/tag_apis';
import Tokenizer from 'components/widgets/tokenizer';
import { relevanceTag } from './albumInfoTagToken';
import './styles.css';

function AlbumInfoTagEdit({ albumId, category, onClose }) {
  const dispatch = useDispatch();
  const [autoCompleteTokens, setAutoCompleteTokens] = useState([]);
  const [tags, setTags] = useState([...category.tags]);

  const onAutoComplete = (value) => {
    if (value.includes(':')) {
      value = value.substring(0, value.indexOf(':'));
    }
    if (value && value.trim().length > 0) {
      get_searchTags(value, category.id).then( (response) => {
        response.forEach( tag => tag.relevance = 80 );
        setAutoCompleteTokens(response);
      });
    } else {
      setAutoCompleteTokens([]);
    }
  }

  const onSave = () => {
    dispatch(updateCategoryTags({ albumId, tags, category }));
  }

  const addNewTag = (value) => {
    let relevance = 80;
    if (value.includes(':')) {
      const delimIndex = value.indexOf(':');
      relevance = parseInt(value.substring(delimIndex + 1));
      value = value.substring(0, delimIndex);
    }
    return { id: null, value, categoryId: category.id, relevance }
  }

  const onRemoveTag = (tagIndex) => {
    setTags(tags.splice(tagIndex, 1));
  }

  return (
    <Tokenizer
      title={'Tags for ' + category.name}
      tokens={tags}
      setTokens={setTags}
      autoCompleteValues={autoCompleteTokens}
      onAutoComplete={onAutoComplete}
      onRemove={onRemoveTag}
      addNewToken={addNewTag}
      relevanceClass={relevanceTag}
      onSave={onSave}
      onClose={onClose} />
  );
}

export default AlbumInfoTagEdit;