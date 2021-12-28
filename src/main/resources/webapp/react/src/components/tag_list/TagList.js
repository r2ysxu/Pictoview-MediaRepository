import React from 'react';
import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { loadCategories, loadTagsByCategory, selectCategories } from '../../model/reducers/tagSlice';
import TagListItem from './TagListItem';
import './TagList.css';

function TagList({tagQueryMap}) {
    const dispatch = useDispatch();
    const { categories } = useSelector(selectCategories);


    const onSelectCategory = (categoryId) => {
        dispatch(loadTagsByCategory({categoryId}));
    }

    const onSelectTag = (category, tag) => {
        if (tagQueryMap.has(category.id + '_' + tag.id)) {
            tagQueryMap.delete(category.id + '_' + tag.id);
        } else {
            tagQueryMap.set(category.id + '_' + tag.id, "::" + category.name + "::" + tag.value);
        }
    }

    const onSearch = () => {
        const searchQuery = Array.from(tagQueryMap.values()).join(" ");
        const url = '/album?searchQuery=' + encodeURIComponent(searchQuery);
        window.location = url;
    }

    useEffect(()=> {
        dispatch(loadCategories());
    }, [dispatch]);

    return (
        <div>
            {(categories || []).map( (category, index) => 
                <TagListItem
                    key={category.id}
                    index={index}
                    size={(categories || []).length}
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