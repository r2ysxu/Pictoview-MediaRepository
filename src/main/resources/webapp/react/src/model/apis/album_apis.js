export const get_fetchAlbums = async (page, parentId) => {
    const searchParams = new URLSearchParams({page, parentId});
    return fetch('/album/image/list?' + searchParams.toString()).then( response => response.json());
}

export const get_searchAlbums = async (query) => {
    const searchParams = new URLSearchParams({query});
    return fetch('/album/image/search?' + searchParams.toString()).then( response => response.json());
}

export const get_listAlbumImages = async (page, albumId) => {
    if (albumId <= 0) return [];
    const searchParams = new URLSearchParams({page, albumId});
    return fetch('/album/image/photos/list?' + searchParams.toString()).then( response => response.json());
}

export const get_listAlbumTags = async(albumId) => {
    if (albumId <= 0) return {};
    const searchParams = new URLSearchParams({albumId});
    return fetch('/album/image/tag/list?' + searchParams.toString()).then( response => response.json());
}