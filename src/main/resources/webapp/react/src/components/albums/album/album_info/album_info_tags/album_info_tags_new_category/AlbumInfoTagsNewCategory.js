import React from 'react';
import { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { get_categories } from '../../../../../../model/apis/tag_apis';
import { addCategory } from '../../../../../../model/reducers/albumSlice';
import Dropdown from '../../../../../widgets/dropdown/Dropdown';
import './AlbumInfoTagsNewCategory.css';

function AlbumInfoTagsNewCategory({albumId, existingCategories}) {
    const dispatch = useDispatch();
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [categories, setCategories] = useState([]);

    const onAddCategory = () => {
        if (selectedCategory) {
            dispatch(addCategory({albumId, newCategory: selectedCategory}));
        }
    }

    useEffect(()=> {
        get_categories().then( categories => {
            const availabileCategories = (categories || []).filter( category => !existingCategories.some( existingCategory => existingCategory.id === category.id ));
            setCategories(availabileCategories);
        });
    });

    return (
        <div className="album_info_tags_new_category_container">
            <Dropdown
                placeholder="Category"
                selectedValue={selectedCategory}
                onSelect={setSelectedCategory}
                values={categories}
                width="115px"
            />
            <div className="album_info_tags_new_category_icon" onClick={onAddCategory} >
                <img className="album_info_tag_edit_add_tag_icon" src="/assets/icons/plus-circle-fill.svg" alt="" />
            </div>
        </div>
    );
}

export default AlbumInfoTagsNewCategory;