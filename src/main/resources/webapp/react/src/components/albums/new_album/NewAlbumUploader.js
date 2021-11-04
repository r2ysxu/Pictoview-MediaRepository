import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import ClipLoader from "react-spinners/ClipLoader";
import { uploadAlbumFile, loadCurrentAlbumInfo, selectAlbums } from '../../../model/reducers/albumSlice';

function NewAlbumUploader({albumId, fromMetadata, onDone}) {
    const dispatch = useDispatch();
    const currentAlbumId = useSelector(selectAlbums).albumId;
    const [newAlbumFile, setNewAlbumFile] = useState({
        albumId,
        fromMetadata,
        file: null
    });
    const [isLoading, setIsLoading] = useState(false);

    const onFileUploadChange = (event) => {
        const file = event.target.files[0];
        setNewAlbumFile({...newAlbumFile, file});
    }

    const onFileUpload = () => {
        setIsLoading(true);
        uploadAlbumFile(newAlbumFile).then( () => {
            setIsLoading(false);
            dispatch(loadCurrentAlbumInfo(currentAlbumId)).then(onDone);
        });
    }

    return (
        <div>
            <input type="file" onChange={onFileUploadChange} disabled={isLoading} />
            {isLoading && <ClipLoader loading={isLoading} size={15} />}
            {!isLoading && <button disabled={newAlbumFile.file === null || isLoading} onClick={onFileUpload}>Submit</button>}
        </div>
    )
}

export default NewAlbumUploader;