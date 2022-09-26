import React, { useState } from 'react';
import AlbumInfoTagView from './album_info_tags/album_info_tags_view/AlbumInfoTagsView';
import '../Album.css';

function AlbumInfo({album, isEditing, setEditing, description, setDescription, onUpdateAlbum, onDeleteAlbum, currentAlbum = false}) {
    const [isTagging, setTagging] = useState(false);

    const onClose = () => {
        setDescription(album.description);
        setEditing(false);
    }

    return (
        <div className="album_info_container">
            <img className={"album_info_edit_icon " + (isTagging ? "album_info_edit_icon_enabled" : "")}
                src="/assets/icons/tags.svg" alt="" title="Edit Tags (Select category to edit)"
                onClick={() => {
                    setTagging(!isTagging);
                }}
            />
            {!isEditing && <img className="album_info_edit_icon"
                src="/assets/icons/pencil.svg" alt="" title="Edit Title &amp; Description"
                onClick={() => {
                    setEditing(!isEditing);
                }}
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
                onClose={() => {setTagging(false)}}
            />
        </div>);
}

export default AlbumInfo;