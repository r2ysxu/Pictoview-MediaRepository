import React, { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { loadCurrentAlbumTags, updateAlbum, selectAlbums } from '../../../../../model/reducers/albumSlice';
import Modal from '../../../../widgets/modal';
import AlbumInfo  from '../../album_info';
import CreateMediaButton  from '../../../new_media/CreateMediaButton';
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
                        <AlbumInfo
                            currentAlbum={true}
                            album={album}
                            isEditing={isEditing}
                            setEditing={setEditing}
                            description={description}
                            setDescription={setDescription}
                            onUpdateAlbum={onUpdateAlbum} />
                        <CreateMediaButton iconClass="albums_side_button albums_side_button_add" />
                    </div>
                }
            />
        </>
    );
}

export default AlbumInfoButton;