import React, { useState } from 'react';
import Modal from 'components/widgets/modal';
import AlbumDetailInfo from './albumDetailInfo';
import './styles.css';

function AlbumInfoModal({ album, iconClass }) {
  const [showMoreInfo, setShowMoreInfo] = useState(false);
  const [isEditing, setEditing] = useState(false);

  const onShowMoreInfo = () => {
      setShowMoreInfo(true);
  }

  if (album === null) return <div />

  return (
    <div>
      <img className={iconClass} src="/assets/icons/info-circle.svg" alt=""
        onClick={onShowMoreInfo} />
      <Modal
        className="album_info_modal_container"
        isShown={showMoreInfo}
        onHide={() => setShowMoreInfo(false)}
        content={<AlbumDetailInfo album={album} isEditing={isEditing} onEdit={setEditing} />}
      />
    </div>
  );
}

export default AlbumInfoModal;