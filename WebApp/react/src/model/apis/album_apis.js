const onResponseJson = (response) => {
    if (response.status === 401) {
        window.location = '/';
    }
    return response.json();
}

export const post_uploadAlbumPath = async (path) => {
    return fetch('/album/update/filepath', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: path,
    }).then(onResponseJson);
}

export const get_album = async (albumId) => {
    if (albumId <= 0) return { metaType: "albums" };
    const searchParams = new URLSearchParams({albumId});
    return fetch('/album/get?' + searchParams.toString()).then(onResponseJson);
}

export const get_listAlbums = async (page, parentId, sort) => {
    if (sort === undefined) sort = { field: 'unsorted', ascending: true };
    const searchParams = new URLSearchParams({page, parentId, sortField: sort.field, ascending: sort.ascending });
    return fetch('/album/list?' + searchParams.toString()).then(onResponseJson);
}

export const get_searchAlbums = async (page, query, sortField) => {
    const searchParams = new URLSearchParams({page, query, sortField});
    return fetch('/album/search?' + searchParams.toString()).then(onResponseJson);
}

export const get_listAlbumImages = async (page, albumId) => {
    if (albumId <= 0) return { items: [], pageInfo: { page: 0, total: 0, hasNext: false } };
    const searchParams = new URLSearchParams({page, albumId});
    return fetch('/album/images/list?' + searchParams.toString()).then(onResponseJson);
}

export const get_listAlbumVideos = async (page, albumId) => {
    if (albumId <= 0) return { items: [], pageInfo: { page: 0, total: 0, hasNext: false } };
    const searchParams = new URLSearchParams({page, albumId});
    return fetch('/album/videos/list?' + searchParams.toString()).then(onResponseJson);
}

export const get_listAlbumAudios = async (page, albumId) => {
    if (albumId <= 0) return { items: [], pageInfo: { page: 0, total: 0, hasNext: false } };
    const searchParams = new URLSearchParams({page, albumId});
    return fetch('/album/audio/list?' + searchParams.toString()).then(onResponseJson);
}

export const get_listAlbumTags = async(albumId) => {
    if (albumId <= 0) return {};
    const searchParams = new URLSearchParams({albumId});
    return fetch('/album/tag/list?' + searchParams.toString()).then(onResponseJson);
}

export const post_createAlbum = async(album) => {
    return fetch('/album/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(album),
    }).then(onResponseJson);
}

export const post_uploadMedia = async(albumId, file) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('albumId', albumId);
    return fetch('/album/update/upload/media', {
        method: 'POST',
        headers: { 'enctype': 'multipart/form-data' },
        body: formData,
    }).then(onResponseJson);
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
    }).then(onResponseJson);
}

export const post_updateAlbum = async(album) => {
    return fetch('/album/update', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(album),
    }).then(onResponseJson);
}

export const post_changeAlbumCover = async(albumId, imageId) => {
    return fetch('/album/update/cover', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({albumId, imageId}),
    }).then(onResponseJson);
}

export const delete_album = async(albumId) => {
    return fetch('/album/delete', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        body: albumId
    })
}