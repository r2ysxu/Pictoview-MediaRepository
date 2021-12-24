import React from 'react';
import './Container.css';

function Container({children, isLoggedIn, containerClass = ''}) {
	return (
		<div className={"container_content " + containerClass }>
			{isLoggedIn && children}
		</div>
	);
}

export default Container