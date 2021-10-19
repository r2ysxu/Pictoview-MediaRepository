import React from 'react';
import { useState } from 'react';
import AlbumInfoTagEdit from '../AlbumInfoTagsEdit';
import AlbumInfoTagsCategoryTagToken from '../album_info_tags_tagtoken/AlbumInfoTagsCategoryTagToken';
import AlbumInfoTagsNewCategory  from '../album_info_tags_new_category/AlbumInfoTagsNewCategory';
import './AlbumInfoTagsView.css';

function AlbumInfoTagView({albumId, tags, isEditing, setEditing}) {
    const [selectedCategory, setSelectedCategory] = useState(null);

    const onClose = () => {
        setSelectedCategory(null);
    }

    return (
        <div className="album_info_tag_container">
            <div className="album_info_tag_title">
                <h3>Tags</h3>
                <img className="album_info_tag_title_edit_icon"
                    src="/assets/icons/pencil.svg" alt=""
                    onClick={() => {
                        setEditing(!isEditing);
                        setSelectedCategory(null);
                    }} />
            </div>
            <div className="album_info_tag_content">
                {tags && tags.categories.map( category => {
                    return (
                        <div className="album_info_tag_category_container" key={category.id} >
                            <div className="album_info_tag_category_label">{category.name}</div>
                            <div className="album_info_tag_category_tag_container">
                                {category.tags.map( (tag, index) => <AlbumInfoTagsCategoryTagToken
                                    key={category.id + '-' + index}
                                    tagToken={tag}
                                    even={index % 2 === 0}
                                /> )}
                                {isEditing && <div className="album_info_tag_edit_icon_container"
                                  onClick={() => {
                                    setSelectedCategory(category);
                                  }} >
                                    <img className="album_info_tag_edit_add_tag_icon" src="/assets/icons/pencil.svg" alt="" />
                                </div>}
                            </div>
                        </div>
                    );
                })}
                {isEditing && <AlbumInfoTagsNewCategory albumId={albumId} existingCategories={tags.categories} />}
            </div>
            {selectedCategory && <AlbumInfoTagEdit albumId={albumId} category={selectedCategory} onClose={onClose} />}
        </div>
    );
};

export default AlbumInfoTagView;