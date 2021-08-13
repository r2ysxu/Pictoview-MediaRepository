import React from 'react';
import { useState } from 'react';
import Header from '../components/header/Header';
import ImageAlbums from '../components/image_albums/ImageAlbums';
import Container from '../components/container/Container';

async function searchAlbums(query) {
    let searchParams = new URLSearchParams({query});
    return fetch('/album/image/search?' + searchParams.toString()).then( response => response.json());
}

function Album(props) {
    const [loggedIn, setLoggedIn] = useState(false);
    const [searchInput, setSearchInput] = useState('');
    const [albumId, setAlbumId] = useState(0);
    const [albums, setAlbums] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const onSearch = (value) => {
        setIsLoading(true);
        searchAlbums(value).then(data => {
            setAlbums(data);
            setIsLoading(false);
        });
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
                    isLoading={isLoading}
                    albums={albums}
                    images={[]}
                    albumId={albumId}
                    setAlbumId={setAlbumId}
                    setAlbums={setAlbums} />
            </Container>
        </div>
    );
}

export default Album;