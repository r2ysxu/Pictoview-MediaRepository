import React from 'react';
import { useState, useEffect, useRef } from 'react';
import './Tokenizer.css';

/* Token : [{ id: ID, value: String}] */
function Tokenizer({title, tokens, setTokens, addNewToken, autoCompleteValues, onAutoComplete, onRemove, onSave, onClose, relevanceClass}) {
    const inputRef = useRef(null);
    const [inputValue, setInputValue] = useState('');

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

    const onEnterPressed = (event) => {
        if (event.charCode === 13 && !event.shiftKey) { // Enter
            onAddToken();
        } else if (event.charCode === 13 && event.shiftKey) { // Shift + Enter
            onSavePressed(event);
        } else if (event.charCode === 96) { // `
            onDiscardPressed(event);
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
                    {(autoCompleteValues || []).map( (token, index) => <div className="tokenizer_input_autocomplete_item" key={index + '-' + token.id} onClick={() => onSelectItem(index)}>{token.value}</div> )}
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
                    onKeyPress={onEnterPressed} />
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