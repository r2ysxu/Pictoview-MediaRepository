import React, { useState } from 'react';

function ManagerTab({title, children}) {
  const [isOpen, setOpen] = useState(true);

  const onToggleOpen = () => {
    setOpen(!isOpen)
  }

  return (
    <div className="admin_manager_tab_container">
      <div onClick={onToggleOpen} className="admin_manager_tab_title">
        {isOpen  && <img className="admin_manager_tab_title_icon" src="/assets/icons/chevron-up.svg" alt="collapse" />}
        {!isOpen && <img className="admin_manager_tab_title_icon" src="/assets/icons/chevron-down.svg" alt="expand" />}
        <div>{title}</div>
      </div>
      {isOpen && <div className="admin_manager_tab_content">
        {children}
      </div>}
    </div>
  )
}

export default ManagerTab;