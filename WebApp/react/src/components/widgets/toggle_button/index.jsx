import React from 'react';
import './styles.css';

function ToggleButton({selectedValue, onSelect, label}) {
  return (
  <div className="toggle_button_container">
    <span className = "toggle_button_label">{label}</span>
    <label className="toggle_button_switch">
      <input type="checkbox"
        value={selectedValue}
        onChange={() => {onSelect(!selectedValue)}} />
      <span className="toggle_button_slider"></span>
    </label>
  </div>
  );
}

export default ToggleButton;