import React from 'react';
import { useState } from 'react';
import './Searchbar.css';

function Searchbar({onSearch, searchInput, onSearchChange, sideContent}) {

    const onSubmit = (event) => {
        event.preventDefault();
        onSearch(searchInput);
        onSearchChange('');
        return false;
    }

    return (
        <div className="searchbar">
            {sideContent}
            <form className="searchbar_context" onSubmit={onSubmit}>
                <input className="searchbar_input_text"
                    type="text"
                    value={searchInput}
                    onChange={(event) => {onSearchChange(event.target.value)}} />
            </form>
        </div>
    );
}

export default Searchbar;