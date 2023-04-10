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

const emptyPageItems = { items: [], pageInfo: { page: 0, total: 0, hasNext: false }, sortedBy: { field: 'unsorted', asc: true } };
const emptySearchQuery = { query: '', sort: { field: 'unsorted', asc: true }, isSearch: false };

const getState = (thunkAPI) => {
  return thunkAPI.getState().albumState;
}

const updateAlbumByIndex = (state, index, album) => {
  if (index === null) state.rootAlbum = album;
  else state.albums.items[index] = album;
}

const buildCategoryTags = (categoryTags) => {
  const categoryMap = categoryTags.categories.reduce( (map, category) => {
    category.tags = [];
    map.set(category.id, category);
    return map;
  }, new Map());
  categoryTags.tags.forEach( tag => categoryMap.get(tag.categoryId).tags.push(tag));
  return { categories: [...categoryMap.values()] };
}

const initialState = {
  isLoading: false,
  rootAlbum: null,
  search: emptySearchQuery,
  albums: emptyPageItems,
  images: emptyPageItems,
  videos: emptyPageItems,
  audios: emptyPageItems,
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

export const updateAlbum = createAsyncThunk('album/update/info', async (changedAlbum, thunkAPI) => {
  const { rootAlbum, albums } = getState(thunkAPI);
  const categoryTags = await get_listAlbumTags(changedAlbum.id);
  const updatedAlbum = {...await post_updateAlbum(changedAlbum), tags: buildCategoryTags(categoryTags) };
  const index = (rootAlbum?.id === changedAlbum.id) ? null : albums.items.findIndex(album => album.id === changedAlbum.id);
  return {
    index,
    updatedAlbum,
  }
});

export const updateCoverImage = createAsyncThunk('album/update/cover', async ({albumId, imageId}) => {
  return await post_changeAlbumCover(albumId, imageId);
});

export const searchAlbums = createAsyncThunk('album/search', async ({ query, sort }, thunkAPI) => {
  const page = 0;
  const albums = await get_searchAlbums(page, query, sort);
  return {
    rootAlbum: await get_album(0),
    search: { query, sort, isSearch: true },
    albums,
    images: emptyPageItems,
    videos: emptyPageItems,
    audios: emptyPageItems,
  };
});

export const loadRootAlbumInfo = createAsyncThunk('album/load', async ({albumId, sort}) => {
  pendingMoreRequests.clear();
  const [ rootAlbum, albums, images, videos, audios ] = await Promise.all([
    get_album(albumId),
    get_listAlbums(0, albumId, sort),
    get_listAlbumImages(0, albumId),
    get_listAlbumVideos(0, albumId),
    get_listAlbumAudios(0, albumId),
  ]);
  return { rootAlbum, albums, images, videos, audios, search: emptySearchQuery };
});

export const loadMoreSearchAlbums = createAsyncThunk('album/search/album/more', async ({ page, sort }, thunkAPI) => {
  const { search } = getState(thunkAPI);
  return { albumsPage: await get_searchAlbums(page, search.query, search.sort ) };
});

export const loadMoreAlbums = createAsyncThunk('album/load/album/more', async ({albumId, page, sort}) => {
  return { albumsPage: await get_listAlbums(page, albumId, sort) };
});

export const loadMoreImages = createAsyncThunk('album/load/image/more', async ({albumId, page}) => {
  return { imagesPage: await get_listAlbumImages(page, albumId) };
});

export const loadMoreVideos = createAsyncThunk('album/load/video/more', async ({albumId, page}) => {
  return { videosPage: await get_listAlbumVideos(page, albumId) };
});

export const loadMoreAudio = createAsyncThunk('album/load/audio/more', async({albumId, page}) => {
  return { audiosPage: await get_listAlbumAudios(page, albumId) };
});

export const loadAlbumTags = createAsyncThunk('album/load/tags', async (albumId, thunkAPI) => {
  const { rootAlbum, albums } = getState(thunkAPI);
  const categoryTags = await get_listAlbumTags(albumId);
  const tags = buildCategoryTags(categoryTags);
  const index = (rootAlbum?.id === albumId) ? null : albums.items.findIndex(album => album.id === albumId);
  const album = (rootAlbum?.id === albumId) ? { ...rootAlbum, tags} : {...albums.items[index], tags };
  return {
    index,
    album,
  };
});

export const updateCategoryTags = createAsyncThunk('album/tags/update', async ({ albumId, tags, category }, thunkAPI) => {
  const { rootAlbum, albums } = getState(thunkAPI);
  const categoryTags = await post_tagAlbum({ albumId, tags, categories: [ category ]});
  const newTags = buildCategoryTags(categoryTags);
  const index = (rootAlbum?.id === albumId) ? null : albums.items.findIndex(album => album.id === albumId);
  const updatedAlbum = (rootAlbum?.id === albumId) ? { ...rootAlbum, tags: newTags } : { ...albums.items[index], tags: newTags };
  return {
    index,
    updatedAlbum,
  };
});

export const addCategory = createAsyncThunk('/album/tags/category/new', async ({albumId, newCategory}, thunkAPI) => {
  const { rootAlbum, albums } = getState(thunkAPI);
  const index = (rootAlbum?.id === albumId) ? null : albums.items.findIndex(album => album.id === albumId);
  return {
    index,
    newCategory,
  };
});

export const changeMetaType = createAsyncThunk('/album/metaType/change', async ({metaType}) => {
  return metaType;
});

export const deleleAlbum = createAsyncThunk('/album/delete', async ({albumId}, thunkAPI) => {
  const { albums } = getState(thunkAPI);
  await delete_album(albumId);
  const index = albums.items.findIndex(album => album.id === albumId);
  return { index };
});

export const albumSlice = createSlice({
  name: 'albumState',
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder
        .addCase(loadRootAlbumInfo.fulfilled, (state, { payload }) => {
        Object.assign(state, payload);
        state.isLoading = false;
      }).addCase(loadRootAlbumInfo.pending, (state,  { meta }) => {
        pendingMoreRequests.add(meta.requestId);
        state.isLoading = true;
      }).addCase(searchAlbums.fulfilled, (state, { payload }) => {
        Object.assign(state, payload);
        state.isLoading = false;
      }).addCase(loadMoreSearchAlbums.pending, (state, { meta }) => {
        pendingMoreRequests.add(meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreSearchAlbums.fulfilled, (state, { meta, payload }) => {
        if (pendingMoreRequests.has(meta.requestId)) {
          state.albums.pageInfo = payload.albumsPage.pageInfo;
          state.albums.items.push(...payload.albumsPage.items || []);
          pendingMoreRequests.delete(meta.requestId);
        }
        state.isLoading = false;
      }).addCase(loadMoreAlbums.fulfilled, (state, { meta, payload }) => {
        if (pendingMoreRequests.has(meta.requestId)) {
          state.albums.pageInfo = payload.albumsPage.pageInfo;
          state.albums.items.push(...payload.albumsPage.items || []);
          pendingMoreRequests.delete(meta.requestId);
        }
        state.isLoading = false;
      }).addCase(loadMoreAlbums.pending, (state, { meta }) => {
        pendingMoreRequests.add(meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreAlbums.rejected, (state, action) => {
        state.isLoading = false;
      }).addCase(loadMoreImages.fulfilled, (state, { meta, payload }) => {
        if (pendingMoreRequests.has(meta.requestId)) {
          state.images.pageInfo = payload.imagesPage.pageInfo;
          state.images.items.push(...payload.imagesPage.items || []);
          pendingMoreRequests.delete(meta.requestId);
        }
        state.isLoading = false;
      }).addCase(loadMoreImages.pending, (state, { meta }) => {
        pendingMoreRequests.add(meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreImages.rejected, (state) => {
        state.isLoading = false;
      }).addCase(loadMoreVideos.pending, (state, { meta }) => {
        pendingMoreRequests.add(meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreVideos.rejected, (state) => {
        state.isLoading = false;
      }).addCase(loadMoreVideos.fulfilled, (state, { meta, payload }) => {
        if (pendingMoreRequests.has(meta.requestId)) {
          state.videos.pageInfo = payload.videosPage.pageInfo;
          state.videos.items.push(...payload.videosPage.items || []);
          pendingMoreRequests.delete(meta.requestId);
        }
        state.isLoading = false;
      }).addCase(loadMoreAudio.pending, (state, { meta }) => {
        pendingMoreRequests.add(meta.requestId);
        state.isLoading = true;
      }).addCase(loadMoreAudio.rejected, (state) => {
        state.isLoading = false;
      }).addCase(loadMoreAudio.fulfilled, (state, { meta, payload }) => {
        if (pendingMoreRequests.has(meta.requestId)) {
          state.audios.pageInfo = payload.audiosPage.pageInfo;
          state.audios.items.push(...payload.audiosPage.items || []);
          pendingMoreRequests.delete(meta.requestId);
        }
        state.isLoading = false;
      }).addCase(updateAlbum.fulfilled, (state, { payload }) => {
        updateAlbumByIndex(state, payload.index, payload.updatedAlbum);
      }).addCase(loadAlbumTags.fulfilled, (state, { payload }) => {
        updateAlbumByIndex(state, payload.index, payload.album);
      }).addCase(updateCategoryTags.fulfilled, (state, { payload }) => {
        updateAlbumByIndex(state, payload.index, payload.updatedAlbum);
      }).addCase(addCategory.fulfilled, (state, { payload }) => {
        if (payload.index === null) state.rootAlbum.tags.categories.push(payload.newCategory);
        else state.albums.items[payload.index].tags.categories.push(payload.newCategory);
      }).addCase(changeMetaType.fulfilled, (state, { payload }) => {
        state.rootAlbum.metaType = payload;
      }).addCase(deleleAlbum.fulfilled, (state, { payload }) => {
        state.albums.items.splice(payload.index, 1);
      });
  },
});

export const selectAlbums = (state) => {
  return state.albumState;
}

export default albumSlice.reducer;