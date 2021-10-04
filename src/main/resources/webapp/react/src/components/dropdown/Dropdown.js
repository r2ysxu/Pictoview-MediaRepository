import React from 'react';
import { useState } from 'react';
import './Dropdown.css';

function Dropdown({selectedValue, onSelect, values, placeholder, width}) {
    const initValue = values.find( elem => elem.value === selectedValue) || null;
    const [selectedValueItem, setSelectedValueItem] = useState(initValue);
    const [showSelector, setShowSelector] = useState(false);

    return (
        <div className="dropdown_container">
            <div className="dropdown_selector_container" style={{ width, minWidth: width }}
              onClick={() => setShowSelector(!showSelector)}>
                <img className="dropdown_icon" src="/assets/icons/chevron-down.svg" />
                {selectedValueItem === null ?
                    <span className="dropdown_selector_placeholder_text"><i>{placeholder}</i></span> :
                    <span>{selectedValueItem.name}</span>
                }
            </div>
            {showSelector &&
            <div className="dropdown_float_container" style={{ width, minWidth: width }}>
                {values.map( (value, index) => 
                    <div key={index} className="dropdown_float_item"
                      onClick={() => {
                        setSelectedValueItem(values[index]);
                        setShowSelector(false);
                        onSelect(values[index]);
                      }}>
                        {value.name}
                    </div>
                )}
            </div>}
        </div>
    );
}

export default Dropdown;