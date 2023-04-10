import React from 'react';
import AlbumInfoTagEdit from './albumInfoTagsEdit';
import AlbumInfoTagsTagToken from './albumInfoTagToken';
import AlbumInfoTagsNewCategory  from './albumInfoTagNewCategory';
import './styles.css';

const capitalize = (str) => {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

function AlbumInfoTagView({albumId, tags, onClose, isTagging, selectedCategory, onSelectCategory }) {

  const onStopEdit = () => {
    if (isTagging) {
      onSelectCategory(null);
      onClose();
    }
  }

  return (
    <div className="album_info_tag_container">
      <div className="album_info_tag_title">
        <h3>Tags</h3>
      </div>
      <div className="album_info_tag_content">
        {isTagging && <AlbumInfoTagsNewCategory albumId={albumId} existingCategories={tags.categories} />}
        {tags && tags.categories.map( category => {
          return (
            <div className={"album_info_tag_category_container "  + (isTagging && selectedCategory === null ? "album_info_tag_edit" : "")} key={category.id}  onClick={() => onSelectCategory(category)}>
              <span className="album_info_tag_category_label">
                {capitalize(category.name)}
              </span>
                {category.tags.map( (tag, index) => <AlbumInfoTagsTagToken key={category.id + '-' + index} tagToken={tag} />)}
            </div>
          );
        })}
      </div>
      {selectedCategory && isTagging && <AlbumInfoTagEdit albumId={albumId} category={selectedCategory} onClose={onStopEdit} />}
    </div>
  );
};

export default AlbumInfoTagView;