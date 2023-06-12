import React from 'react';
import { useState } from 'react';
import DropdownSelector from './DropdownSelector';
import './styles.css';

function Dropdown({selectedValue = null, onSelect, values, placeholder, width, className = "", itemClassName}) {
  const initValue = values.find( elem => elem.value === selectedValue) || null;
  const [selectedValueItem, setSelectedValueItem] = useState(initValue);
  const [showSelector, setShowSelector] = useState(false);

  return (
    <div className={`dropdown_container ${className}`}>
      <div className="dropdown_selector_container" style={{ width, minWidth: width }}
          onClick={() => setShowSelector(!showSelector)}>
        <img className="dropdown_chevron" src={"/assets/icons/"+ (showSelector ? "chevron-up.svg" : "chevron-down.svg")} alt="dropdown" />
        {selectedValueItem === null ?
          <span className="dropdown_selector_placeholder_text"><i>{placeholder}</i></span> :
          <>
            <img className={`dropdown_item_icon ${selectedValueItem?.iconClass ?? "" }`} src={selectedValueItem.icon} alt="" />
            <span>{selectedValueItem.name}</span>
          </>
        }
      </div>
      {showSelector &&
        <DropdownSelector
            className={itemClassName}
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