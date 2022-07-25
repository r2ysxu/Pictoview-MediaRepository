import React from 'react';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { loadUserProfile, logout, selectUserLoggedIn } from '../../model/reducers/userSlice';
import HomeBanner from '../home_banner/HomeBanner';
import Searchbar from '../widgets/searchbar/Searchbar';
import MenuBar from '../widgets/menubar/MenuBar';
import MenuItem from '../widgets/menu_item/MenuItem';
import HeaderSearchSideBar from './HeaderSearchSidebar';
import './Header.css';

function Header({onSearchSubmit, setShowNewAlbumModal, setShowTagModal, searchQuery, onMenuSelect}) {
    const dispatch = useDispatch();
    const isLoggedIn = useSelector(selectUserLoggedIn);

    const onLogout = () => {
        dispatch(logout());
        window.location.replace('/');
    }

    useEffect(()=> {
        if (!isLoggedIn) dispatch(loadUserProfile());
    }, [dispatch, isLoggedIn]);

    return (
        <div className="header">
            {!isLoggedIn && <HomeBanner />}
            {isLoggedIn && <Searchbar
                    menuContent={
                        <MenuBar
                            onSelect={onMenuSelect}
                            footerItem={<MenuItem label={<img src="/assets/icons/box-arrow-up.svg" alt="" />} tooltip="Logout" onClick={onLogout} />}>
                            <MenuItem label={<img src="/assets/icons/house.svg" alt="" />} tooltip="Home" onClick={() => {window.location.replace("/")}} />
                            <MenuItem label={<img src="/assets/icons/journal-album.svg" alt="" />} tooltip="Albums" onClick={() => {window.location.replace("/album")}} />
                            <MenuItem label={<img src="/assets/icons/folder-plus.svg" alt="" />} tooltip="New Album" onClick={() => {setShowNewAlbumModal(true)}} />
                        </MenuBar>
                    }
                    sideContent={
                        <HeaderSearchSideBar setShowTagModal={setShowTagModal} />
                    }
                    searchQuery={searchQuery}
                    onSearch={onSearchSubmit} />}
        </div>
    );
}

export default Header;