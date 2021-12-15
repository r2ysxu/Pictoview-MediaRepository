import React from 'react';
import './Breadcrumbs.css';

function Breadcrumbs({history}) {

    const pathHistory = () => {
        if (history.length === 0) return [];
        return history.split(',') || [];
    }

    const goHome = () => {
        window.location = '/album';
    }

    const sliceHistory = (index) => {
        if (pathHistory().length - 1 === index) {
            // Current path
        } else if (pathHistory().length > index && index >= 0) {
            const lastId = pathHistory()[index].id;
            const historyPath = pathHistory().slice(0, index);
            window.location = '/album?albumId=' + lastId + ((historyPath.length === 0) ? '' : '&history=' + historyPath.join(','));
        } else {
            window.location = '/album';
        }
    }

    const popHistory = () => {
        sliceHistory(pathHistory().length - 2);
    }

    return (
        <div className="breadcrumbs_container">
            <div className="breadcrumbs_icon_container" onClick={() => popHistory()}>
                <img className="breadcrumbs_icon" src="/assets/icons/folder-symlink.svg" alt="" />
            </div>
            <div onClick={() => goHome()}>
                <img className="breadcrumbs_icon" src="/assets/icons/house-door.svg" alt="" />
            </div>
            { pathHistory().map( (id, index) => 
                <div key={id + index}>
                    <img className="breadcrumbs_icon_chevron" src="/assets/icons/chevron-compact-right.svg" alt="" />
                    <img className="breadcrumbs_icon" src="/assets/icons/folder.svg" alt="" onClick={() => sliceHistory(index)} />
                </div> ) }
        </div>
    );
};

export default Breadcrumbs;