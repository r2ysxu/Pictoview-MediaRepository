import React from 'react';
import { useState, useEffect, useCallback } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useLocation } from "react-router-dom";
import { selectUserLoggedIn } from '../model/reducers/userSlice';
import { searchAlbums } from '../model/reducers/albumSlice';
import Modal from '../components/widgets/modal/Modal';
import Container from '../components/widgets/container/Container';
import Header from '../components/header/Header';
import AlbumsContainer from '../components/albums/AlbumsContainer';
import TagList from '../components/tag_list/TagList';
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

    const isLoggedIn = useSelector(selectUserLoggedIn);
    const [showNewAlbumModal, setShowNewAlbumModal] = useState(false);
    const [showTagModal, setShowTagModal] = useState(false);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [tagQuery, setTagQuery] = useState({ and: new Map(), or: new Map(), not: new Map()});

    const onSearch = useCallback((query) => {
        dispatch(searchAlbums(query));
    }, [dispatch]);

    const onSearchRefresh = (query) => {
        const url = '/album?searchQuery=' + encodeURIComponent(query);
        window.location = url;
    }

    const hideNewAlbumModal = () => {
        setShowNewAlbumModal(false);
    }

    const hideTagModal = () => {
        setShowTagModal(false);
        setTagQuery({ and: new Map(), or: new Map(), not: new Map()});
    }

    const onMenuSelect = (isOpen) => {
        setIsMenuOpen(isOpen);
    }

    useEffect( () => {
        if (albumId === null) onSearch(albumSearchQuery);
    }, [albumId, albumSearchQuery, onSearch]);

    return (
        <div>
            <Header
                onSearchSubmit={onSearchRefresh}
                setShowNewAlbumModal={setShowNewAlbumModal}
                setShowTagModal={setShowTagModal}
                searchQuery={albumSearchQuery}
                onMenuSelect={onMenuSelect} />
            <Container isLoggedIn={isLoggedIn} containerClass={isMenuOpen ? "menubar_offset" : ""}>
                <Modal
                    content={<CreateAlbum onDone={hideNewAlbumModal} />}
                    isShown={showNewAlbumModal}
                    onHide={hideNewAlbumModal} />
                <Modal
                    content={<TagList tagQuery={tagQuery} />}
                    isShown={showTagModal}
                    onHide={hideTagModal} />
                <AlbumsContainer
                    albumId={albumId}
                    history={history}
                    selectorClass={"album_selector " + (isMenuOpen ? "album_selector_offset" : "") } />
            </Container>
        </div>
    );
}

export default AlbumPage;