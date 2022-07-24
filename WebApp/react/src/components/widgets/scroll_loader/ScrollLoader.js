import React from 'react';
import { useEffect } from 'react';

function ScrollLoader({isLoading, children, loadMore, hasMore, height}) {

    const loadUntilScrollable = () => {
        const scrollHeight = (document.documentElement
          && document.documentElement.scrollHeight)
          || document.body.scrollHeight;
        if (window.innerHeight === scrollHeight && !isLoading && hasMore) {
            loadMore();
        }
    }

    const onScrollLoad = () => {
        const scrollTop = (document.documentElement
          && document.documentElement.scrollTop)
          || document.body.scrollTop;
        const scrollHeight = (document.documentElement
          && document.documentElement.scrollHeight)
          || document.body.scrollHeight;
        if (scrollTop + window.innerHeight + height >= scrollHeight && !isLoading && hasMore) {
            loadMore();
        }
    }

    useEffect(() => {
        loadUntilScrollable();
        window.addEventListener('scroll', onScrollLoad);
        return () => window.removeEventListener('scroll', onScrollLoad);
    });

    return (
        <>{children}</>
    )
}

export default ScrollLoader;