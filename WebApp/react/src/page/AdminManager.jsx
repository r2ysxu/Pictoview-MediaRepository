import React from 'react';
import Container from 'components/widgets/container';
import ManagerTabs from 'components/admin/manager_tabs';
import ManagerTab from 'components/admin/manager_tabs/manager_tab';
import CategoryManager from 'components/admin/category_manager/CategoryManager';
import NewAlbumPathManager from 'components/admin/NewAlbumPathManager';
import NewMediaPathManager from 'components/admin/NewMediaPathManager';
import GenerateAlbumInfoManager from 'components/admin/GenerateAlbumInfoManager';

function AdminManager() {
  return (
    <div>
      <Container isLoggedIn={true}>
        <ManagerTabs>
          <ManagerTab title="Category/Tags">
            <CategoryManager />
          </ManagerTab>
          <ManagerTab title="New">
            <NewAlbumPathManager />
            <NewMediaPathManager />
          </ManagerTab>
          <ManagerTab title="JSON">
            <GenerateAlbumInfoManager />
          </ManagerTab>
        </ManagerTabs>
      </Container>
    </div>
  );
}

export default AdminManager;