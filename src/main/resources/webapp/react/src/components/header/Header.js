import React from 'react';
import { useState, useEffect } from 'react';
import LoginForm from '../login/LoginForm';
import Searchbar from '../widgets/searchbar/Searchbar';
import MenuBar from '../widgets/menubar/MenuBar';
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

function Header({setLoggedIn, searchInput, onSearchChange, onSearchSubmit}) {
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
                        </MenuBar>
                    }
                    searchInput={searchInput}
                    onSearchChange={onSearchChange}
                    onSearch={onSearchSubmit} />}
        </div>
    );
}

export default Header;