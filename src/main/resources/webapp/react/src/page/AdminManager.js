import React from 'react';
import Header from '../components/header/Header';
import { useState } from 'react';
import Container from '../components/container/Container';
import FileManager from '../components/admin/file_manager/FileManager';

function AdminManager(props) {
    const [loggedIn, setLoggedIn] = useState(false);
    const [currentPath, setCurrentPath] = useState("");
    return (
        <div>
            <Header
                setLoggedIn={setLoggedIn}
                searchInput={currentPath}
                onSearchChange={() => {}}
                onSearchSubmit={setCurrentPath} />
            <Container isLoggedIn={loggedIn}>
                <FileManager currentPath={currentPath} setCurrentPath={setCurrentPath} />
            </Container>
        </div>
    );
}

export default AdminManager;