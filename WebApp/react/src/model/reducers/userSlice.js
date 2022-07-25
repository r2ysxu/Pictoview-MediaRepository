import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { get_userProfile, get_logout } from '../apis/user_apis';

const initialState = {
    isLoading: false,
    isLoggedIn: false,
    username: "",
}

export const loadUserProfile = createAsyncThunk('user/profile', async () => {
    return await get_userProfile();
});

export const logout = createAsyncThunk('user/logout', async ({ mediaId }) => {
    return await get_logout();
});


export const videoSlice = createSlice({
    name: 'user',
    initialState,
    reducers: {},
    extraReducers(builder) {
        builder
            .addCase(loadUserProfile.pending, (state, action) => {
                state.isLoading = true;
            }).addCase(loadUserProfile.fulfilled, (state, action) => {
                state.isLoading = false;
                if (action.payload.username != null) {
                    state.username = action.payload.username;
                    state.isLoggedIn = true;
                }
            }).addCase(loadUserProfile.rejected, (state, action) => {
                state.isLoading = false;
                console.log('rejected')
            }).addCase(logout.fulfilled, (state) => {
                state.isLoading = false;
                state.isLoggedIn = false;
            });
    },
});

export const selectUser = (state) => {
    return state.user;
}

export const selectUserLoggedIn = (state) => {
    return selectUser(state).isLoggedIn;
}

export default videoSlice.reducer;