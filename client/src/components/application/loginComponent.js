import React, { Fragment } from 'react'
import {useNavigate} from 'react-router-dom'
import {useAuth0} from '@auth0/auth0-react'
import { useDispatch } from 'react-redux'
import { userLoggedIn } from '../../state/userSlice'

export default function LoginComponent() {
  console.log("LoginComponent");
  const navigate = useNavigate();
  const { logout, loginWithRedirect } = useAuth0();    
  const dispatch = useDispatch();
 
  let navHome = () => {navigate('/')}
 
   let logIn = () => {
    console.log("Login called");
    loginWithRedirect();     
  }
 
  let logOut = () => {
    console.log("Logout called");
    logout({returnTo: window.location.origin});
    dispatch(userLoggedIn({}));
    navHome();
  }
 
  return (
          <Fragment>
            <LoginButton onClick={() => logIn() }/> {"  "}
            <LogoutButton onClick={() => logOut() }/>
          </Fragment>   
    );
  }

function LoginButton(params) {
  const { isAuthenticated } = useAuth0();   
  if( !isAuthenticated){
    return <button type='link' onClick={params.onClick }>Login</button>;
  }
}

function LogoutButton(params) {
  const { isAuthenticated } = useAuth0();   
  if( isAuthenticated){
    return <button type='link' onClick={params.onClick }>Logout</button>;
  }
}

