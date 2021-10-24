import React from 'react';
import { useState } from 'react';
import './Breadcrumbs.css';

function Breadcrumbs({path, initHistory, setHistory, onChange}) {

    const pathHistory = path || [];

    const goHome = () => {
        onChange(0);
        setHistory([initHistory]);
    }

    const sliceHistory = (index) => {
        if (pathHistory.length > index) {
            const lastId = pathHistory[index].id;
            onChange(lastId);
            setHistory(pathHistory.slice(0, index + 1));
        }
    }

    const popHistory = () => {
        sliceHistory(Math.max(0, pathHistory.length - 2));
    }

    return (
        <div className="breadcrumbs_container">
            <div className="breadcrumbs_back_icon" onClick={() => popHistory()}>
                <img className="breadcrumbs_icon" src="/assets/icons/folder-symlink.svg" alt="" />
            </div>
            <div onClick={() => goHome()}>
                <img className="breadcrumbs_icon" src="/assets/icons/house-door.svg" alt="" />
            </div>
            { pathHistory.slice(1).map( (item, index) => 
                <div key={item.id}>
                    <img className="breadcrumbs_icon" src="/assets/icons/chevron-compact-right.svg" alt="" />
                    <div className="breadcrumbs_label" onClick={() => sliceHistory(index + 1)}>
                        <span>{item.name}</span>
                    </div>
                </div> ) }
        </div>
    );
};

export default Breadcrumbs;