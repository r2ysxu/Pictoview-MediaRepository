import { configureStore } from '@reduxjs/toolkit'
import userReducer from './reducers/userSlice';
import albumReducer from './reducers/albumSlice';
import tagReducer, { CategoryState } from './reducers/tagSlice';

export type RootState = {
  tagsState: CategoryState;
}

export default configureStore({
  reducer: {
    user: userReducer,
    albumState: albumReducer,
    tagsState: tagReducer,
  },
})