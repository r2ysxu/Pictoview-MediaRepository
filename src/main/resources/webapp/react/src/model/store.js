import { configureStore } from '@reduxjs/toolkit'
import albumReducer from './reducers/albumSlice';

export default configureStore({
  reducer: {
    album: albumReducer,
  },
})