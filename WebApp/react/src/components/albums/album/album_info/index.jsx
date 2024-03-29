import React, { useState } from 'react';
import AlbumInfoTagView from './album_info_tags';
import '../styles.css';

function AlbumInfo({album, isEditing, setEditing, description, setDescription, onUpdateAlbum, onDeleteAlbum, currentAlbum = false}) {
  const [isTagging, setTagging] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState(null);

  const onClose = () => {
    setDescription(album.description);
    setSelectedCategory(null);
    setEditing(false);
  }

  const handleTagging = () => {
    setTagging(!isTagging);
    setSelectedCategory(null);
  }

  const handleEditting = () => {
    setEditing(!isEditing);
  }

  return (
    <div className="album_info_container">
      <img className={"album_info_edit_icon " + (isTagging ? "album_info_edit_icon_enabled" : "")}
        src="/assets/icons/tags.svg" alt="" title="Edit Tags (Select category to edit)"
        onClick={handleTagging}
      />
      {!isEditing && <img className="album_info_edit_icon"
        src="/assets/icons/pencil.svg" alt="" title="Edit Title &amp; Description"
        onClick={handleEditting}
      />}
      <img className="album_info_edit_icon album_info_edit_icon_delete"
        src="/assets/icons/x.svg" alt="" title="Delete Album (Hold Ctrl & Shift)"
        onClick={onDeleteAlbum}
      />
      {isEditing && <div className="album_info_edit_buttons">
        <img className="album_info_edit_buttons_save" src="/assets/icons/check.svg" alt="" onClick={onUpdateAlbum} />
        <img className="album_info_edit_buttons_cancel" src="/assets/icons/x.svg" alt="" onClick={onClose} />
      </div>}
      <div className="album_info_description_container">
        <h3>Description</h3>
        {!isEditing && <div className="album_info_description_text">{album.description}</div>}
        {isEditing && <textarea className="album_info_description_text" placeholder="Description" value={description} onChange={(event) => setDescription(event.target.value)} />}
      </div>
      <AlbumInfoTagView
        currentAlbum={currentAlbum}
        albumId={album.id}
        tags={album.tags}
        isTagging={isTagging}
        selectedCategory={selectedCategory}
        onSelectCategory={setSelectedCategory}
        onClose={() => {setTagging(false)}}
      />
    </div>);
}

export default AlbumInfo;