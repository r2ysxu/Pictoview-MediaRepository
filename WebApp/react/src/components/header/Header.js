import React from 'react';
import { useState, useEffect } from 'react';
import { get_userProfile, get_logout } from '../../model/apis/user_apis';
import HomeBanner from '../home_banner/HomeBanner';
import Searchbar from '../widgets/searchbar/Searchbar';
import MenuBar from '../widgets/menubar/MenuBar';
import MenuItem from '../widgets/menu_item/MenuItem';
import './Header.css';

const hasProfile = (userProfile) => {
    return userProfile !== null && userProfile !== undefined && userProfile.username && userProfile.username !== "";
}

function Header({setLoggedIn, onSearchSubmit, setShowNewAlbumModal, setShowTagModal, searchQuery, onMenuSelect}) {
    const [userProfile, setUserProfile] = useState(null);

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
            {!hasProfile(userProfile) && <HomeBanner />}
            {hasProfile(userProfile) && <Searchbar
                    sideContent={
                        <MenuBar
                            onSelect={onMenuSelect}
                            footerItem={<MenuItem label={<img src="/assets/icons/box-arrow-up.svg" alt="" />} tooltip="Logout" onClick={onLogout} />}>
                            <MenuItem label={<img src="/assets/icons/house.svg" alt="" />} tooltip="Home" onClick={() => {window.location.replace("/")}} />
                            <MenuItem label={<img src="/assets/icons/journal-album.svg" alt="" />} tooltip="Albums" onClick={() => {window.location.replace("/album")}} />
                            <MenuItem label={<img src="/assets/icons/tags.svg" alt="" />} tooltip="Tags" onClick={() => {setShowTagModal(true)}} />
                            <MenuItem label={<img src="/assets/icons/folder-plus.svg" alt="" />} tooltip="New Album" onClick={() => {setShowNewAlbumModal(true)}} />
                        </MenuBar>
                    }
                    searchQuery={searchQuery}
                    onSearch={onSearchSubmit} />}
        </div>
    );
}

export default Header;