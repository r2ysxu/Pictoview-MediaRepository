import React from 'react';
import { useState, useEffect } from 'react';
import './TabSelector.css';

function TabSelector({footerContent, selectorClass= "", sideContent, defaultTab, tabs, children}) {
    const [selectedTabIndex, setSelectedTabIndex] = useState(0);

    useEffect(() => {
        setSelectedTabIndex(tabs.findIndex( tab => tab.value === defaultTab ) || 0);
    }, [setSelectedTabIndex, tabs, defaultTab]);

    return (
        <div className="tab_selector_container">
            <div className={"tab_selector_row tab_selector_row_container " + selectorClass}>
                <div className="tab_selector_row_left">
                    <div className="tab_selector_tabs">
                        {tabs.map( (tab, index) =>
                            <div key={index}
                              className={"tab_selector_button " +
                                    (tab.disabled === true ? "tab_selector_button_disabled " : " ") +
                                    (selectedTabIndex === index ? "tab_selector_button_selected " : " ")}
                              onClick={() => {if (!tab.disabled) setSelectedTabIndex(index)} }>
                                {tab.label}
                                {tab.badgeLabel !== undefined && <div className="tab_selector_badge">{tab.badgeLabel}</div>}
                            </div>
                        )}
                    </div>
                    <div className="tab_selector_footer">
                        {footerContent}
                    </div>
                </div>
                <div className="tab_selector_side_content">
                    {sideContent}
                </div>
            </div>
            <div className="tab_selector_content">
                {children.map( (child, index) => {
                    return <div key={index} style={{display: index === selectedTabIndex ? 'block' : 'none'}}>{child}</div>
                } )}
            </div>
        </div>
    );
}

export default TabSelector;