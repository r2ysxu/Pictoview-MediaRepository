import React from 'react';
import { useState } from 'react';
import './Searchbar.css';

function Searchbar({onSearch, searchQuery = '', menuContent, sideContent}) {
    const [searchInput, setSearchInput] = useState(searchQuery);

    const onSubmit = (event) => {
        event.preventDefault();
        onSearch(searchInput);
        setSearchInput('');
        return false;
    }

    return (
        <div className="searchbar">
            {menuContent}
            <form className="searchbar_context" onSubmit={onSubmit}>
                <input className="searchbar_input_text"
                    type="text"
                    value={searchInput}
                    onChange={(event) => {setSearchInput(event.target.value)}} />
            </form>
            {sideContent}
        </div>
    );
}

export default Searchbar;