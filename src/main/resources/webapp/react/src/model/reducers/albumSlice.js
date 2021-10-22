import { createSlice, createAction, createAsyncThunk } from '@reduxjs/toolkit';
import { get_fetchAlbums, get_searchAlbums, get_listAlbumImages, get_listAlbumVideos, get_listAlbumTags } from '../apis/album_apis';
import { post_tagAlbum } from '../apis/tag_apis.js';

const pendingMoreRequests = new Set();

const initialState = {
    albumId: 0,

    albums: [],
    albumsPage: 0,
    albumsHasMore: true,

    imageIds: [],
    imageIdsPage: 0,
    imageIdsHasMore: true,

    videoIds: [],
    videoIdsPage: 0,
    videoIdsHasMore: true,

    isLoading: false,
}

export const searchAlbums = createAsyncThunk('album/search', async (query) => {
    const albums = await get_searchAlbums(query);
    return {
        albumId: 0,
        albums,
        albumsPage: 0,
        imageIds: [],
        imageIdsPage: 0,
    };
});

export const loadCurrentAlbumInfo = createAsyncThunk('album/load', async (albumId) => {
    pendingMoreRequests.clear();
    const albums = await get_fetchAlbums(0, albumId);
    const imageIds = await get_listAlbumImages(0, albumId);
    const videoIds = await get_listAlbumVideos(0, albumId);
    return {
        albumId,
        albums,
        albumPage: 0,
        albumsHasMore: true,
        imageIds,
        imageIdsPage: 0,
        imageIdsHasMore: true,
        videoIds,
        videoIdsPage: 0,
        videoIdsHasMore: true,
    };
});

export const loadMoreImages = createAsyncThunk('album/load/image/more', async (_value, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const moreImages = await get_listAlbumImages(currentState.imageIdsPage + 1, currentState.albumId);
    return {
        imageIdsPage: currentState.imageIdsPage + 1,
        moreImages,
        imageIdsHasMore: moreImages && moreImages.length > 0
    }
});

export const loadMoreVideos = createAsyncThunk('album/load/video/more', async (_value, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const moreVideos = await get_listAlbumVideos(currentState.videoIdsPage + 1, currentState.albumId);
    return {
        videoIdsPage: currentState.videoIdsPage + 1,
        moreVideos,
        videoIdsHasMore: moreVideos && moreVideos.length > 0
    }
});

export const loadAlbumTags = createAsyncThunk('album/load/tags', async(albumId, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const tags = await get_listAlbumTags(albumId);
    const currentAlbumIndex = currentState.albums.findIndex(album => album.id === albumId);
    const currentAlbum = {...currentState.albums[currentAlbumIndex], tags };
    return {
        currentAlbumIndex,
        currentAlbum,
    }
});

export const updateCategoryTags = createAsyncThunk('album/tags/update', async({albumId, categories}, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const tags = await post_tagAlbum({albumId, categories});
    console.log('tags', tags);
    const currentAlbumIndex = currentState.albums.findIndex(album => album.id === albumId);
    const currentAlbum = {...currentState.albums[currentAlbumIndex], tags };
    return {
        currentAlbumIndex,
        currentAlbum,
    }
});

export const addCategory = createAsyncThunk('/album/tags/category/new', async ({albumId, newCategory}, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const currentAlbumIndex = currentState.albums.findIndex(album => album.id === albumId);
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
            .addCase(loadCurrentAlbumInfo.pending, (state,  action) => {
            }).addCase(loadCurrentAlbumInfo.fulfilled, (state, action) => {
                Object.assign(state, action.payload);
                state.isLoading = false;
            }).addCase(searchAlbums.fulfilled, (state, action) => {
                Object.assign(state, action.payload);
                state.isLoading = false;
            }).addCase(loadMoreImages.fulfilled, (state, action) => {
                if (pendingMoreRequests.has(action.meta.requestId)) {
                    state.imageIdsHasMore = action.payload.imageIdsHasMore;
                    state.imageIdsPage = action.payload.imageIdsPage;
                    state.imageIds.push(...action.payload.moreImages || []);
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
                    state.videoIdsHasMore = action.payload.videoIdsHasMore;
                    state.videoIdsPage = action.payload.videoIdsPage;
                    state.videoIds.push(...action.payload.moreVideos || []);
                    pendingMoreRequests.delete(action.meta.requestId);
                }
            }).addCase(loadAlbumTags.fulfilled, (state, action) => {
                state.albums[action.payload.currentAlbumIndex] = action.payload.currentAlbum;
            }).addCase(updateCategoryTags.fulfilled, (state, action) => {
                state.albums[action.payload.currentAlbumIndex] = action.payload.currentAlbum;
            }).addCase(addCategory.fulfilled, (state, action) => {
                state.albums[action.payload.currentAlbumIndex].tags.categories.push(action.payload.newCategory);
            });
    },
});

export const selectAlbums = (state) => {
    return state.album;
}

export default albumSlice.reducer;