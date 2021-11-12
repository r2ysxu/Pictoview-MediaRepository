import React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { updateCategoryTags } from '../../../../../model/reducers/albumSlice';
import { get_searchTags } from '../../../../../model/apis/tag_apis.js';
import Tokenizer from '../../../../widgets/tokenizer/Tokenizer';
import './album_info_tags_view/AlbumInfoTagsView.css';

function AlbumInfoTagEdit({albumId, category, onClose}) {
    const dispatch = useDispatch();
    const [autoCompleteTokens, setAutoCompleteTokens] = useState([]);
    const [tags, setTags] = useState([...category.tags]);

    const onAutoComplete = (value) => {
        if (value && value.trim().length > 0) {
            get_searchTags(value).then( (response) => {
                setAutoCompleteTokens(response);
            });
        } else {
            setAutoCompleteTokens([]);
        }
    }

    const onSave = () => {
        const taggedCategory = {
            id: category.id,
            name: category.name,
            tags
        };
        dispatch(updateCategoryTags({
            albumId,
            categories: [taggedCategory]
        }));
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
            onSave={onSave}
            onClose={onClose} />
    );
}

export default AlbumInfoTagEdit;