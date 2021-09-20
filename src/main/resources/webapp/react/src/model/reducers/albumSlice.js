import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { get_fetchAlbums, get_searchAlbums, get_listAlbumImages } from '../apis/album_apis';

const initialState = {
    albumId: 0,
    albums: [],
    albumsPage: 0,
    imageIds: [],
    imageIdsPage: 0,
    isLoading: false,
}

export const searchAlbums = createAsyncThunk('album/search', async (query) => {
    const albums = await get_searchAlbums(query);
    return {
        albumId: 0,
        albums,
        imageIds: [],
    };
});

export const loadCurrentAlbumInfo = createAsyncThunk('album/load', async (albumId) => {
    const albums = await get_fetchAlbums(0, albumId);
    const imageIds = await get_listAlbumImages(0, albumId);
    return {
        albumId,
        albums,
        albumPage: 0,
        imageIds,
        imageIdsPage: 0,
    };
});

export const loadMoreImages = createAsyncThunk('album/load/image/more', async (_value, thunkAPI) => {
    const currentState = thunkAPI.getState().album;
    const moreImages = await get_listAlbumImages(currentState.imageIdsPage + 1, currentState.albumId);
    return {
        imageIdsPage: currentState.imageIdsPage + 1,
        moreImages
    }
});

export const albumSlice = createSlice({
    name: 'album',
    initialState,
    reducers: {},
    extraReducers(builder) {
        builder
            .addCase(loadCurrentAlbumInfo.fulfilled, (state, action) => {
                Object.assign(state, action.payload);
                state.isLoading = false;
            }).addCase(searchAlbums.fulfilled, (state, action) => {
                Object.assign(state, action.payload);
                state.isLoading = false;
            }).addCase(loadMoreImages.fulfilled, (state, action) => {
                state.imageIdsPage = action.payload.imageIdsPage;
                state.imageIds.push(...action.payload.moreImages || []);
                state.isLoading = false;
            }).addCase(loadMoreImages.pending, (state, action) => {
                state.isLoading = true;
            }).addCase(loadMoreImages.rejected, (state, action) => {
                state.isLoading = false;
            });
    },
});

export const selectAlbums = (state) => {
    return state.album;
}

export default albumSlice.reducer;