import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { get_fetchAlbums } from '../apis/album_apis';

const initialState = {
    currentId: 0,
    history: [],
    albums: [],
    imageIds: [],
}



export const fetchAlbums = createAsyncThunk('album/load', async(albumId) => {
    return await get_fetchAlbums(0, albumId);
});

export const albumSlice = createSlice({
    name: 'album',
    initialState,
    reducers: {},
    extraReducers(builder) {
        builder.addCase(fetchAlbums.fulfilled, (state, action) => {
            console.log('payload', action);
            state.albums = action.payload;
        });
    },
});

export const selectAlbums = (state) => state.album.albums;

export default albumSlice.reducer;