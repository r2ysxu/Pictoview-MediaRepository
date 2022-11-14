import React from 'react';
import { useState } from 'react';
import './styles.css';

function MenuItem({label, tooltip, onClick}) {
  const [showTooltip, setShowTooltip] = useState(false);

  return (
    <div className="menu_item_container"
          onMouseEnter={() => setShowTooltip(true)}
         onMouseLeave={() => setShowTooltip(false)}>
      <button className="menu_item_button" onClick={onClick}>
        {label}
      </button>
      {showTooltip && <div className="menu_item_tooltip">
        {tooltip}
      </div>}
    </div>
  );
}

export default MenuItem;