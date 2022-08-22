import React from 'react';
import Container from '../components/widgets/container/Container';
import CategoryManager from '../components/admin/category_manager/CategoryManager';
import NewAlbumPathManager from '../components/admin/NewAlbumPathManager';

function AdminManager() {
    return (
        <div>
            <Container isLoggedIn={true}>
                <CategoryManager />
                <NewAlbumPathManager />
            </Container>
        </div>
    );
}

export default AdminManager;