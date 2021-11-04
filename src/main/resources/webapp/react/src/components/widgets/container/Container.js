import React from 'react';
import './Container.css';

function Container({children, isLoggedIn}) {
	return (
		<div className="container_content">
			{isLoggedIn && children}
		</div>
	);
}

export default Container