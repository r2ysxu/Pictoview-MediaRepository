import React, { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { loadCurrentAlbumTags, updateAlbum, selectAlbums } from '../../../../../model/reducers/albumSlice';
import Modal from '../../../../widgets/modal';
import AlbumInfo  from '../../album_info';
import CreateMediaButton  from '../../../new_media/CreateMediaButton';
import AlbumRating from '../../album_rating';
import '../../../../widgets/common/Common.css';
import './styles.css';

function AlbumInfoButton({iconClass}) {
    const dispatch = useDispatch();
    const album = useSelector(selectAlbums);
    const [showMoreInfo, setShowMoreInfo] = useState(false);
    const [isEditing, setEditing] = useState(false);
    const [description, setDescription] = useState(album.description);

    const onUpdateAlbum = () => {
        dispatch(updateAlbum({
            id: album.id,
            description,
        }));
        setEditing(false);
    }

    const onShowMoreInfo = () => {
        setShowMoreInfo(true);
        dispatch(loadCurrentAlbumTags(album.id));
    }

    return (
        <>
            <img className={iconClass} src="/assets/icons/info-circle.svg" alt=""
                onClick={onShowMoreInfo} />
            <Modal
                className="album_info_modal_container"
                isShown={showMoreInfo}
                onHide={() => setShowMoreInfo(false)}
                content={
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
                                setEditing={setEditing}
                                description={description}
                                setDescription={setDescription}
                                onUpdateAlbum={onUpdateAlbum} />
                        </div>
                    </div>
                }
            />
        </>
    );
}

export default AlbumInfoButton;