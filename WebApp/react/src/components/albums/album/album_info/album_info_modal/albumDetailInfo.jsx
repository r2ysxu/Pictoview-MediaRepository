import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { loadAlbumTags, updateAlbum, } from 'model/reducers/albumSlice';
import Modal from '../../../../widgets/modal';
import AlbumInfo  from '../../album_info';
import CreateMediaButton  from '../../../new_media/CreateMediaButton';
import AlbumRating from '../../album_rating';
import '../../../../widgets/common/Common.css';
import './styles.css';

function AlbumDetailInfo({ album, isEditing, onEdit }) {
  const dispatch = useDispatch();
    const [description, setDescription] = useState(album.description);

  const onUpdateAlbum = () => {
      dispatch(updateAlbum({
          id: album.id,
          description,
      }));
      onEdit(false);
  }

  useEffect( () => {
    dispatch(loadAlbumTags(album.id));
  }, []);

  return (
    <div className="album_info_modal_content">
        <h2>{album.albumName}</h2>
        <div className="album_info_button_img_container">
            {album.id === 0 ? <imge className="album_info_button_image" src="/assets/icons/image.svg" /> :
                <img className="album_info_button_image" src={'/album/image/cover?albumid=' + album.id} alt="" />}
        </div>
        <div className="album_info_button_options">
            <div className="album_info_buttons_option_rating">
                <AlbumRating album={album} />
            </div>
            <CreateMediaButton iconClass="album_info_button_add_button albums_side_button_add" />
        </div>
        <div className="album_info_modal_info">
            <AlbumInfo
                currentAlbum={true}
                album={album}
                isEditing={isEditing}
                setEditing={onEdit}
                description={description}
                setDescription={setDescription}
                onUpdateAlbum={onUpdateAlbum} />
        </div>
    </div>
  )
}

export default AlbumDetailInfo;