import React from 'react';
import { useState } from 'react';
import { post_uploadAlbumPath } from '../../model/apis/album_apis';

function NewAlbumPathManager() {
    const [path, setPath] = useState('');
    const [loading, setLoading] = useState(false);

    const onCreateAlbum = (event) => {
        setLoading(true);
        post_uploadAlbumPath(path)
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
                placeholder="File Path" />
            <button onClick={onCreateAlbum} disabled={loading}>Create Album</button>
        </div>
    )
}

export default NewAlbumPathManager;