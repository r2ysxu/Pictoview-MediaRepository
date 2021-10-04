import React from 'react';
import { useState } from 'react';
import './ImageAlbumInfoTagsCategoryTagToken.css';

function ImageAlbumInfoTagsCategoryTagToken({tagToken, even}) {
    return (
        <div className={"image_album_info_tags_category_tagToken_container " + (even ? "" : "image_album_info_tags_category_tagToken_container_odd" ) } >
            <span>{tagToken.value}</span>
        </div>
    );
}

export default ImageAlbumInfoTagsCategoryTagToken;