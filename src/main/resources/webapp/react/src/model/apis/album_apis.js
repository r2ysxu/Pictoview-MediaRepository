export const get_fetchAlbums = async (page, parentId) => {
    let searchParams = new URLSearchParams({page, parentId});
    return fetch('/album/image/list?' + searchParams.toString()).then( response => response.json());
}

export const get_searchAlbums = async (query) => {
    let searchParams = new URLSearchParams({query});
    return fetch('/album/image/search?' + searchParams.toString()).then( response => response.json());
}


export const get_listAlbumImages = async (page, albumId) => {
    if (albumId < 0) return [];
    let searchParams = new URLSearchParams({page, albumId});
    return fetch('/album/image/photos/list?' + searchParams.toString()).then( response => response.json());
}