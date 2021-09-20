import React from 'react';
import { useState, useEffect } from 'react';

function ScrollLoader({loading, children, onLoad, hasNext, height}) {

    const [isBottom, setIsBottom] = useState(true);

    const onScrollLoad = () => {
        console.log('scrolling');
        const scrollTop = (document.documentElement
          && document.documentElement.scrollTop)
          || document.body.scrollTop;
        const scrollHeight = (document.documentElement
          && document.documentElement.scrollHeight)
          || document.body.scrollHeight;
        console.log('scrollTop ' + scrollTop + ' scrollHeight ' + scrollHeight);
        if (scrollTop + window.innerHeight + height >= scrollHeight) {
            setIsBottom(false);
        }
    }

    useEffect(() => {
        window.addEventListener('scroll', onScrollLoad);
        return () => window.removeEventListener('scroll', onScrollLoad);
    }, [onScrollLoad]);

    useEffect(() => {
        //if (!loading && isBottom) {
            //onLoad();
            //onScrollLoad();
        //}
    }, [isBottom, loading, onLoad, onScrollLoad]);

    return (
        <>{children}</>
    )
}

export default ScrollLoader;