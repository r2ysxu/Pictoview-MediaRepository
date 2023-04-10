import {
  request,
  toSearchParams,
  ID,
  SortField,
  PageItems,
  Album,
  MediaItem,
  AlbumTag,
} from './api.types';

export const post_uploadAlbumPath = async (path: string): Promise<Album> => {
  return request<Album>('/album/update/filepath', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: path,
  });
}

export const get_album = async (albumId: ID) => {
  if (albumId <= 0) return { metaType: "albums" };
  const searchParams = toSearchParams({ albumId });
  return request<Album>('/album/get?' + searchParams.toString());
}

export const get_listAlbums = async (page: number, parentId: ID, sort: SortField): Promise<PageItems<Album>> => {
  if (sort === undefined) sort = { field: 'unsorted', ascending: true };
  const searchParams = toSearchParams({page, parentId, sortField: sort.field, ascending: sort.ascending });
  return request<PageItems<Album>>('/album/list?' + searchParams.toString());
}

export const get_searchAlbums = async (page: number, query: string, sort: SortField): Promise<PageItems<Album>> => {
  const searchParams = toSearchParams({page, query, sortField: sort.field, ascending: sort.ascending});
  return request<PageItems<Album>>('/album/search?' + searchParams.toString());
}

export const get_listAlbumImages = async (page: number, albumId: ID): Promise<PageItems<MediaItem>> => {
  if (albumId <= 0) return { items: [], pageInfo: { page: 0, total: 0, hasNext: false } };
  const searchParams = toSearchParams({page, albumId});
  return request<PageItems<MediaItem>>('/album/images/list?' + searchParams.toString());
}

export const get_listAlbumVideos = async (page: number, albumId: ID): Promise<PageItems<MediaItem>> => {
  if (albumId <= 0) return { items: [], pageInfo: { page: 0, total: 0, hasNext: false } };
  const searchParams = toSearchParams({page, albumId});
  return request<PageItems<MediaItem>>('/album/videos/list?' + searchParams.toString());
}

export const get_listAlbumAudios = async (page: number, albumId: ID): Promise<PageItems<MediaItem>> => {
  if (albumId <= 0) return { items: [], pageInfo: { page: 0, total: 0, hasNext: false } };
  const searchParams = toSearchParams({page, albumId});
  return request<PageItems<MediaItem>>('/album/audio/list?' + searchParams.toString());
}

export const get_listAlbumTags = async(albumId: ID): Promise<AlbumTag> => {
  if (albumId <= 0) return { albumId, tags: [], categories: [] };
  const searchParams = toSearchParams({ albumId });
  return request<AlbumTag>('/album/tag/list?' + searchParams.toString());
}

export const post_createAlbum = async(album: Album): Promise<Album> => {
  return request<Album>('/album/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(album),
  });
}

export const post_uploadMedia = async(albumId: ID, file: any): Promise<boolean> => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('albumId', albumId.toString());
  return request<boolean>('/album/update/upload/media', {
    method: 'POST',
    headers: { 'enctype': 'multipart/form-data' },
    body: formData,
  });
}

export const post_uploadAlbum = async(albumId: ID, file: any, fromMetadata: boolean): Promise<boolean> => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('albumId', albumId.toString());
  formData.append('fromMetadata', fromMetadata.toString());
  return request<boolean>('/album/update/upload', {
    method: 'POST',
    headers: { 'enctype': 'multipart/form-data' },
    body: formData,
  });
}

export const post_updateAlbum = async(album: Album): Promise<Album> => {
  return request<Album>('/album/update', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(album),
  });
}

export const post_changeAlbumCover = async(albumId: ID, imageId: ID): Promise<Album> => {
  return request<Album>('/album/update/cover', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ albumId, imageId }),
  });
}

export const delete_album = async(albumId: ID): Promise<boolean> => {
  return request<boolean>('/album/delete', {
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    body: albumId.toString()
  });
}