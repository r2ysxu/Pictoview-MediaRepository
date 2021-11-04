import React from 'react';
import { useState, useEffect } from 'react';
import ClipLoader from "react-spinners/ClipLoader";
import FileManagerFile from './FileManagerFile';
import './FileManager.css';

async function fetchDirectories(path) {
    const data = { path, name: '' };
    const listDirectoryRequest = fetch('/admin/files/list', {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then( response => response.json());
    return listDirectoryRequest;
}

function FileManager({currentPath, setCurrentPath}) {
	const [directories, setDirectories] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(()=> {
        setIsLoading(true);
        fetchDirectories(currentPath).then(data => {
            setDirectories(data)
            setIsLoading(false);
        });
    }, [currentPath, setDirectories]);

    const setNewPath = (name) => {
        const newPath = currentPath + "/" + name;
        setCurrentPath(newPath);
    }

    const setParentPath = () => {
        const newParentPath = currentPath.replace(new RegExp('/(?!.*/).+$'), '');
        setCurrentPath(newParentPath);
    }

    return (
    	<div>
            <div onClick={() => setParentPath()}>
                <img className="file_manager_file_icon" src="/assets/icons/folder-symlink.svg" alt="" />
            </div>
        	{!isLoading && (directories || []).map( (directory, index) => 
        		<FileManagerFile key={index} fileInfo={directory} onSetPath={setNewPath}/>
        	)}
            {isLoading && <ClipLoader loading={isLoading} size={150} />}
    	</div>
    );
}

export default FileManager;