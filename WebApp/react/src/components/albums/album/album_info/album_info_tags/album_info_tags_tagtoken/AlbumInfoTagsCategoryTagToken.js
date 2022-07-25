import React from 'react';
import './AlbumInfoTagsCategoryTagToken.css';

const between = (value, beg, end) => {
    return value >= beg && value <= end;
}

function AlbumInfoTagsCategoryTagToken({tagToken}) {

    const relevanceTag = () => {
        const relevance = tagToken.relevance;
        if (between(relevance, 0, 25)) return "album_info_tags_category_tagToken_container40";
        if (between(relevance, 26, 50)) return "album_info_tags_category_tagToken_container60";
        if (between(relevance, 51, 75)) return "album_info_tags_category_tagToken_container80";
        else return "";
    }

    return (
        <div className={"album_info_tags_category_tagToken_container" } >
            <div className="album_info_tag_category_tagToken_dot" />
            <span className={relevanceTag()}>{tagToken.value}</span>
        </div>
    );
}

export default AlbumInfoTagsCategoryTagToken;