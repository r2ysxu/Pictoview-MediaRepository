import React from 'react';
import { useState } from 'react';
import { createAlbum } from '../../../model/reducers/albumSlice';
import ToggleButton from '../../toggle_button/ToggleButton';
import NewAlbumUploader from './NewAlbumUploader';

function CreateAlbum({onNextStep, fromMetadata, setAlbumId}) {
    const [newAlbum, setNewAlbum] = useState({
        name: "",
        publisher: "",
        description: ""
    });

    const onSubmit = () => {
        createAlbum(newAlbum).then( album => {
            console.log('ablum', album);
            setAlbumId(album.id);
            onNextStep();
        });
    };

    return (
        <div>
            {!fromMetadata && <div>
                <span>Name</span>
                <input type="text" value={newAlbum.name} onChange={(event) => setNewAlbum({...newAlbum, name: event.target.value})} />
                <span>Publisher</span>
                <input type="text" value={newAlbum.publisher} onChange={(event) => setNewAlbum({...newAlbum, publisher: event.target.value})} />
                <span>Description</span>
                <textarea value={newAlbum.description} onChange={(event) => setNewAlbum({...newAlbum, description: event.target.value})} />
            </div>}
            <button onClick={onSubmit}>Create</button>
        </div>
    );
}

export default CreateAlbum;