import { configureStore } from '@reduxjs/toolkit'
import userReducer from './reducers/userSlice';
import albumReducer from './reducers/albumSlice';
import tagReducer from './reducers/tagSlice';

export default configureStore({
  reducer: {
    user: userReducer,
    album: albumReducer,
    tags: tagReducer,
  },
})