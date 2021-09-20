import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { get_fetchAlbums, get_searchAlbums, get_listAlbumImages } from '../apis/album_apis';

const initialState = {
    albumId: 0,
    albums: [],
    imageIds: [],
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
        imageIds,
    };
});

export const albumSlice = createSlice({
    name: 'album',
    initialState,
    reducers: {

    },
    extraReducers(builder) {
        builder
            .addCase(loadCurrentAlbumInfo.fulfilled, (state, action) => {
                Object.assign(state, action.payload);
            }).addCase(searchAlbums.fulfilled, (state, action) => {
                Object.assign(state, action.payload);
            });
    },
});

export const selectAlbums = (state) => {
    return state.album;
}

export default albumSlice.reducer;