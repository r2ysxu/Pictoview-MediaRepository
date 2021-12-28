import { configureStore } from '@reduxjs/toolkit'
import albumReducer from './reducers/albumSlice';
import tagReducer from './reducers/tagSlice';

export default configureStore({
  reducer: {
    album: albumReducer,
    tags: tagReducer,
  },
})