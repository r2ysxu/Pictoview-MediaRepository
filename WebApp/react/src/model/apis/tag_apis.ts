import {
  request,
  toSearchParams,
  ID,
  Tag,
  Category,
  AlbumTag,
} from './api.types';

export const get_categories = async (): Promise<Category[]> => {
  return request<Category[]>('/category/list');
}

export const get_tagsByCategory = async (categoryId: ID): Promise<Tag[]> => {
  const searchParams = toSearchParams({categoryId});
  return request<Tag[]>('/tag/list?' + searchParams.toString());
}

export const post_createCategory = async (categoryName: string): Promise<Category> => {
  return request<Category>('/tag/category/create', {
      method: 'POST',
      body: categoryName
    });
}

export const post_tagAlbum = async (albumCategoryTags: AlbumTag): Promise<AlbumTag> => {
  return request<AlbumTag>('/album/tag/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(albumCategoryTags),
  });
}

export const get_searchTags = async (tagQuery: string, categoryId: ID): Promise<Category[]> => {
  const searchParams = toSearchParams({tagQuery, categoryId});
  return request<Category[]>('/tag/search?' + searchParams.toString());
}