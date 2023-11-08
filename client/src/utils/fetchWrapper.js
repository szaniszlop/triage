// @flow
 /*
type Configuration = {
    refreshToken: (options: Object) => Promise<void>,
    audience: string,
    scope: string
  }
*/

export default function configureFetchWrapper (configuration) {
    const { refreshToken, audience, scope } = configuration;

    const get = async (url) => {
        return fetchWithAccessToken(requestOptionsGet, url) ;
    }

    const post = (url, body) => {
        console.log("fetchwrapper.post")
        return fetchWithAccessToken(requestOptionsPost, url, body) ; 
    }

    const put = (url, body) => {
        return fetchWithAccessToken(requestOptionsPut, url, body) ; 
    }

    // prefixed with underscored because delete is a reserved word in javascript
    const _delete = (url) => {
        return fetchWithAccessToken(requestOptionsDelete, url) ; 
    }

    const fetchWrapper = {
        get,
        post,
        put,
        delete: _delete
    };

    // helper functions
    const fetchWithAccessToken = async (optionsProviderCb, url, body) => {
        return getAccessToken()
                .then((token) => {return optionsProviderCb(token, body)})
                .then((options) =>{return fetch(url, options)})
                .then(handleResponse);
    } 

    const handleResponse = async (response) => {
        console.log("Called: handleResponse", response)
        return response.text().then(text => {
            const data = text && JSON.parse(text);
            console.log("response text", data);
            if (!response.ok) {
                const error = (data && data.message) || response.statusText;
                return Promise.reject(error);
            }

            return Promise.resolve(data);
        });
    }

    const requestOptionsGet = async (accessToken, _) => {
        const requestOptions = {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${accessToken}`
                }
        };
        console.log("called: requestOptionsGet", requestOptions);
        return requestOptions;
    };

    function requestOptionsPost(accessToken, body){
        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                Authorization: `Bearer ${accessToken}` 
            },
            body: JSON.stringify(body)
        };        
        console.log("requestOptionsPost", requestOptions)
        return requestOptions;
    };

    function requestOptionsPut(accessToken, body){
        const requestOptions = {
            method: 'PUT',
            headers: { 
                'Content-Type': 'application/json',
                Authorization: `Bearer ${accessToken}` 
            },
            body: JSON.stringify(body)
        };        
        return requestOptions;
    };

    function requestOptionsDelete(accessToken, _){
        const requestOptions = {
            method: 'DELETE',
            headers: { 
                Authorization: `Bearer ${accessToken}` 
            }
        };        
        return requestOptions;
    };

    const getAccessToken = async () =>  {
        console.log("called: getAccessToken");
        return refreshToken({
                audience: audience,
                scope: scope
            });
    }

    return fetchWrapper; 
};

