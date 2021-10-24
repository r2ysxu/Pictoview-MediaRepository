import React from 'react';
import { useState } from 'react';
import './MenuBar.css';

function MenuBar({children, selectedIndex, onSelect}) {
    const [showExpand, setShowExpand] = useState(false);
    return (
        <div className="menubar_container">
            <div className="menubar_icon" onClick={() => setShowExpand(!showExpand)}>
                <img className="searchbar_iv_icon" src="/assets/icons/list.svg" alt="" />
            </div>
            {showExpand && 
                <div className="menubar_content">
                { (children || []).map( (child, index) =>
                    <div key={index} className="menubar_child">
                        {child}
                    </div>
                ) }
                </div>
        }
        </div>
    );
}

export default MenuBar;