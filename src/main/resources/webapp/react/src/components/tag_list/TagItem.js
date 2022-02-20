import React from 'react';
import { useState } from 'react';

function TagItem({index, tag, onSelect}) {
    const [isSelected, setSelected] = useState(false);

    const onSelectItem = () => {
        setSelected(!isSelected);
        onSelect(tag);
    }

    return (
        <span className={"tag_list_item_content_item " + (isSelected ? "tag_list_item_content_item_selected " : "")} onClick={onSelectItem}>{tag.value} </span>
    );
}

export default TagItem;