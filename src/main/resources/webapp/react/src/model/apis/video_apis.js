export const get_video_handler = async (mediaId) => {
    const searchParams = new URLSearchParams({mediaId});
    return fetch('/album/videos/handler?' + searchParams.toString());
}