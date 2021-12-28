import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { get_categories, get_tagsByCategory } from '../apis/tag_apis';

const initialState = {
    categories: []
}

export const loadCategories = createAsyncThunk('tag/load/category', async () => {
    return await get_categories();
});

export const loadTagsByCategory = createAsyncThunk('tag/load/category/tags', async ({categoryId}, thunkAPI) => {
    const currentState = thunkAPI.getState().tags;
    const currentCategoryIndex = currentState.categories.findIndex(category => category.id === categoryId);
    const category = await get_tagsByCategory(categoryId);
    return {
        currentCategoryIndex,
        category
    }
});

export const tagSlice = createSlice({
    name: 'tags',
    initialState,
    reducers: {},
    extraReducers(builder) {
        builder
            .addCase(loadCategories.fulfilled, (state, action) => {
                Object.assign(state.categories, action.payload);
            }).addCase(loadTagsByCategory.fulfilled, (state, action) => {
                state.categories[action.payload.currentCategoryIndex].tags = action.payload.category;
            });
    },
});

export const selectCategories = (state) => {
    return state.tags;
}

export default tagSlice.reducer;