import React from 'react';
import { useState } from 'react';
import './SideSelector.css';

function SideSelector({children, selectedIndex, onSelect}) {
	const [showExpand, setShowExpand] = useState(false);
	return (
		<div className="side_selector">
			<div className="side_selector_chosen">
				{children[selectedIndex]}
				<div onClick={() => setShowExpand(!showExpand)}>
					<img className="searchbar_chevron_icon" src="/assets/icons/chevron-compact-right.svg" alt="" />
				</div>
			</div>
			{showExpand && children.map( (child, index) => (
				<div key={index} className="searchbar_child" onClick={() => {
					setShowExpand(false);
					onSelect(index);
				}}>
					{child}
				</div>
			))}
		</div>
	);
}

export default SideSelector;