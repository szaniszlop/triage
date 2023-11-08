import React, {useEffect} from 'react'
import {Outlet} from 'react-router-dom'
import Header from './components/application/headerComponent.js'
import { useDispatch, useSelector } from 'react-redux'
import {useAuth0} from '@auth0/auth0-react'
import {userLoggedIn} from './state/userSlice';
import { selectLocation } from './state/navigationSlice';
import { useNavigate } from 'react-router-dom';
import { setNavigation } from './state/navigationSlice.js'

function App() {
  console.log("inside App");
  const { isAuthenticated, user } = useAuth0();    
  const dispatch = useDispatch();
  const navigation = useNavigate();
  const location = useSelector(selectLocation);

  useEffect(() =>{ 
    console.log("App effect", isAuthenticated, user);
    if(isAuthenticated){
      dispatch(userLoggedIn(user));
      if(location && location.location && location.location.pathname){
        console.log("App effect dispatch to location", location);
        navigation(location);
        dispatch( setNavigation({}));
      }
    }
  }, [isAuthenticated, dispatch, user, navigation, location])

  return (
    <div className="App">
      <Header/>
      <div className="App-content">
        <Outlet />
      </div>
    </div>
  );
}

export default App;
