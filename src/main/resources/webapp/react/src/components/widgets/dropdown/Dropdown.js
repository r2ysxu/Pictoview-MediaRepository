import React from 'react';
import { useState } from 'react';
import DropdownSelector from './DropdownSelector';
import './Dropdown.css';

function Dropdown({selectedValue = null, onSelect, values, placeholder, width, className = ""}) {
    const initValue = values.find( elem => elem.value === selectedValue) || null;
    const [selectedValueItem, setSelectedValueItem] = useState(initValue);
    const [showSelector, setShowSelector] = useState(false);

    return (
        <div className={`dropdown_container ${className}`}>
            <div className="dropdown_selector_container" style={{ width, minWidth: width }}
              onClick={() => setShowSelector(!showSelector)}>
                <img className="dropdown_icon" src={"/assets/icons/"+ (showSelector ? "chevron-up.svg" : "chevron-down.svg")} alt="dropdown" />
                {selectedValueItem === null ?
                    <span className="dropdown_selector_placeholder_text"><i>{placeholder}</i></span> :
                    <span>{selectedValueItem.name}</span>
                }
            </div>
            {showSelector &&
                <DropdownSelector
                    setSelectedValueItem={setSelectedValueItem}
                    onSelect={onSelect}
                    values={values}
                    onClose={() => setShowSelector(false)}
                    width={width} />
            }
        </div>
    );
}

export default Dropdown;