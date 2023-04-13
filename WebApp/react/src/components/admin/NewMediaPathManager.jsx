import React from 'react';
import { useState } from 'react';
import { post_uploadMediaPath } from '../../model/apis/album_apis';

function NewMediaPathManager() {
  const [path, setPath] = useState('');
  const [albumId, setAlbumId] = useState(0);
  const [loading, setLoading] = useState(false);

  const onAddMedia = (event) => {
    setLoading(true);
    post_uploadMediaPath(path, albumId)
      .then(() => {
        setPath('');
        setLoading(false);
      });
  }

  return (
    <div>
      <input type="text"
        value={path}
        onChange={(event) => {
          setPath(event.target.value);
        }}
        placeholder="Media Path" />
        <input type="text"
         value={albumId}
         onChange={ (event) => {
          setAlbumId(event.target.value);
         }}
         placeholder="Album ID"
        />
      <button onClick={onAddMedia} disabled={loading}>Add Media</button>
    </div>
  )
}

export default NewMediaPathManager;