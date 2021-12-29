import React from 'react';
import { useState } from 'react';
import TagItem from './TagItem';

function TagListItem({index, category, onSelectCategory, onSelectTag}) {
    const [showExpand, setShowExpand] = useState(false);

    const onExpand = () => {
        onSelectCategory(category.id);
        setShowExpand(!showExpand);
    }

    const onSelectItem = (tag) => {
        onSelectTag(category, tag);
    }

    return (
        <div className="tag_list_item_container">
            <div className={"tag_list_header_container " + (index === 0 ? "tag_list_header_container_first " : "")} 
                onClick={onExpand}>
                <h3>{category.name}</h3>
            </div>
            {showExpand &&
                <div className="tag_list_item_content">
                    {(category.tags || []).map( tag => <TagItem key={`${category.id}_${tag.id}`} tag={tag} onSelect={onSelectItem} /> )}
                </div>
            }
        </div>
    );
}

export default TagListItem;