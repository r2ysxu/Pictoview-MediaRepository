export const get_userProfile = async () => {
    return fetch('/profile', {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
        }).then( response => response.json());
}

export const get_logout = async () => {
    fetch('/logout').then( response => response.json());
}