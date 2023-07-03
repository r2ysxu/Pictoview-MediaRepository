import React from 'react';
import { useState, useEffect, useRef } from 'react';
import './styles.css';

/* Token : [{ id: ID, value: String}] */
function Tokenizer({title, tokens, setTokens, addNewToken, autoCompleteValues, onAutoComplete, onRemove, onSave, onClose, relevanceClass}) {
  const inputRef = useRef(null);
  const [inputValue, setInputValue] = useState('');
  const [hoverIndex, setHoverIndex] = useState(0);

  useEffect(() => {
    inputRef.current.focus();
  }, []);

  const onAddToken = () => {
    let token = addNewToken(inputValue);
    if (inputValue.trim().length && !tokens.some( token => token.value === inputValue )) {
      setTokens([...tokens, token]);
    }
    setInputValue('');
    onAutoComplete('');
  }

  const onKeyDown = async (event) => {
    console.log('event', event);
    if (event.keyCode === 40) { // Down
      await setHoverIndex(Math.min(hoverIndex + 1, autoCompleteValues.length - 1));
      event.preventDefault();
    } else if(event.keyCode === 38) { // up
      await setHoverIndex(Math.max(0, hoverIndex - 1));
      event.preventDefault();
    } else if (event.keyCode === 9 && hoverIndex >= 0) { // Tab
      setInputValue(autoCompleteValues[hoverIndex]?.value ?? '');
      onAutoComplete('');
      event.preventDefault();
    } else if (event.keyCode === 13 && !event.shiftKey) { // Enter
      onAddToken();
      setHoverIndex(0);
    } else if (event.keyCode === 13 && event.shiftKey) { // Shift + Enter
      onSavePressed(event);
      setHoverIndex(0);
    } else if (event.keyCode === 27) { // Esc
      onDiscardPressed(event);
      setHoverIndex(0);
    }
  }

  const onSavePressed = (event) => {
    onSave();
    onDiscardPressed(event);
  }

  const onDiscardPressed = (event) => {
    onClose(event);
    setInputValue('');
    setTokens([]);
  }

  const onRemovePressed = (index) => {
    tokens.splice(index, 1);
    setTokens([...tokens]);
  }

  const onSelectItem = (index) => {
    setTokens([...tokens, autoCompleteValues[index]]);
    setInputValue('');
    onAutoComplete('');
    inputRef.current.focus();
  }

  return (
    <div className="tokenizer_container">
      <div className="tokenizer_input_container">
        <div className="tokenizer_input_autocomplete_container">
          {(autoCompleteValues || []).map((token, index) =>
            <div className={"tokenizer_input_autocomplete_item " + (hoverIndex === index ? "tokenizer_input_autocomplete_item_selected" : "")}
                key={index + '-' + token.id}
                onMouseEnter={() => setHoverIndex(index)}
                onClick={() => onSelectItem(index)}>{token.value}</div> )}
        </div>
        <input className="tokenizer_input_text"
            ref={inputRef}
            type="text"
            value={inputValue}
            onChange={(event) => {
              const value = event.target.value;
              setInputValue(value);
              onAutoComplete(value);
            }}
            onKeyDown={onKeyDown} />
        <button className="tokenizer_image_button tokenizer_input_image_save_button" onClick={onSavePressed}>
          <img src="/assets/icons/check.svg" alt="" />
        </button>
        <button className="tokenizer_image_button tokenizer_input_image_discard_button" onClick={(onDiscardPressed)}>
          <img src="/assets/icons/trash.svg" alt="" />
        </button>
      </div>
      <div className="tokenizer_tokens_container">
        {tokens.map( (token, index) =>
          <div key={index + '-' + token.id} className="tokenizer_token_container">
            <span className={relevanceClass(token.relevance)} >{token.value}</span>
            <div className="tokenizer_token_delete_icon_container" onClick={() => onRemovePressed(index)}>
              <img className="tokenizer_token_delete_icon" src="/assets/icons/x.svg" alt="" />
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Tokenizer;