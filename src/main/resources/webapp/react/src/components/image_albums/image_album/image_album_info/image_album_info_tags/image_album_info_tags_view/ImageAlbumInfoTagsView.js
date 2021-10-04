import React from 'react';
import { useState } from 'react';
import ImageAlbumInfoTagEdit from '../ImageAlbumInfoTagsEdit';
import ImageAlbumInfoTagsCategoryTagToken from '../image_album_info_tags_tagtoken/ImageAlbumInfoTagsCategoryTagToken';
import ImageAlbumInfoTagsNewCategory  from '../image_album_info_tags_new_category/ImageAlbumInfoTagsNewCategory';
import './ImageAlbumInfoTagsView.css';

function ImageAlbumInfoTagView({albumId, tags, isEditing, setEditing}) {
    const [selectedCategory, setSelectedCategory] = useState(null);

    const onClose = () => {
        setSelectedCategory(null);
    }

    return (
        <div className="image_album_info_tag_container">
            <div className="image_album_info_tag_title">
                <h3>Tags</h3>
                <img className="image_album_info_tag_title_edit_icon"
                    src="/assets/icons/pencil.svg" alt=""
                    onClick={() => {
                        setEditing(!isEditing);
                        setSelectedCategory(null);
                    }} />
            </div>
            <div className="image_album_info_tag_content">
                {tags && tags.categories.map( category => {
                    return (
                        <div className="image_album_info_tag_category_container" key={category.id} >
                            <div className="image_album_info_tag_category_label">{category.name}</div>
                            <div className="image_album_info_tag_category_tag_container">
                                {category.tags.map( (tag, index) => <ImageAlbumInfoTagsCategoryTagToken
                                    key={category.id + '-' + index}
                                    tagToken={tag}
                                    even={index % 2 === 0}
                                /> )}
                                {isEditing && <div className="image_album_info_tag_edit_icon_container"
                                  onClick={() => {
                                    setSelectedCategory(category);
                                  }} >
                                    <img className="image_album_info_tag_edit_add_tag_icon" src="/assets/icons/pencil.svg" alt="" />
                                </div>}
                            </div>
                        </div>
                    );
                })}
                {isEditing && <ImageAlbumInfoTagsNewCategory albumId={albumId} existingCategories={tags.categories} />}
            </div>
            {selectedCategory && <ImageAlbumInfoTagEdit albumId={albumId} category={selectedCategory} onClose={onClose} />}
        </div>
    );
};

export default ImageAlbumInfoTagView;