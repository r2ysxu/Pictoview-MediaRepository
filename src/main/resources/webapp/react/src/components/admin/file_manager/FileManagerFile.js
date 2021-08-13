import React from 'react';
import { useState } from 'react';
import './FileManager.css';

async function postImageDirectories(path, name) {
    const data = { path, name };
    const listDirectoryRequest = fetch('/admin/files/add/image', {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then( response => response.json());
    return listDirectoryRequest;
}

function FileManagerFile({fileInfo, onSetPath}) {
	const [isLoading, setIsLoading] = useState(false);

	const addImageDirectories = (event) => {
		event.preventDefault();
		event.stopPropagation();
		setIsLoading(true);
		postImageDirectories(fileInfo.path, fileInfo.name).then(() => setIsLoading(false));
	};

	return (
		<div className="file_manager_file" onClick={() => {onSetPath(fileInfo.name)}}>
			<div>
				<img className="file_manager_file_icon" src="/assets/icons/folder.svg" alt="" />
				<span>{fileInfo.name}</span>
			</div>
			<div>
				<button onClick={(event) => addImageDirectories(event)} disabled={isLoading} >
					<img src="/assets/icons/image.svg" alt="Create Album" />
				</button>
				<img className={'file_manager_scan_icon ' + (fileInfo.scanned ? 'file_manager_scan_icon_scanned' : '')} src="/assets/icons/upc-scan.svg" alt="alt" />
			</div>
		</div>
	);
}

export default FileManagerFile;