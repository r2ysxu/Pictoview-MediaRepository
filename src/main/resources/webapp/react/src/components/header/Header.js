import React from 'react';
import { useState, useEffect } from 'react';
import { get_userProfile, get_logout } from '../../model/apis/user_apis';
import LoginForm from '../login/LoginForm';
import Searchbar from '../widgets/searchbar/Searchbar';
import MenuBar from '../widgets/menubar/MenuBar';
import MenuItem from '../widgets/menu_item/MenuItem';
import './Header.css';

const hasProfile = (userProfile) => {
    return userProfile !== null && userProfile !== undefined && userProfile.username && userProfile.username !== "";
}

function Header({setLoggedIn, searchInput, onSearchChange, onSearchSubmit, setShowNewAlbumModal}) {
    const [userProfile, setUserProfile] = useState(null);
    const [selectedIV, setSelectedIV] = useState(0);

    const onLogout = () => {
        get_logout().then(() => {
            window.location.replace('/');
        });
    }

    useEffect(()=> {
        get_userProfile().then(data => {
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
                        <MenuBar lastItem={
                            <MenuItem label={<img src="/assets/icons/box-arrow-up.svg" alt="" />} tooltip="Logout" onClick={onLogout} />
                        }>
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