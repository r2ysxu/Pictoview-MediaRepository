import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import ClipLoader from "react-spinners/ClipLoader";
import { uploadAlbumFile, loadCurrentAlbumInfo, selectAlbums } from '../../../model/reducers/albumSlice';
import { createAlbum } from '../../../model/reducers/albumSlice';
import ToggleButton from '../../widgets/toggle_button/ToggleButton';
import TextField from '../../widgets/text_field/TextField';
import '../../widgets/common/Common.css';

function CreateAlbum({onDone}) {
    const dispatch = useDispatch();
    const currentAlbumId = useSelector(selectAlbums).albumId;
    const [isLoading, setIsLoading] = useState(false);
    const [newAlbum, setNewAlbum] = useState({
        name: "",
        publisher: "",
        description: ""
    });
    const [newAlbumFile, setNewAlbumFile] = useState({
        albumId: null,
        fromMetadata: false,
        file: null
    });

    const setFromMetadata = (fromMetadata) => {
        setNewAlbumFile({...newAlbumFile, fromMetadata});
    }

    const onFileUploadChange = (event) => {
        const file = event.target.files[0];
        setNewAlbumFile({...newAlbumFile, file});
    }

    const onFileUpload = () => {
        uploadAlbumFile(newAlbumFile).then( () => {
            setIsLoading(false);
            dispatch(loadCurrentAlbumInfo({ albumId: currentAlbumId })).then(onDone);
        });
    }

    const onSubmit = () => {
        setIsLoading(true);
        createAlbum(newAlbum).then( album => {
            newAlbumFile.albumId = album.id;
            if (newAlbumFile.albumId && newAlbumFile.file) {
                onFileUpload();
            } else {
                setIsLoading(false);
                dispatch(loadCurrentAlbumInfo({ albumId: currentAlbumId })).then(onDone);
            }
        })
    };

    return (
        <div>
            <h3>Create New Album</h3>
            <ToggleButton selectedValue={newAlbumFile.fromMetadata} onSelect={setFromMetadata} label="Populate From File" />
            {!newAlbumFile.fromMetadata && <div>
                <TextField label="Name" value={newAlbum.name} onChange={(event) => setNewAlbum({...newAlbum, name: event.target.value})} />
                <TextField label="Publisher" value={newAlbum.publisher} onChange={(event) => setNewAlbum({...newAlbum, publisher: event.target.value})} />
                <textarea className="text_field" placeholder="Description" value={newAlbum.description} onChange={(event) => setNewAlbum({...newAlbum, description: event.target.value})} />
            </div>}
                <input className="text_field" type="file" onChange={onFileUploadChange} disabled={isLoading} />
                <button className="button_form_submit" disabled={isLoading} onClick={onSubmit}>
                    <span>Create</span>
                    {isLoading && <div className="button_form_loader">
                        <ClipLoader loading={isLoading} size={15} />
                    </div>}
                </button>
        </div>
    );
}

export default CreateAlbum;