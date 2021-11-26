import React from 'react';
import { useState, useEffect, useRef } from 'react';
import './Tokenizer.css';

/* Token : [{ id: ID, value: String}] */
function Tokenizer({title, tokens, setTokens, autoCompleteValues, onAutoComplete, onRemove, onSave, onClose}) {
    const inputRef = useRef(null);
    const [tagValue, setTagValue] = useState('');

    useEffect(() => {
        inputRef.current.focus();
    }, []);

    const onAddToken = () => {
        let token;
        if (autoCompleteValues &&
          autoCompleteValues.length &&
          autoCompleteValues[0].value === tagValue) {
            token = autoCompleteValues[0];
        } else {
           token = { id: null, value: tagValue };
        }
        if (tagValue.trim().length && !tokens.some( token => token.value === tagValue )) {
            setTokens([...tokens, token]);
        }
        setTagValue('');
        onAutoComplete('');
    }

    const onEnterPressed = (event) => {
        if (event.charCode === 13 && !event.shiftKey) { // Enter
            onAddToken();
        } else if (event.charCode === 13 && event.shiftKey) { // Shift + Enter
            onSave();
            onDiscardPressed(event);
        } else if (event.charCode === 96) { // `
            onDiscardPressed(event);
        }
    }

    const onDiscardPressed = (event) => {
        onClose(event);
        setTagValue('');
        setTokens([]);
    }

    const onRemovePressed = (index) => {
        tokens.splice(index, 1);
        setTokens([...tokens]);
    }

    return (
        <div className="tokenizer_container">
            <div className="tokenizer_input_container">
                <div className="tokenizer_input_autocomplete_container">
                    {(autoCompleteValues || []).map( (token, index) => <div key={index + '-' + token.id}>{token.value}</div> )}
                </div>
                <input className="tokenizer_input_text"
                    ref={inputRef}
                    type="text"
                    value={tagValue}
                    onChange={(event) => {
                        const value = event.target.value;
                        setTagValue(value);
                        onAutoComplete(value);
                    }}
                    onKeyPress={onEnterPressed} />
                <button className="tokenizer_image_button tokenizer_input_image_save_button" onClick={onSave}>
                    <img src="/assets/icons/check.svg" alt="" />
                </button>
                <button className="tokenizer_image_button tokenizer_input_image_discard_button" onClick={(onDiscardPressed)}>
                    <img src="/assets/icons/trash.svg" alt="" />
                </button>
            </div>
            <div className="tokenizer_tokens_container">
                {tokens.map( (token, index) =>
                    <div key={index + '-' + token.id} className="tokenizer_token_container">
                        <span>{token.value}</span>
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