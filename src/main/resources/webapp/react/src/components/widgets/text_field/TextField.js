import React from 'react';
import './TextField.css';

function TextField({name, label, value, onChange, type = "text"}) {
	return <input className="text_field" type={type} placeholder={label} name={name} value={value} onChange={onChange} />
}

export default TextField;