import React from 'react';
import './styles.css';

function TabSelector({footerContent, selectorClass= "", sideContent, selectedTab, onChangeTab, tabs, children}) {
  return (
    <div className="tab_selector_container">
      <div className={"tab_selector_row tab_selector_row_container " + selectorClass}>
        <div className="tab_selector_row_left">
          <div className="tab_selector_tabs">
            {tabs.map( (tab, index) =>
              <div key={index}
                className={"tab_selector_button " +
                  (tab.disabled === true ? "tab_selector_button_disabled " : " ") +
                  (selectedTab === index ? "tab_selector_button_selected " : " ")}
                onClick={() => {if (!tab.disabled) onChangeTab(index)} }>
                  <span className="tab_selector_text">{tab.label}</span>
                  <img className="tab_selector_icon" src={tab.icon} alt={tab.label} title={tab.label} />
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
            return <div key={index} style={{display: index === selectedTab ? 'block' : 'none'}}>{child}</div>
        } )}
      </div>
    </div>
  );
}

export default TabSelector;