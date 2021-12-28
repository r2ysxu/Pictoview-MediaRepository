const onResponseJson = (response) => {
    if (response.status === 401) {
        window.location = '/';
    }
    return response.json();
}

export const get_categories = async () => {
    return fetch('/category/list').then(onResponseJson);
}

export const get_tagsByCategory = async (categoryId) => {
    const searchParams = new URLSearchParams({categoryId});
    return fetch('/tag/list?' + searchParams.toString()).then(onResponseJson);
}

export const post_createCategory = async (categoryName) => {
    return fetch('/tag/category/create', {
            method: 'POST',
            body: categoryName
        });
}

export const post_tagAlbum = async (albumCategoryTags) => {
    return fetch('/album/tag/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(albumCategoryTags),
    }).then( response => response.json());
}

export const get_searchTags = async (tagQuery) => {
    const searchParams = new URLSearchParams({tagQuery});
    return fetch('/tag/search?' + searchParams.toString()).then(onResponseJson);
}