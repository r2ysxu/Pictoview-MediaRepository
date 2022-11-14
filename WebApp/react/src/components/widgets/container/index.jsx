import React from 'react';
import './styles.css';

function Container({children, isLoggedIn, containerClass = ''}) {
  return (
    <div className={"container_content " + containerClass }>
      {isLoggedIn && children}
    </div>
  );
}

export default Container