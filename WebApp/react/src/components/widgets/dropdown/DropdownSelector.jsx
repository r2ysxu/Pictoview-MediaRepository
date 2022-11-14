import React from 'react';
import './styles.css';

function DropdownSelector({setSelectedValueItem, onSelect, values, onClose, width}) {
  return (
    <div className="dropdown_float_container" style={{ width, minWidth: width }}>
      {values.map( (value, index) => 
        <div key={index} className="dropdown_float_item"
          onClick={() => {
            setSelectedValueItem(values[index]);
            onSelect(values[index]);
            onClose();
          }}>
          {value.name}
        </div>
      )}
    </div>
  );
}

export default DropdownSelector;