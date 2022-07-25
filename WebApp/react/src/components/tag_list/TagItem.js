import React from 'react';
import { useState } from 'react';

const Type = { NONE: 'NONE', AND : 'AND', NOT: 'NOT', OR: 'OR' };

const selectClassNameByType = (type) => {
    switch(type) {
        case Type.NONE: return "";
        case Type.AND: return "tag_list_item_content_item_selected_and";
        case Type.OR: return "tag_list_item_content_item_selected_or";
        case Type.NOT: return "tag_list_item_content_item_selected_not";
        default: return "";
    }
}

function TagItem({index, tag, onSelect}) {
    const [isSelected, setSelected] = useState(Type.NONE);

    const onSelectItem = (event) => {
        let type = Type.AND;
        if (event.altKey) type = Type.NOT;
        else if (event.ctrlKey) type = Type.OR;
        setSelected(isSelected === Type.NONE ? type : Type.NONE);
        onSelect(tag, type);
    }

    return (
        <span className={"tag_list_item_content_item " + selectClassNameByType(isSelected)} onClick={onSelectItem}>{tag.value} </span>
    );
}

export default TagItem;