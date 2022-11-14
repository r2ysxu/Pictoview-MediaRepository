import React from 'react';
import { useState } from 'react';
import { post_createCategory } from '../../../model/apis/tag_apis';

function CategoryManager() {
  const [categoryName, setCategoryName] = useState('');

  const onCreateCategory = (event) => {
    post_createCategory(categoryName)
      .then(() => {
        setCategoryName('');
      });
  }

  return (
    <div>
      <input type="text"
        value={categoryName}
        onChange={(event) => {
          const value = event.target.value;
          setCategoryName(value);
        }}
        placeholder="Category" />
      <button onClick={onCreateCategory}>Create Tag</button>
    </div>
  )
}

export default CategoryManager;