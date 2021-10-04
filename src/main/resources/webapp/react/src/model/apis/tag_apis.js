export const get_categories = async () => {
    return fetch('/album/category/list').then( response => response.json());
}

export const post_createCategory = async (categoryName) => {
    return fetch('/album/tag/category/create', {
            method: 'POST',
            body: categoryName
        });
}

export const post_tagAlbum = async (albumCategoryTags) => {
    return fetch('/album/image/tag/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(albumCategoryTags),
    }).then( response => response.json());
}

export const get_searchTags = async (tagQuery) => {
    const searchParams = new URLSearchParams({tagQuery});
    return fetch('/album/tag/search?' + searchParams.toString()).then( response => response.json());
}