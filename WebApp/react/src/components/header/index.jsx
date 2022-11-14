import React from 'react';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { loadUserProfile, logout, toggleHeader, selectUser } from '../../model/reducers/userSlice';
import HomeBanner from '../home_banner';
import Searchbar from '../widgets/searchbar';
import MenuBar from '../widgets/menubar';
import MenuItem from '../widgets/menu_item';
import HeaderSearchSideBar from './HeaderSearchSidebar';
import './styles.css';

function Header({onSearchSubmit, setShowNewAlbumModal, setShowTagModal, searchQuery, onMenuSelect}) {
  const dispatch = useDispatch();
  const { isLoggedIn, showHeader } = useSelector(selectUser);

  const onLogout = () => {
    dispatch(logout());
    window.location.replace('/');
  }

  const onCollapseMenu = () => {
    if (showHeader) {
      document.getElementsByClassName('tab_selector_row')[0].classList.add('header_offset');
    } else {
      document.getElementsByClassName('tab_selector_row')[0].classList.remove('header_offset');
    }
    dispatch(toggleHeader({ showHeader: !showHeader }));
  }

  useEffect(()=> {
    if (!isLoggedIn) dispatch(loadUserProfile());
  }, [dispatch, isLoggedIn]);

  return (
    <div className={"header " + (showHeader ? "" : "header_hidden")}>
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
      <div className="header_collapse" onClick={onCollapseMenu}></div>
    </div>
  );
}

export default Header;