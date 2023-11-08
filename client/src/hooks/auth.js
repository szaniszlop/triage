//hooks/auth.js
import React, { useState, useEffect, useContext, createContext } from 'react'
import {useAuth0} from '@auth0/auth0-react'

// declare authentication context
const authContext = createContext()

// Authentication context holder as component
export function ProvideAuth({ children }) {
  console.log("inside ProvideAuth");
  const auth = useProvideAuth()
  return <authContext.Provider value={auth}> { children } </authContext.Provider> 
}

// authentication hook implementation
export const useAuth = () => useContext(authContext)

// Authentication content initialization function
function useProvideAuth() {
    const [myUser, setMyUser] = useState(null)
    const { loginWithRedirect, user, isAuthenticated, logout } = useAuth0();      

    const signin = (cb) => {
      loginWithRedirect();
      if(isAuthenticated){
        setMyUser(user);  
      }       
      console.log("inside signin; user:", user); 
      cb();
    }

    const signout = (cb) => {
      setMyUser(null);
      logout();
      cb();
    }

  useEffect(() => {
    }, [])

  return {
      myUser,
      signin,
      signout
    }
  }