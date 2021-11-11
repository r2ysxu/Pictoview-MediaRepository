export const get_fetchAlbums = async (page, parentId) => {
    const searchParams = new URLSearchParams({page, parentId});
    return fetch('/album/list?' + searchParams.toString()).then( response => response.json());
}

export const get_searchAlbums = async (page, query) => {
    const searchParams = new URLSearchParams({page, query});
    return fetch('/album/search?' + searchParams.toString()).then( response => response.json());
}

export const get_listAlbumImages = async (page, albumId) => {
    if (albumId <= 0) return { items: [], pageInfo: { page: 0, total: 0, hasNext: false } };
    const searchParams = new URLSearchParams({page, albumId});
    return fetch('/album/images/list?' + searchParams.toString()).then( response => response.json());
}

export const get_listAlbumVideos = async (page, albumId) => {
    if (albumId <= 0) return { items: [], pageInfo: { page: 0, total: 0, hasNext: false } };
    const searchParams = new URLSearchParams({page, albumId});
    return fetch('/album/videos/list?' + searchParams.toString()).then( response => response.json());
}

export const get_listAlbumAudios = async (page, albumId) => {
    if (albumId <= 0) return { items: [], pageInfo: { page: 0, total: 0, hasNext: false } };
    const searchParams = new URLSearchParams({page, albumId});
    return fetch('/album/audio/list?' + searchParams.toString()).then( response => response.json());
}

export const get_listAlbumTags = async(albumId) => {
    if (albumId <= 0) return {};
    const searchParams = new URLSearchParams({albumId});
    return fetch('/album/tag/list?' + searchParams.toString()).then( response => response.json());
}

export const post_createAlbum = async(album) => {
    return fetch('/album/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(album),
    }).then( response => response.json());
}

export const post_uploadAlbum = async(albumId, file, fromMetadata) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('albumId', albumId);
    formData.append('fromMetadata', fromMetadata);
    return fetch('/album/update/upload', {
        method: 'POST',
        headers: { 'enctype': 'multipart/form-data' },
        body: formData,
    }).then( response => response.json());
}

export const post_updateAlbum = async(album) => {
    return fetch('/album/update', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(album),
    }).then( response => response.json());
}

export const post_changeAlbumCover = async(albumId, imageId) => {
    return fetch('/album/update/cover', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({albumId, imageId}),
    }).then( response => response.json());
}