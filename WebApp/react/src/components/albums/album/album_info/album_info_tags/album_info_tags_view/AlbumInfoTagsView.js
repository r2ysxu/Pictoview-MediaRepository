import React from 'react';
import { useState } from 'react';
import AlbumInfoTagEdit from '../AlbumInfoTagsEdit';
import AlbumInfoTagsCategoryTagToken from '../album_info_tags_tagtoken/AlbumInfoTagsCategoryTagToken';
import AlbumInfoTagsNewCategory  from '../album_info_tags_new_category/AlbumInfoTagsNewCategory';
import './AlbumInfoTagsView.css';

function AlbumInfoTagView({albumId, tags, onClose, isTagging, currentAlbum = false}) {
    const [selectedCategory, setSelectedCategory] = useState(null);

    const onStopEdit = () => {
        if (isTagging) {
            setSelectedCategory(null);
            onClose();
        }
    }

    return (
        <div className="album_info_tag_container">
            <div className="album_info_tag_title">
                <h3>Tags</h3>
            </div>
            <div className="album_info_tag_content">
                {tags && tags.categories.map( category => {
                    return (
                        <div className="album_info_tag_category_container" key={category.id}  onClick={() => setSelectedCategory(category)}>
                            <div className="album_info_tag_category_label">
                                {category.name}
                            </div>
                            <div className={"album_info_tag_category_tag_container " + (isTagging && selectedCategory === null ? "album_info_tag_edit" : "")} >
                                {category.tags.map( (tag, index) => <AlbumInfoTagsCategoryTagToken
                                    key={category.id + '-' + index}
                                    tagToken={tag}
                                /> )}
                            </div>
                        </div>
                    );
                })}
                {isTagging && <AlbumInfoTagsNewCategory albumId={albumId} existingCategories={tags.categories} />}
            </div>
            {selectedCategory && isTagging && <AlbumInfoTagEdit albumId={albumId} category={selectedCategory} onClose={onStopEdit} currentAlbum={currentAlbum} />}
        </div>
    );
};

export default AlbumInfoTagView;