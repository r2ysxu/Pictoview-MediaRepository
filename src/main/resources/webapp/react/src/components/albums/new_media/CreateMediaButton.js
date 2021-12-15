import React from 'react';
import { useState } from 'react';
import Modal from '../../widgets/modal/Modal';
import CreateMedia  from './CreateMedia';
import '../../widgets/common/Common.css';

function CreateMediaButton({iconClass}) {
    const [showAddMedia, setShowAddMedia] = useState(false);
    return (
        <>
            <img className={iconClass} src="/assets/icons/plus-circle-fill.svg" alt=""
                onClick={() => setShowAddMedia(true)} />
            <Modal
                isShown={showAddMedia}
                onHide={() => setShowAddMedia(false)}
                content={<CreateMedia onDone={() => setShowAddMedia(false)} />}
            />
        </>
    );
}

export default CreateMediaButton;