import React from 'react';
import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadRootAlbumInfo, changeMetaType, searchAlbums } from '../../model/reducers/albumSlice';
import TabSelector from '../widgets/tab_selector';
import SortField from '../widgets/sort_field';
import SubAlbums from './subalbums';
import ImageMedia from './media/image_media';
import VideoMedia from './media/video_media';
import AudioMedia from './media/audio_media';
import AlbumInfoModal  from './album/album_info/album_info_modal';
import './styles.css';

const iconClass = "album_tabs_sort_icon";

function AlbumsContainer({albumId, selectorClass}) {
  const dispatch = useDispatch();
  const { rootAlbum, albums, images, videos, audios, search } = useSelector(selectAlbums);

  const onSortField = (sort) => {
    if (search?.isSearch) dispatch(searchAlbums({ query: search.query, sort }));
    else dispatch(loadRootAlbumInfo({ albumId, sort }));
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
    if (albumId !== null) dispatch(loadRootAlbumInfo({ albumId, sort: { field: 'name', ascending: true } }));
  }, [dispatch, albumId]);

  const tabs = [{ label: "Albums", value: "albums", badgeLabel: albums.pageInfo.total, icon: "/assets/icons/journal-album.svg" }];
  if (images.pageInfo.total > 0) tabs.push({ label: "Images", value: "images", badgeLabel: images.pageInfo.total, icon: "/assets/icons/images.svg" });
  if (videos.pageInfo.total > 0) tabs.push({ label: "Videos", value: "videos", badgeLabel: videos.pageInfo.total, icon: "/assets/icons/film.svg" });
  if (audios.pageInfo.total > 0) tabs.push({ label: "Music",  value: "music", badgeLabel: audios.pageInfo.total, icon: '/assets/icons/music-note-list.svg' });

  const sortFields = [
    {name: "Unsorted", value: "unsorted", icon: "/assets/icons/journal-medical.svg", iconClass },
    {name: "Name", value: "name", icon: "/assets/icons/journal-text.svg", iconClass },
    {name: "Updated", value: "updatedAt", icon: "/assets/icons/journal-arrow-up.svg", iconClass }
  ]

  return (
    <TabSelector
        tabs={tabs}
        selectedTab={tabs.findIndex( tab => tab.value === rootAlbum?.metaType )}
        onChangeTab={onChangeTab}
        selectorClass={selectorClass}
        sideContent={
        <>
          {albumId < 1 && <SortField
              iconClass="albums_side_button albums_side_button_sort"
              dropdownClass="album_side_button_dropdown"
              onSortField={onSortField}
              values={sortFields} />}
          {albumId > 0 && <AlbumInfoModal album={rootAlbum} iconClass="albums_side_button" />}
        </>
      }>
      <SubAlbums albumId={albumId} />
      <ImageMedia albumId={albumId} onFullViewOpen={onHideTabPanel} />
      <VideoMedia albumId={albumId} />
      <AudioMedia albumId={albumId} />
    </TabSelector>
  );
}

export default AlbumsContainer;