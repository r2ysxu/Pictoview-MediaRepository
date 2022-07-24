import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { get_video_handler } from '../apis/video_apis';

const initialState = {
    isLoading: false,
}

export const loadVideoHandler = createAsyncThunk('video/handler', async ({ mediaId }) => {
    await get_video_handler(mediaId);
});


export const videoSlice = createSlice({
    name: 'video',
    initialState,
    reducers: {},
    extraReducers(builder) {
        builder
            .addCase(loadVideoHandler.pending, (state, action) => {
                state.isLoading = true;
            }).addCase(loadVideoHandler.fulfilled, (state) => {
                state.isLoading = false;
            }).addCase(loadVideoHandler.rejected, (state, action) => {
                state.isLoading = false;
                console.log('rejected')
            });
    },
});

export const selectVideo = (state) => {
    return state.video;
}

export default videoSlice.reducer;