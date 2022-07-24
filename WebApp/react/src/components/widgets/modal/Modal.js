import React from 'react';
import './Modal.css';

function Modal({content, isShown, onHide = null}) {
    const noCancel = onHide === null;
    return isShown ?
        <div className="modal_container">
            <div className="modal_background" onClick={onHide} />
            <div className="modal_dialog modal_dialog_float_out">
                <div className="modal_header">
                    {!noCancel && <img src="/assets/icons/x.svg" alt="close" onClick={onHide} />}
                </div>
                <div className="modal_content">
                    {content}
                </div>
            </div>
        </div> : null
}

export default Modal;