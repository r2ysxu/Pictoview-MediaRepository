import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import ClipLoader from "react-spinners/ClipLoader";
import { uploadAlbumMediaFile, loadCurrentAlbumInfo, selectAlbums } from '../../../model/reducers/albumSlice';
import '../../widgets/common/Common.css';

function CreateMedia({onDone}) {
    const dispatch = useDispatch();
    const albumId = useSelector(selectAlbums).albumId;
    const [isLoading, setIsLoading] = useState(false);
    const [newMediaFile, setNewMediaFile] = useState({
        albumId,
        file: null
    });

    const onFileUploadChange = (event) => {
        const file = event.target.files[0];
        setNewMediaFile({...newMediaFile, file});
    }

    const onFileUpload = () => {
        uploadAlbumMediaFile(newMediaFile).then( () => {
            setIsLoading(false);
            dispatch(loadCurrentAlbumInfo({ albumId })).then(onDone);
        });
    }

    const onSubmit = () => {
        if (newMediaFile.albumId && newMediaFile.file && albumId > 0) {
            setIsLoading(true);
            onFileUpload();
        }
    };

    return (
        <div>
            <h3>Upload Media</h3>
            <input className="text_field" type="file" onChange={onFileUploadChange} disabled={isLoading} />
            <button className="button_form_submit" disabled={isLoading || albumId < 1 || !newMediaFile.file} onClick={onSubmit}>
                <span>Create</span>
                {isLoading && <div className="button_form_loader">
                    <ClipLoader loading={isLoading} size={15} />
                </div>}
            </button>
        </div>
    );
}

export default CreateMedia;