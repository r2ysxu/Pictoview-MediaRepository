import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { searchAlbums } from '../model/reducers/albumSlice';
import Header from '../components/header/Header';
import ImageAlbums from '../components/image_albums/ImageAlbums';
import Container from '../components/container/Container';

function Album(props) {
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
                <ImageAlbums
                    albumHistory={albumHistory}
                    setAlbumHistory={setAlbumHistory} />
            </Container>
        </div>
    );
}

export default Album;