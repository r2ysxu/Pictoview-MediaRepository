export const get_fetchAlbums = async (page, parentId) => {
    const searchParams = new URLSearchParams({page, parentId});
    return fetch('/album/list?' + searchParams.toString()).then( response => response.json());
}

export const get_searchAlbums = async (query) => {
    const searchParams = new URLSearchParams({query});
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

export const get_listAlbumTags = async(albumId) => {
    if (albumId <= 0) return {};
    const searchParams = new URLSearchParams({albumId});
    return fetch('/album/tag/list?' + searchParams.toString()).then( response => response.json());
}