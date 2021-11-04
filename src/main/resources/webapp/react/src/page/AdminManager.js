import React from 'react';
import Header from '../components/header/Header';
import { useState } from 'react';
import Container from '../components/widgets/container/Container';
import FileManager from '../components/admin/file_manager/FileManager';
import CategoryManager from '../components/admin/category_manager/CategoryManager';

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
                <CategoryManager />
                <FileManager currentPath={currentPath} setCurrentPath={setCurrentPath} />
            </Container>
        </div>
    );
}

export default AdminManager;