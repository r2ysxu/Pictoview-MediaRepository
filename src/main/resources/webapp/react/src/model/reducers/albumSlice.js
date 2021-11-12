import { createSlice, createAction, createAsyncThunk } from '@reduxjs/toolkit';
import { 
    get_fetchAlbums,
    get_searchAlbums,
    get_listAlbumImages,
    get_listAlbumVideos,
    get_listAlbumAudios,
    get_listAlbumTags,
    post_createAlbum,
    post_uploadAlbum,
    post_updateAlbum,
    post_changeAlbumCover,
} from '../apis/album_apis';
import { post_tagAlbum } from '../apis/tag_apis.js';

const pendingMoreRequests = new Set();

const initialState = {
    albumId: 0,
    albums: { items: [], pageInfo: { page: 0, total: 0, hasNext: false } },
    images: { items: [], pageInfo: { page: 0, total: 0, hasNext: false } },
    videos: { items: [], pageInfo: { page: 0, total: 0, hasNext: false } },
    audios: { items: [], pageInfo: { page: 0, total: 0, hasNext: false } },

    isLoading: false,
}

export const createAlbum = async ({name, publisher, description}) => {
    return await post_createAlbum({
        name,
        publisher,
        description,
        coverId: null,
    });
}

export const uploadAlbumFile = async ({albumId, file, fromMetadata}) => {
    return await post_uploadAlbum(albumId, file, fromMetadata);
};

export const updateAlbum = createAsyncThunk('album/update/info', async(updatedAlbum, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const currentAlbumIndex = currentState.albums.items.findIndex(album => album.id === updatedAlbum.id);
    const tags = await get_listAlbumTags(updatedAlbum.id);
    const currentAlbum = {...await post_updateAlbum(updatedAlbum), tags };
    return {
        currentAlbumIndex,
        currentAlbum,
    }
});

export const updateCoverImage = createAsyncThunk('album/update/cover', async ({albumId, imageId}) => {
    return await post_changeAlbumCover(albumId, imageId);
});

export const searchAlbums = createAsyncThunk('album/search', async (query, thunkAPI) => {
    const page = 0;
    const albums = await get_searchAlbums(page, query);
    return {
        albumId: 0,
        albums
    };
});

export const loadCurrentAlbumInfo = createAsyncThunk('album/load', async (albumId) => {
    pendingMoreRequests.clear();
    const albums = await get_fetchAlbums(0, albumId);
    const images = await get_listAlbumImages(0, albumId);
    const videos = await get_listAlbumVideos(0, albumId);
    const audios = await get_listAlbumAudios(0, albumId);
    return {
        albumId,
        albums,
        images,
        videos,
        audios,
    };
});

export const loadMoreAlbums = createAsyncThunk('album/load', async ({albumId, page}) => {
    const albumsPage = await get_fetchAlbums(page, albumId);
    return { albumsPage };
});

export const loadMoreImages = createAsyncThunk('album/load/image/more', async ({albumId, page}) => {
    const imagesPage = await get_listAlbumImages(page, albumId);
    return { imagesPage };
});

export const loadMoreVideos = createAsyncThunk('album/load/video/more', async ({albumId, page}) => {
    const videosPage = await get_listAlbumVideos(page, albumId);
    return { videosPage };
});

export const loadMoreAudio = createAsyncThunk('album/load/audio/more', async({albumId, page}) => {
    const audiosPage = await get_listAlbumAudios(page, albumId);
    return { audiosPage };
});

export const loadAlbumTags = createAsyncThunk('album/load/tags', async (albumId, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const tags = await get_listAlbumTags(albumId);
    const currentAlbumIndex = currentState.albums.items.findIndex(album => album.id === albumId);
    const currentAlbum = {...currentState.albums.items[currentAlbumIndex], tags };
    return {
        currentAlbumIndex,
        currentAlbum,
    }
});

export const updateCategoryTags = createAsyncThunk('album/tags/update', async ({albumId, categories}, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const tags = await post_tagAlbum({albumId, categories});
    const currentAlbumIndex = currentState.albums.items.findIndex(album => album.id === albumId);
    const currentAlbum = {...currentState.albums.items[currentAlbumIndex], tags };
    return {
        currentAlbumIndex,
        currentAlbum,
    }
});

export const addCategory = createAsyncThunk('/album/tags/category/new', async ({albumId, newCategory}, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const currentAlbumIndex = currentState.albums.items.findIndex(album => album.id === albumId);
    return {
        currentAlbumIndex,
        newCategory
    };
});

export const albumSlice = createSlice({
    name: 'album',
    initialState,
    reducers: {},
    extraReducers(builder) {
        builder
              .addCase(loadCurrentAlbumInfo.fulfilled, (state, action) => {
                Object.assign(state,action.payload);
                state.isLoading = false;
            }).addCase(loadCurrentAlbumInfo.pending, (state,  action) => {
                pendingMoreRequests.add(action.meta.requestId);
                state.isLoading = true;
            }).addCase(searchAlbums.fulfilled, (state, action) => {
                Object.assign(state, action.payload);
                state.isLoading = false;
            }).addCase(loadMoreImages.fulfilled, (state, action) => {
                if (pendingMoreRequests.has(action.meta.requestId)) {
                    state.images.pageInfo = action.payload.imagesPage.pageInfo;
                    state.images.items.push(...action.payload.imagesPage.items || []);
                    pendingMoreRequests.delete(action.meta.requestId);
                }
                state.isLoading = false;
            }).addCase(loadMoreImages.pending, (state, action) => {
                pendingMoreRequests.add(action.meta.requestId);
                state.isLoading = true;
            }).addCase(loadMoreImages.rejected, (state, action) => {
                state.isLoading = false;
            }).addCase(loadMoreVideos.pending, (state, action) => {
                pendingMoreRequests.add(action.meta.requestId);
                state.isLoading = true;
            }).addCase(loadMoreVideos.rejected, (state, action) => {
                state.isLoading = false;
            }).addCase(loadMoreVideos.fulfilled, (state, action) => {
                if (pendingMoreRequests.has(action.meta.requestId)) {
                    state.videos.pageInfo = action.payload.videosPage.pageInfo;
                    state.videos.items.push(...action.payload.videosPage.items || []);
                    pendingMoreRequests.delete(action.meta.requestId);
                }
            }).addCase(loadMoreAudio.pending, (state, action) => {
                pendingMoreRequests.add(action.meta.requestId);
                state.isLoading = true;
            }).addCase(loadMoreAudio.rejected, (state, action) => {
                state.isLoading = false;
            }).addCase(loadMoreAudio.fulfilled, (state, action) => {
                if (pendingMoreRequests.has(action.meta.requestId)) {
                    state.audios.pageInfo = action.payload.audiosPage.pageInfo;
                    state.audios.items.push(...action.payload.audiosPage.items || []);
                    pendingMoreRequests.delete(action.meta.requestId);
                }
            }).addCase(updateAlbum.fulfilled, (state, action) => {
                state.albums.items[action.payload.currentAlbumIndex] = action.payload.currentAlbum;
            }).addCase(loadAlbumTags.fulfilled, (state, action) => {
                state.albums.items[action.payload.currentAlbumIndex] = action.payload.currentAlbum;
            }).addCase(updateCategoryTags.fulfilled, (state, action) => {
                state.albums.items[action.payload.currentAlbumIndex] = action.payload.currentAlbum;
            }).addCase(addCategory.fulfilled, (state, action) => {
                state.albums.items[action.payload.currentAlbumIndex].tags.categories.push(action.payload.newCategory);
            });
    },
});

export const selectAlbums = (state) => {
    return state.album;
}

export default albumSlice.reducer;