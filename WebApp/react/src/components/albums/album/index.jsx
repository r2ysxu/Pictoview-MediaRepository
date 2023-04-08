import React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { loadAlbumTags, updateAlbum, deleleAlbum } from '../../../model/reducers/albumSlice';
import AlbumInfo from './album_info';
import AlbumRating from './album_rating';
import './styles.css';

function Album({album, onChangeAlbum}) {
  const dispatch = useDispatch();
  const [showMoreInfo, setShowMoreInfo] = useState(false);
  const [isEditing, setEditing] = useState(false);
  const [currentAlbum, setCurrentAlbum] = useState(album);

  const generateAlbumLink = () => {
    return '/album?albumId=' + encodeURIComponent(album.id) + '&history=' + encodeURIComponent([]);
  }

  const onShowMoreInfo = (albumId) => {
    if (showMoreInfo === false) {
      setShowMoreInfo(true);
      loadTags(albumId);
    } else if (isEditing === false) {
      setShowMoreInfo(false);
    }
  }

  const loadTags = (albumId) => {
    dispatch(loadAlbumTags(albumId));
  }

  const onUpdateAlbum = () => {
    dispatch(updateAlbum({
      id: album.id,
      name: currentAlbum.name,
      publisher: currentAlbum.publisher,
      description: currentAlbum.description,
    }));
    setEditing(false);
  }

  const onDeleteAlbum = (event) => {
    if (event.ctrlKey && event.shiftKey) {
      dispatch(deleleAlbum({
        albumId: album.id,
      }));
    }
  }

  const setDescription = (value) => {
    setCurrentAlbum({...currentAlbum, description: value});
  }

  let albumNameSizeClass = "";
  if (currentAlbum.name.length > 80) albumNameSizeClass = "album_title_120";
  if (currentAlbum.name.length > 80) albumNameSizeClass = "album_title_80";
  else if (currentAlbum.name.length > 20) albumNameSizeClass = "album_title_20";

  let albumMetaTypeClass = "";
  switch(album.metaType) {
    case 'images': albumMetaTypeClass = 'album_banner_images'; break;
    case 'videos': albumMetaTypeClass = 'album_banner_video'; break;
    default: break;
  }

  return (
    <div className="album_container">
      <div className={"album_banner " + albumMetaTypeClass} onClick={() => onShowMoreInfo(album.id)}>
        {!isEditing && <h4>{album.publisher}</h4>}
        {isEditing && <input className="album_text_field album_publisher_text_field" placeholder="Publisher" value={currentAlbum.publisher} onChange={ (event) => setCurrentAlbum({...currentAlbum, publisher: event.target.value}) } />}
      </div>
      <div className="album_center_container">
        <a href={generateAlbumLink()} className="album_wrapper">
            {album.id === 0 ? 
              <imge className="album_image" src="/assets/icons/image.svg" /> :
              <img className="album_image" src={'/album/image/cover?albumid=' + album.id} alt="" />}
        </a>
        {showMoreInfo && <AlbumInfo
          album={album}
          isEditing={isEditing}
          setEditing={setEditing}
          description={currentAlbum.description}
          setDescription={setDescription}
          onUpdateAlbum={onUpdateAlbum}
          onDeleteAlbum={onDeleteAlbum} />}
      </div>
      <div className="album_title">
        <AlbumRating album={album} />
        {!isEditing && <h2 className={albumNameSizeClass} title={album.altname}>{album.name}</h2>}
        {isEditing && <input className="album_text_field album_name_text_field" type="text" placeholder="Name" value={currentAlbum.name} onChange={ (event) => setCurrentAlbum({...currentAlbum, name: event.target.value}) } />}
      </div>
    </div>
  );
}

export default Album;