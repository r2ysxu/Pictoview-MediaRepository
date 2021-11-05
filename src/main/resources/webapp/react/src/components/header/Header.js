import React from 'react';
import { useState, useEffect } from 'react';
import LoginForm from '../login/LoginForm';
import Searchbar from '../widgets/searchbar/Searchbar';
import MenuBar from '../widgets/menubar/MenuBar';
import MenuItem from '../widgets/menu_item/MenuItem';
import './Header.css';

async function fetchUserProfile() {
    const userProfileRequest = fetch('/profile', {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
        }).then( response => response.json());
    return userProfileRequest;
}

const hasProfile = (userProfile) => {
    return userProfile !== null && userProfile !== undefined && userProfile.username && userProfile.username !== "";
}

function Header({setLoggedIn, searchInput, onSearchChange, onSearchSubmit, setShowNewAlbumModal}) {
    const [userProfile, setUserProfile] = useState(null);
    const [selectedIV, setSelectedIV] = useState(0);

    useEffect(()=> {
        fetchUserProfile().then(data => {
            if (hasProfile(data) && !hasProfile(userProfile)) {
                setUserProfile(data)
                setLoggedIn(true);
            }
        });
    }, [userProfile, setLoggedIn, setUserProfile]);

    return (
        <div className="header">
            {!hasProfile(userProfile) && 
                <>
                    <div className="searchbar searchbar_context"></div>
                    <LoginForm onLoggedIn={setLoggedIn} />
                </>}
            {hasProfile(userProfile) && 
                <Searchbar 
                    sideContent={
                        <MenuBar>
                            <MenuItem label={<img src="/assets/icons/house.svg" alt="" />} tooltip="Home" onClick={() => {window.location.replace("/")}} />
                            <MenuItem label={<img src="/assets/icons/journal-album.svg" alt="" />} tooltip="Albums" onClick={() => {window.location.replace("/album")}} />
                            <MenuItem label={<img src="/assets/icons/folder-plus.svg" alt="" />} tooltip="New Album" onClick={() => {setShowNewAlbumModal(true)}} />
                        </MenuBar>
                    }
                    searchInput={searchInput}
                    onSearchChange={onSearchChange}
                    onSearch={onSearchSubmit} />}
        </div>
    );
}

export default Header;