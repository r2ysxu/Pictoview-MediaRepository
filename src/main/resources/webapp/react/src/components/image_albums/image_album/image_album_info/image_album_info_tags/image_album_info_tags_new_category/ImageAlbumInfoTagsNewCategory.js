import React from 'react';
import { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import Dropdown from '../../../../../dropdown/Dropdown';
import { get_categories } from '../../../../../../model/apis/tag_apis';
import { addCategory } from '../../../../../../model/reducers/albumSlice';
import './ImageAlbumInfoTagsNewCategory.css';

function ImageAlbumInfoTagsNewCategory({albumId, existingCategories}) {
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
    }, []);

    return (
        <div className="image_album_info_tags_new_category_container">
            <Dropdown
                placeholder="Category"
                selectedValue={selectedCategory}
                onSelect={setSelectedCategory}
                values={categories}
                width="115px"
            />
            <div className="image_album_info_tags_new_category_icon" onClick={onAddCategory} >
                <img className="image_album_info_tag_edit_add_tag_icon" src="/assets/icons/plus-circle-fill.svg" alt="" />
            </div>
        </div>
    );
}

export default ImageAlbumInfoTagsNewCategory;