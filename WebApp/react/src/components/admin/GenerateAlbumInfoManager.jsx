import React from 'react';
import { useState } from 'react';
import { post_generateJson } from '../../model/apis/album_apis';

function GenerateAlbumInfoManager() {
  const [albumId, setAlbumId] = useState('');
  const [loading, setLoading] = useState(false);

  const onGenerateJson = (event) => {
    setLoading(true);
    post_generateJson(albumId)
      .then(() => {
        setAlbumId('');
        setLoading(false);
      });
  }

  return (
    <div>
      <input type="text"
        value={albumId}
        onChange={(event) => {
          setAlbumId(event.target.value);
        }}
        placeholder="Album ID" />
      <button onClick={onGenerateJson} disabled={loading}>Generate <i>album_info.json</i></button>
    </div>
  )
}

export default GenerateAlbumInfoManager;