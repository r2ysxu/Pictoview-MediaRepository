import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { get_categories, get_tagsByCategory } from 'model/apis/tag_apis';

import { RootState } from 'model/store';
import { ID, Category } from 'model/apis/api.types';

export type CategoryState = {
  categories: Category[];
}

type CategoryRequest = {
  categoryId: ID
}

const initialState: CategoryState = {
  categories: []
}

export const loadCategories = createAsyncThunk('tag/load/category', async () => {
  return await get_categories();
});

export const loadTagsByCategory = createAsyncThunk('tag/load/category/tags', async ({ categoryId }: CategoryRequest, thunkAPI) => {
  const { tagsState } = thunkAPI.getState() as RootState;
  const currentCategoryIndex = tagsState.categories.findIndex( (category: Category) => category.id === categoryId);
  const category = await get_tagsByCategory(categoryId);
  return {
    currentCategoryIndex,
    category
  }
});

export const tagSlice = createSlice({
  name: 'tagsState',
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(loadCategories.fulfilled, (state: CategoryState, action) => {
        Object.assign(state.categories, action.payload);
      }).addCase(loadTagsByCategory.fulfilled, (state: CategoryState, action) => {
        state.categories[action.payload.currentCategoryIndex].tags = action.payload.category;
      });
  },
});

export const selectCategories = (state: RootState) => {
  return state.tagsState;
}

export default tagSlice.reducer;