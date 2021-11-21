import React from 'react';
import { useState, useEffect, useRef } from 'react';
import './MenuBar.css';

function MenuBar({children, lastItem, selectedIndex, onSelect}) {
    const ref = useRef();
    const [showExpand, setShowExpand] = useState(false);

    useEffect(() => {
        const onOutsideClicked = (event) => {
            if (ref.current && !ref.current.contains(event.target)) {
                setShowExpand(false);
            }
        };
        document.addEventListener("mousedown", onOutsideClicked);
        return () => document.removeEventListener("mousedown", onOutsideClicked);
    }, [setShowExpand]);

    return (
        <div className="menubar_container" ref={ref}>
            <div className="menubar_icon" onClick={() => setShowExpand(!showExpand)}>
                <img className="searchbar_iv_icon" src="/assets/icons/list.svg" alt="" />
            </div>
            {showExpand && 
                <div className="menubar_content_container">
                        <div className="menubar_content">
                        { (children || []).map( (child, index) =>
                            <div key={index} className="menubar_child">
                                {child}
                            </div>
                        ) }
                        <div className="menubar_child menubar_bottom_content">
                            {lastItem}
                        </div>
                    </div>
                </div>
            }

        </div>
    );
}

export default MenuBar;