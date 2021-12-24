import React from 'react';
import { useState, useEffect, useCallback } from 'react';
import { useDispatch } from 'react-redux';
import { useLocation } from "react-router-dom";
import { searchAlbums } from '../model/reducers/albumSlice';
import Modal from '../components/widgets/modal/Modal';
import Container from '../components/widgets/container/Container';
import Header from '../components/header/Header';
import AlbumsContainer from '../components/albums/AlbumsContainer';
import CreateAlbum from '../components/albums/new_album/CreateAlbum';

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function AlbumPage(props) {

    const query = useQuery();
    const dispatch = useDispatch();
    const albumSearchQuery = decodeURIComponent(query.get('searchQuery') ?? '');
    const albumId = albumSearchQuery.length > 0 ? null : query.get('albumId') ?? 0;
    const history = decodeURIComponent(query.get('history') ?? '');

    const [loggedIn, setLoggedIn] = useState(false);
    const [showNewAlbumModal, setShowNewAlbumModal] = useState(false);
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const onSearch = useCallback((query) => {
        dispatch(searchAlbums(query));
    }, [dispatch]);

    const onSearchRefresh = (query) => {
        const url = '/album?searchQuery=' + encodeURIComponent(query);
        window.location = url;
    }

    const hideNewAlbumModal = (album) => {
        setShowNewAlbumModal(false);
    }

    const onMenuSelect = (isOpen) => {
        setIsMenuOpen(isOpen);
    }

    useEffect( () => {
        if (albumId === null) onSearch(albumSearchQuery);
    }, [albumId, albumSearchQuery, onSearch])

    return (
        <div>
            <Header
                setLoggedIn={setLoggedIn}
                onSearchSubmit={onSearchRefresh}
                setShowNewAlbumModal={setShowNewAlbumModal}
                searchQuery={albumSearchQuery}
                onMenuSelect={onMenuSelect} />
            <Container isLoggedIn={loggedIn} containerClass={isMenuOpen ? "menubar_offset" : ""}>
                <Modal
                    content={<CreateAlbum onDone={hideNewAlbumModal} />}
                    isShown={showNewAlbumModal}
                    onHide={hideNewAlbumModal} />
                <AlbumsContainer
                    albumId={albumId}
                    history={history}
                    selectorClass={"album_selector " + (isMenuOpen ? "album_selector_offset" : "") } />
            </Container>
        </div>
    );
}

export default AlbumPage;