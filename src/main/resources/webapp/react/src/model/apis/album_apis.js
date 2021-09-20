export const get_fetchAlbums = async (page, parentId) => {
    console.log('get_fetchAlbums : ' + parentId);
    let searchParams = new URLSearchParams({page, parentId});
    return fetch('/album/image/list?' + searchParams.toString()).then( response => response.json());
}