import React, { useEffect, useState } from "react";
import {useAuth0} from '@auth0/auth0-react'
import {env} from '../../env/environment.js'
import { useSelector } from 'react-redux'
import configureFetchWrapper  from '../../utils/fetchWrapper.js'

export default function UserDetails() {
    console.log("UserDetails");
    const { isAuthenticated, getAccessTokenSilently } = useAuth0();   
    const [userMetadata, setUserMetadata] = useState(null);
    const [userRoles, setUserRoles] = useState(null);
    const user = useSelector(state => state.user);

    useEffect(() => {
      console.log("useEffect called ");
      const domain = env.AUTH0_DOMAIN;
      const fetchWraper = configureFetchWrapper({refreshToken: getAccessTokenSilently, audience: `https://${domain}/api/v2/`, scope: env.AUTH0_DEFAULT_SCOPE});

      const initUserStateFromAuth0 = async (userDetailsByIdUrl, stateCallback) => {
        try {
          fetchWraper.get(userDetailsByIdUrl).then(stateCallback);
        } catch (e) {
          console.log(e.message);
        }
      }

      const getUserMetadata = async () => {
        console.log("getUserMetadata: ");
        const userDetailsByIdUrl = `https://${domain}/api/v2/users/${user.details.sub}`;
        initUserStateFromAuth0( userDetailsByIdUrl, (metadata) => {
            console.log("Called setUserMetadata: ", metadata);
            setUserMetadata(metadata);}
          );
      };

      if(isAuthenticated){
        getUserMetadata();
      }
    }, [user?.details.sub, isAuthenticated, setUserMetadata, setUserRoles, getAccessTokenSilently ]);

    if(isAuthenticated){
        return (
            <div>
              <h3> User details </h3>
              <img src={user.details.picture} alt={user.details.name} />
              <p>Name: {user.details.name}</p>
              <p>Email: {user.details.email}</p>
              <p>Phone Number: {user.details.phone_number}</p>
              <h3> User Metadata </h3>
              {userMetadata ? (
                <div className="App-codebox">
                <pre><code>{JSON.stringify(userMetadata, null, 2)}</code></pre>
                </div>
              ) : (
                <p>"No user metadata found"</p>
              )}
              {userRoles ? (
                <pre>{JSON.stringify(userRoles, null, 2)}</pre>
              ) : (
                <p>"No role metadata found"</p>
              )}

            </div>   
      );
    } 

  }

