import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { searchAlbums } from '../model/reducers/albumSlice';
import Header from '../components/header/Header';
import Albums from '../components/albums/Albums';
import Container from '../components/container/Container';

function AlbumPage(props) {
    const dispatch = useDispatch();
    const [loggedIn, setLoggedIn] = useState(false);
    const [searchInput, setSearchInput] = useState('');
    const [albumHistory, setAlbumHistory] = useState([0]);

    const onSearch = (query) => {
        dispatch(searchAlbums(query));
    }

    return (
        <div>
            <Header
                setLoggedIn={setLoggedIn}
                searchInput={searchInput}
                onSearchChange={setSearchInput}
                onSearchSubmit={onSearch} />
            <Container isLoggedIn={loggedIn}>
                <Albums
                    albumHistory={albumHistory}
                    setAlbumHistory={setAlbumHistory} />
            </Container>
        </div>
    );
}

export default AlbumPage;