import React from 'react';
import { useState } from 'react';
import Dropdown from '../dropdown';
import './styles.css';

function SortField({iconClass, dropdownClass, onSortField, values}) {
  const [ascending, setAscending] = useState(true);
  const [selectedValue, setSelectedValue] = useState(values[0]);

  const onSort = (field) => {
    setSelectedValue(field.value);
    onSortField({ field: field.value, ascending });
  }

  return (
    <div className="sort_field_container">
      <img className={iconClass} src={ascending ? "/assets/icons/sort-alpha-up.svg" : "/assets/icons/sort-alpha-down.svg" } alt="" onClick={() => setAscending(!ascending)} />
      <Dropdown
          className={dropdownClass}
          selectedValue={selectedValue}
          onSelect={onSort}
          values={values}
          placeholder="Sorting"
          width="100px" />
    </div>
  );
}

export default SortField;