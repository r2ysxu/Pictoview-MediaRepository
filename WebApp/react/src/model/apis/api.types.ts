import { MetaType } from 'model/constants/dataConstants';

const onResponseJson = (response: any) => {
  return response.json();
}

export function request<TResponse>(url: string, config: RequestInit = {}): Promise<TResponse> {
  return fetch(url, config).then(onResponseJson).then((data) => data as TResponse);
}

export const toSearchParams = (body: any) => {
  return new URLSearchParams(body);
}

export type ID = number;

export type SortField = {
  field: string;
  ascending: boolean;
}

export type PageInfo = {
  hasNext: boolean;
  total: number;
  page: number;
  sortedBy?: SortField;
}

export type PageItems<T> = {
  items: T[];
  pageInfo: PageInfo;
}

export interface Album {
  id: ID;
  name: string;
  altname: string;
  publisher: string;
  description: string;
  coverPhotoId: number;
  rating: number;
  metaType: MetaType;
}

export interface MediaItem {
  id: ID;
  name: string;
}

export interface Tag {
  id: ID;
  categoryId: ID;
  value: string;
  relevance: number;
}

export interface Category {
  id: ID;
  name: string;
  tags?: Tag[];
}

export interface AlbumTag {
  albumId: ID;
  tags: Tag[];
  categories: Category[];
}