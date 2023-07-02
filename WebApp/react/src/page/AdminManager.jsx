import React from 'react';
import Container from 'components/widgets/container';
import CategoryManager from 'components/admin/category_manager/CategoryManager';
import NewAlbumPathManager from 'components/admin/NewAlbumPathManager';
import NewMediaPathManager from 'components/admin/NewMediaPathManager';
import GenerateAlbumInfoManager from 'components/admin/GenerateAlbumInfoManager';

function AdminManager() {
  return (
    <div>
      <Container isLoggedIn={true}>
        <CategoryManager />
        <NewAlbumPathManager />
        <NewMediaPathManager />
        <GenerateAlbumInfoManager />
      </Container>
    </div>
  );
}

export default AdminManager;