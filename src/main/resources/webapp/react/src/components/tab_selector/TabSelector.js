import React from 'react';
import { useState } from 'react';
import './TabSelector.css';

function TabSelector({headerContent, tabs, children}) {
    const [selectedTabIndex, setSelectedTabIndex] = useState(0);

    return (
        <div className="tab_selector_container">
            <div>
                {headerContent}
            </div>
            <div className="tab_selector_tabs">
                {tabs.map( (tab, index) => 
                    <div className={"tab_selector_button " + 
                            (tab.disabled === true ? "tab_selector_button_disabled " : " ") +
                            (selectedTabIndex === index ? "tab_selector_button_selected " : " ")}
                      onClick={() => {
                            if (!tab.disabled) setSelectedTabIndex(index)
                        }}>
                        {tab.label}
                    </div>
                )}
            </div>
            <div className="tab_selector_content">
                {children.map( (child, index) => {
                    return <div style={{display: index === selectedTabIndex ? 'block' : 'none'}}>{child}</div>
                } )}
            </div>
        </div>
    );
}

export default TabSelector;