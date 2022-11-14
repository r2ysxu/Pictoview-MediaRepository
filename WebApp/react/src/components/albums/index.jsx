import React from 'react';
import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadCurrentAlbumInfo, changeMetaType } from '../../model/reducers/albumSlice';
import TabSelector from '../widgets/tab_selector';
import SortField from '../widgets/sort_field';
import Breadcrumbs from '../breadcrumbs';
import SubAlbums from './subalbums';
import ImageMedia from './media/image_media';
import VideoMedia from './media/video_media';
import AudioMedia from './media/audio_media';
import AlbumInfoButton  from './album/album_info/album_info_button';
import './styles.css';

function AlbumsContainer({albumId, history, selectorClass}) {
  const dispatch = useDispatch();
  const { metaType, albumName, albums, images, videos, audios} = useSelector(selectAlbums);

  const changeCurrentAlbum = (id, openNew) => {
    const newHistory = history.length === 0 ? [] : history.split(',');
    newHistory.push(albumId);
    const url = '/album?albumId=' + encodeURIComponent(id) + '&history=' + encodeURIComponent(newHistory.join(','));
    if(!openNew) window.location = url;
    else window.open(url, '_blank');
    return false;
  }

  const onSortField = (sort) => {
    dispatch(loadCurrentAlbumInfo({ albumId, sort }));
  }

  const onChangeTab = (index) => {
    dispatch(changeMetaType({ metaType: tabs[index].value }));
  }

  const onHideTabPanel = (showPanel) => {
    if (showPanel) {
      document.getElementsByClassName('tab_selector_row')[0].classList.add('album_tabs_panel_hidden');
    } else {
      document.getElementsByClassName('tab_selector_row')[0].classList.remove('album_tabs_panel_hidden');
    }
  }

  useEffect(()=> {
    if (albumId !== null) dispatch(loadCurrentAlbumInfo({ albumId, sort: { field: 'name', ascending: true } }));
  }, [dispatch, albumId]);

  const tabs = [
    {label: "Albums", value: "albums", badgeLabel: albums.pageInfo.total},
    {label: "Images", value: "images", badgeLabel: images.pageInfo.total},
    {label: "Videos", value: "videos", badgeLabel: videos.pageInfo.total},
    {label: "Music",  value: "music", badgeLabel: audios.pageInfo.total},
  ]

  const sortFields = [
    {name: "Unsorted", value: "unsorted"},
    {name: "Name", value: "name"},
    {name: "Updated", value: "updatedAt"}
  ]

  return (
    <TabSelector
        tabs={tabs}
        selectedTab={tabs.findIndex( tab => tab.value === metaType )}
        onChangeTab={onChangeTab}
        selectorClass={selectorClass}
        footerContent={<Breadcrumbs history={history} current={albumName} />}
        sideContent={
        <div>
          {albumId === 0 && <SortField
              iconClass="albums_side_button albums_side_button_sort"
              dropdownClass="album_side_button_dropdown"
              onSortField={onSortField}
              values={sortFields} />}
          {albumId > 0 && <div>
            <AlbumInfoButton iconClass="albums_side_button" />
          </div>}
        </div>
      }>
      <SubAlbums albumId={albumId} changeCurrentAlbum={changeCurrentAlbum}/>
      <ImageMedia albumId={albumId} onFullViewOpen={onHideTabPanel} />
      <VideoMedia albumId={albumId} />
      <AudioMedia albumId={albumId} />
    </TabSelector>
  );
}

export default AlbumsContainer;