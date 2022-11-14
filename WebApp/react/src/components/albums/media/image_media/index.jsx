import React from 'react';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectAlbums, loadMoreImages } from '../../../../model/reducers/albumSlice';
import ScrollLoader from '../../../widgets/scroll_loader';
import ImagePhoto from './image_photo';
import ImageView from './image_view';
import './styles.css';

const LOAD_MORE_BUFFER = 10;

function ImageMedia({albumId, onFullViewOpen}) {
  const dispatch = useDispatch();
  const { images } = useSelector(selectAlbums);
  const isLoading = useSelector((state) => state.album.isLoading);
  const [selectedImageIndex, setSelectedImageIndex] = useState(null);
  const [isFetching, setFetching] = useState(false);

  const loadMore = () => {
    if (!isLoading && !isFetching) {
      setFetching(true);
      dispatch(loadMoreImages({albumId, page: images.pageInfo.page + 1}))
        .then( () => setFetching(false));
    }
  }

  const onSelectImage = (index) => {
    setSelectedImageIndex(index);
    if (index == null) {
      onFullViewOpen(false);
      return;
    } else if (index > images.items.length - LOAD_MORE_BUFFER && images.pageInfo.hasNext) {
      loadMore();
    }
    onFullViewOpen(true);
  }

  return (
    <div>
      <ScrollLoader isLoading={isLoading} loadMore={loadMore} height={50} hasMore={images.pageInfo.hasNext}>
        <div className="album_media_list">
          {images.items.map( (image, index) =>
            <ImagePhoto 
                key={index}
                index={index}
                image={image}
                onSelectIndex={onSelectImage} />
          )}
        </div>
      </ScrollLoader>
      <ImageView
          albumId={albumId}
          imageItems={images.items}
          selectedIndex={selectedImageIndex}
          onSelectIndex={onSelectImage} />
    </div>
  );
}

export default ImageMedia;