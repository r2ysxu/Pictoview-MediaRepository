import React from 'react';
import './Modal.css';

function Modal({content, isShown, onHide}) {
    return isShown ?
        <div className="modal_container">
            <div className="modal_background" onClick={onHide} />
            <div className="modal_dialog">
                <div className="modal_header">
                    <img src="/assets/icons/x.svg" onClick={onHide} />
                </div>
                <div className="modal_content">
                    {content}
                </div>
            </div>
        </div> : null
}

export default Modal;