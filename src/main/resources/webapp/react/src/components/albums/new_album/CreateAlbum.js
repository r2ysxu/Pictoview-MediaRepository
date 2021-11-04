import React from 'react';
import { useState } from 'react';
import { createAlbum } from '../../../model/reducers/albumSlice';
import ToggleButton from '../../widgets/toggle_button/ToggleButton';
import TextField from '../../widgets/text_field/TextField';
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
                <TextField label="Name" value={newAlbum.name} onChange={(event) => setNewAlbum({...newAlbum, name: event.target.value})} />
                <TextField label="Publisher" value={newAlbum.publisher} onChange={(event) => setNewAlbum({...newAlbum, publisher: event.target.value})} />
                <textarea className="text_field" placeholder="Description" value={newAlbum.description} onChange={(event) => setNewAlbum({...newAlbum, description: event.target.value})} />
            </div>}
            <button onClick={onSubmit}>Create</button>
        </div>
    );
}

export default CreateAlbum;