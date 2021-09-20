import React from 'react';
import { useState, useEffect, useCallback } from 'react';

function ScrollLoader({isLoading, children, loadMore, hasNext, height}) {

    const loadUntilScrollable = () => {
        const scrollHeight = (document.documentElement
          && document.documentElement.scrollHeight)
          || document.body.scrollHeight;
        if (window.innerHeight == scrollHeight && !isLoading) {
            loadMore();
        }
    }

    const onScrollLoad = () => {
        //console.log('scrolling');
        const scrollTop = (document.documentElement
          && document.documentElement.scrollTop)
          || document.body.scrollTop;
        const scrollHeight = (document.documentElement
          && document.documentElement.scrollHeight)
          || document.body.scrollHeight;
        if (scrollTop + window.innerHeight + height >= scrollHeight && !isLoading) {
            loadMore();
        }
    }

    useEffect(() => {
        loadUntilScrollable();
        window.addEventListener('scroll', onScrollLoad);
        return () => window.removeEventListener('scroll', onScrollLoad);
    }, [onScrollLoad, isLoading]);

    return (
        <>{children}</>
    )
}

export default ScrollLoader;