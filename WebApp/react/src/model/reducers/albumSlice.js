import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { 
  get_album,
  get_searchAlbums,
  get_listAlbums,
  get_listAlbumImages,
  get_listAlbumVideos,
  get_listAlbumAudios,
  get_listAlbumTags,
  post_createAlbum,
  post_uploadAlbum,
  post_uploadMedia,
  post_updateAlbum,
  post_changeAlbumCover,
  delete_album,
} from '../apis/album_apis';
import { post_tagAlbum } from '../apis/tag_apis';

const pendingMoreRequests = new Set();

const initialState = {
  id: 0,
  albumName: '',
  description: '',
  metaType: 'albums',
  albums: { items: [], pageInfo: { page: 0, total: 0, hasNext: false }, sortedBy: { field: 'unsorted', asc: true } },
  images: { items: [], pageInfo: { page: 0, total: 0, hasNext: false }, sortedBy: { field: 'unsorted', asc: true } },
  videos: { items: [], pageInfo: { page: 0, total: 0, hasNext: false }, sortedBy: { field: 'unsorted', asc: true } },
  audios: { items: [], pageInfo: { page: 0, total: 0, hasNext: false }, sortedBy: { field: 'unsorted', asc: true } },

  isLoading: false,
}

export const createAlbum = async ({name, publisher, description}) => {
  return await post_createAlbum({
    name,
    publisher,
    description,
    coverId: null,
  });
}

export const uploadAlbumFile = async ({albumId, file, fromMetadata}) => {
  return await post_uploadAlbum(albumId, file, fromMetadata);
};

export const uploadAlbumMediaFile = async({albumId, file}) => {
  return await post_uploadMedia(albumId, file);
}

export const updateAlbum = createAsyncThunk('album/update/info', async (updatedAlbum, thunkAPI) => {
  const currentState = thunkAPI.getState().album;
  const currentAlbumIndex = currentState.albums.items.findIndex(album => album.id === updatedAlbum.id);
  const categoryTags = await get_listAlbumTags(updatedAlbum.id);
  const currentAlbum = {...await post_updateAlbum(updatedAlbum), tags: buildCategoryTags(categoryTags) };
  return {
    currentAlbumIndex,
    currentAlbum,
  }
});

export const updateCoverImage = createAsyncThunk('album/update/cover', async ({albumId, imageId}) => {
  return await post_changeAlbumCover(albumId, imageId);
});

export const searchAlbums = createAsyncThunk('album/search', async (query, thunkAPI) => {
  const page = 0;
  const albums = await get_searchAlbums(page, query, "name");
  return {
    id: null,
    albumName: '',
    albumQuery: query,
    albums,
    images: { items: [], pageInfo: { page: 0, total: 0, hasNext: false, sortedBy: { field: 'unsorted', asc: true } } },
    videos: { items: [], pageInfo: { page: 0, total: 0, hasNext: false, sortedBy: { field: 'unsorted', asc: true } } },
    audios: { items: [], pageInfo: { page: 0, total: 0, hasNext: false, sortedBy: { field: 'unsorted', asc: true } } },
  };
});

export const loadCurrentAlbumInfo = createAsyncThunk('album/load', async ({albumId, sort}) => {
  pendingMoreRequests.clear();
  const currentAlbum = get_album(albumId);
  const albums = get_listAlbums(0, albumId, sort);
  const images = get_listAlbumImages(0, albumId);
  const videos = get_listAlbumVideos(0, albumId);
  const audios = get_listAlbumAudios(0, albumId);
  const currentAlbumInfo = await currentAlbum;
  return {
    id: albumId,
    albumName: currentAlbumInfo.name,
    description: currentAlbumInfo.description,
    metaType: currentAlbumInfo.metaType,
    rating: currentAlbumInfo.rating,
    albums: await albums,
    images: await images,
    videos: await videos,
    audios: await audios,
  };
});

export const loadMoreSearchAlbums = createAsyncThunk('album/search/album/more', async ({page}, thunkAPI) => {
  const currentState = thunkAPI.getState().album;
  const albumsPage = await get_searchAlbums(page, currentState.albumQuery, "name");
  return { albumsPage };
});

export const loadMoreAlbums = createAsyncThunk('album/load/album/more', async ({albumId, page, sort}) => {
  const albumsPage = await get_listAlbums(page, albumId, sort);
  return { albumsPage };
});

export const loadMoreImages = createAsyncThunk('album/load/image/more', async ({albumId, page}) => {
  const imagesPage = await get_listAlbumImages(page, albumId);
  return { imagesPage };
});

export const loadMoreVideos = createAsyncThunk('album/load/video/more', async ({albumId, page}) => {
  const videosPage = await get_listAlbumVideos(page, albumId);
  return { videosPage };
});

export const loadMoreAudio = createAsyncThunk('album/load/audio/more', async({albumId, page}) => {
  const audiosPage = await get_listAlbumAudios(page, albumId);
  return { audiosPage };
});

const buildCategoryTags = (categoryTags) => {
  const categoryMap = categoryTags.categories.reduce( (map, category) => {
    category.tags = [];
    map.set(category.id, category);
    return map;
  }, new Map());
  categoryTags.tags.forEach( tag => categoryMap.get(tag.categoryId).tags.push(tag));
  return { categories: [...categoryMap.values()] };
}

export const loadCurrentAlbumTags = createAsyncThunk('album/load/current/tags', async (albumId) => {
  const categoryTags = await get_listAlbumTags(albumId);
  const tags = buildCategoryTags(categoryTags);
  return { tags };
});

export const loadAlbumTags = createAsyncThunk('album/load/tags', async (albumId, thunkAPI) => {
  const currentState = thunkAPI.getState().album;
  const categoryTags = await get_listAlbumTags(albumId);
  const tags = buildCategoryTags(categoryTags);

  const currentAlbumIndex = currentState.albums.items.findIndex(album => album.id === albumId);
  const currentAlbum = {...currentState.albums.items[currentAlbumIndex], tags };
  return {
    currentAlbumIndex,
    currentAlbum,
  };
});

export const updateCurrentAlbumCategoryTags = createAsyncThunk('album/tags/current/update', async ({albumId, tags, category}, thunkAPI) => {
  const categoryTags = await post_tagAlbum({albumId, tags, categories: [ category ]});
  const newTags = buildCategoryTags(categoryTags);
  return { tags: newTags };
});

export const updateCategoryTags = createAsyncThunk('album/tags/update', async ({albumId, tags, category}, thunkAPI) => {
  const currentState = thunkAPI.getState().album;
  const categoryTags = await post_tagAlbum({albumId, tags, categories: [ category ]});
  const newTags = buildCategoryTags(categoryTags);
  const currentAlbumIndex = currentState.albums.items.findIndex(album => album.id === albumId);
  const currentAlbum = {...currentState.albums.items[currentAlbumIndex], tags: newTags };
  return {
    currentAlbumIndex,
    currentAlbum,
  };
});

export const addCategory = createAsyncThunk('/album/tags/category/new', async ({albumId, newCategory}, thunkAPI) => {
  const currentState = thunkAPI.getState().album;
  const currentAlbumIndex = currentState.albums.items.findIndex(album => album.id === albumId);
  return {
    currentAlbumIndex,
    newCategory,
  };
});

export const changeMetaType = createAsyncThunk('/album/metaType/change', async ({metaType}) => {
  return metaType;
});

export const deleleAlbum = createAsyncThunk('/album/delete', async ({albumId}, thunkAPI) => {
  const currentState = thunkAPI.getState().album;
  await delete_album(albumId);
  const currentAlbumIndex = currentState.albums.items.findIndex(album => album.id === albumId);
  return {
    currentAlbumIndex,
  }
});

export const albumSlice = createSlice({
  name: 'album',
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder
        .addCase(loadCurrentAlbumInfo.fulfilled, (state, action) => {
        Object.assign(state,action.payload);
        state.isLoading = false;
      }).addCase(loadCurrentAlbumInfo.pending, (state,  action) => {
        pendingMoreRequests.add(action.meta.requestId);
        state.isLoading = true;
      }).addCase(searchAlbums.fulfilled, (state, action) => {
        Object.assign(state, action.payload);
        state.isLoading = false;
      }).addCase(loadMoreSearchAlbums.pending, (state, action) => {
        pendingMoreRequests.add(action.meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreSearchAlbums.fulfilled, (state, action) => {
        if (pendingMoreRequests.has(action.meta.requestId)) {
          state.albums.pageInfo = action.payload.albumsPage.pageInfo;
          state.albums.items.push(...action.payload.albumsPage.items || []);
          pendingMoreRequests.delete(action.meta.requestId);
        }
        state.isLoading = false;
      }).addCase(loadMoreAlbums.fulfilled, (state, action) => {
        if (pendingMoreRequests.has(action.meta.requestId)) {
          state.albums.pageInfo = action.payload.albumsPage.pageInfo;
          state.albums.items.push(...action.payload.albumsPage.items || []);
          pendingMoreRequests.delete(action.meta.requestId);
        }
        state.isLoading = false;
      }).addCase(loadMoreAlbums.pending, (state, action) => {
        pendingMoreRequests.add(action.meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreAlbums.rejected, (state, action) => {
        state.isLoading = false;
      }).addCase(loadMoreImages.fulfilled, (state, action) => {
        if (pendingMoreRequests.has(action.meta.requestId)) {
          state.images.pageInfo = action.payload.imagesPage.pageInfo;
          state.images.items.push(...action.payload.imagesPage.items || []);
          pendingMoreRequests.delete(action.meta.requestId);
        }
        state.isLoading = false;
      }).addCase(loadMoreImages.pending, (state, action) => {
        pendingMoreRequests.add(action.meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreImages.rejected, (state, action) => {
        state.isLoading = false;
      }).addCase(loadMoreVideos.pending, (state, action) => {
        pendingMoreRequests.add(action.meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreVideos.rejected, (state, action) => {
        state.isLoading = false;
      }).addCase(loadMoreVideos.fulfilled, (state, action) => {
        if (pendingMoreRequests.has(action.meta.requestId)) {
          state.videos.pageInfo = action.payload.videosPage.pageInfo;
          state.videos.items.push(...action.payload.videosPage.items || []);
          pendingMoreRequests.delete(action.meta.requestId);
        }
        state.isLoading = false;
      }).addCase(loadMoreAudio.pending, (state, action) => {
        pendingMoreRequests.add(action.meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreAudio.rejected, (state, action) => {
        state.isLoading = false;
      }).addCase(loadMoreAudio.fulfilled, (state, action) => {
        if (pendingMoreRequests.has(action.meta.requestId)) {
          state.audios.pageInfo = action.payload.audiosPage.pageInfo;
          state.audios.items.push(...action.payload.audiosPage.items || []);
          pendingMoreRequests.delete(action.meta.requestId);
        }
        state.isLoading = false;
      }).addCase(updateAlbum.fulfilled, (state, action) => {
        state.albums.items[action.payload.currentAlbumIndex] = action.payload.currentAlbum;
      }).addCase(loadAlbumTags.fulfilled, (state, action) => {
        state.albums.items[action.payload.currentAlbumIndex] = action.payload.currentAlbum;
      }).addCase(loadCurrentAlbumTags.fulfilled, (state, action) => {
        state.tags = action.payload.tags;
      }).addCase(updateCategoryTags.fulfilled, (state, action) => {
        state.albums.items[action.payload.currentAlbumIndex] = action.payload.currentAlbum;
      }).addCase(updateCurrentAlbumCategoryTags.fulfilled, (state, action) => {
        state.tags = action.payload.tags;
      }).addCase(addCategory.fulfilled, (state, action) => {
        state.albums.items[action.payload.currentAlbumIndex].tags.categories.push(action.payload.newCategory);
      }).addCase(changeMetaType.fulfilled, (state, action) => {
        state.metaType = action.payload;
      }).addCase(deleleAlbum.fulfilled, (state, action) => {
        state.albums.items.splice(action.payload.currentAlbumIndex, 1);
      });
  },
});

export const selectAlbums = (state) => {
  return state.album;
}

export default albumSlice.reducer;