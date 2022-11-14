import React from 'react';
import './styles.css';

function HeaderSearchSidebar({setShowTagModal}) {

  return (
    <div className="header_sidebar_container">
      <div className="header_sidebar_tag_icon" onClick={() => {setShowTagModal(true)}}>
        <img src="/assets/icons/tags.svg" alt="" />
      </div>
    </div>
  );
}

export default HeaderSearchSidebar;