import React from 'react';
import { useState } from 'react';
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
    const albumIdQuery =  useQuery().get('albumId') || 0;
    const albumId = isNaN(albumIdQuery) ? 0 : albumIdQuery;
    const history = useQuery().get('history') || '';
    const dispatch = useDispatch();

    const [loggedIn, setLoggedIn] = useState(false);
    const [searchInput, setSearchInput] = useState('');
    const [showNewAlbumModal, setShowNewAlbumModal] = useState(false);

    const onSearch = (query) => {
        dispatch(searchAlbums(query));
    }

    const hideNewAlbumModal = (album) => {
        setShowNewAlbumModal(false);
    }

    return (
        <div>
            <Header
                setLoggedIn={setLoggedIn}
                searchInput={searchInput}
                onSearchChange={setSearchInput}
                onSearchSubmit={onSearch}
                setShowNewAlbumModal={setShowNewAlbumModal} />
            <Container isLoggedIn={loggedIn}>
                <Modal
                    content={<CreateAlbum onDone={hideNewAlbumModal} />}
                    isShown={showNewAlbumModal}
                    onHide={hideNewAlbumModal} />
                <AlbumsContainer
                    albumId={albumId}
                    history={history} />
            </Container>
        </div>
    );
}

export default AlbumPage;