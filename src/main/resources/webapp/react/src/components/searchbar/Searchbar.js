import React from 'react';
import { useState } from 'react';
import SideSelector from '../side_selector/SideSelector';
import './Searchbar.css';

function Searchbar({onSearch, searchInput, onSearchChange}) {
    const [selectedIV, setSelectedIV] = useState(0);

    const onSubmit = (event) => {
        event.preventDefault();
        console.log(searchInput);
        onSearch(searchInput);
        onSearchChange('');
        return false;
    }

    return (
        <div className="searchbar">
            <SideSelector selectedIndex={selectedIV} onSelect={setSelectedIV}>
                <img className="searchbar_iv_icon" src="/assets/icons/image.svg" alt="" />
                <img className="searchbar_iv_icon" src="/assets/icons/film.svg" alt="" />
            </SideSelector>
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