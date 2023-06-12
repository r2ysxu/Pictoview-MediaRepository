import React, { useEffect, useRef } from 'react';
import './styles.css';

function DropdownSelector({ setSelectedValueItem, onSelect, values, onClose, width, className = "" }) {
  const ref = useRef();

  useEffect(() => {
    const onOutsideClicked = (event) => {
      if (ref.current && !ref.current.contains(event.target)) {
        onClose();
      }
    };
    document.addEventListener("mousedown", onOutsideClicked);
    return () => document.removeEventListener("mousedown", onOutsideClicked);
  });

  const onHandleClose = () => {
    onClose();
  }

  return (
    <>
      <div className="dropdown_modal" onClick={onHandleClose} />
      <div className="dropdown_float_container" ref={ref}>
        {values.map( (value, index) => 
          <div key={index} className={`dropdown_float_item ${className}`}
            onClick={() => {
              setSelectedValueItem(values[index]);
              onSelect(values[index]);
              onClose();
            }}>
            {value.name}
          </div>
        )}
      </div>
    </>
  );
}

export default DropdownSelector;