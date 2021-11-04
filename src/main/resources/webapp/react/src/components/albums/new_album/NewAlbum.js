import React from 'react';
import { useState } from 'react';
import { createAlbum } from '../../../model/reducers/albumSlice';
import ToggleButton from '../../toggle_button/ToggleButton';
import NewAlbumUploader from './NewAlbumUploader';
import CreateAlbum from './CreateAlbum';

const Steps = { create: "CREATE", upload: "UPLOAD", done: "DONE" }

function NewAlbum({onDone}) {
    const [fromMetadata, setFromMetadata] = useState(false);
    const [currentStep, setCurrentStep] = useState(Steps.create);
    const [albumId, setAlbumId] = useState(null);

    const toUploadStep = () => {
        setCurrentStep(Steps.upload);
    }

    return (
        <div>
            <ToggleButton selectedValue={fromMetadata} onSelect={setFromMetadata} label="From File" />
            {currentStep === Steps.create && <CreateAlbum fromMetadata={fromMetadata} onNextStep={toUploadStep} setAlbumId={setAlbumId} />}
            {currentStep === Steps.upload && <NewAlbumUploader albumId={albumId} fromMetadata={fromMetadata} onDone={onDone} />}
        </div>
    );
}

export default NewAlbum;