import React from 'react';
import { useState } from 'react';
import './AlbumInfoTagsCategoryTagToken.css';

function AlbumInfoTagsCategoryTagToken({tagToken, even}) {
    return (
        <div className={"album_info_tags_category_tagToken_container " + (even ? "" : "album_info_tags_category_tagToken_container_odd" ) } >
            <span>{tagToken.value}</span>
        </div>
    );
}

export default AlbumInfoTagsCategoryTagToken;