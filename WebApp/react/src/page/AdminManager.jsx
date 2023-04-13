import React from 'react';
import Container from 'components/widgets/container';
import CategoryManager from 'components/admin/category_manager/CategoryManager';
import NewAlbumPathManager from 'components/admin/NewAlbumPathManager';
import NewMediaPathManager from 'components/admin/NewMediaPathManager';

function AdminManager() {
  return (
    <div>
      <Container isLoggedIn={true}>
        <CategoryManager />
        <NewAlbumPathManager />
        <NewMediaPathManager />
      </Container>
    </div>
  );
}

export default AdminManager;