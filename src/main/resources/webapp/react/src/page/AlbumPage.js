import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { searchAlbums } from '../model/reducers/albumSlice';
import Modal from '../components/widgets/modal/Modal';
import Container from '../components/widgets/container/Container';
import Header from '../components/header/Header';
import AlbumsContainer from '../components/albums/AlbumsContainer';
import NewAlbum from '../components/albums/new_album/NewAlbum';

function AlbumPage(props) {
    const dispatch = useDispatch();
    const [loggedIn, setLoggedIn] = useState(false);
    const [searchInput, setSearchInput] = useState('');
    const [albumHistory, setAlbumHistory] = useState([{id: 0, name: ""}]);
    const [showNewAlbumModal, setShowNewAlbumModal] = useState(true);

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
                onSearchSubmit={onSearch} />
            <Container isLoggedIn={loggedIn}>
                <Modal
                    content={<NewAlbum onDone={hideNewAlbumModal} />}
                    isShown={showNewAlbumModal}
                    onHide={hideNewAlbumModal} />

                <AlbumsContainer
                    albumHistory={albumHistory}
                    setAlbumHistory={setAlbumHistory} />
            </Container>
        </div>
    );
}

export default AlbumPage;