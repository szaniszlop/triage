import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux'
import { Auth0Provider } from "@auth0/auth0-react"
import {env} from './env/environment.js'

import './index.css';
import Router from './components/application/router.js';
import store from './state/store'
import ApiClientProvider from './hooks/apiClientProvider';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Auth0Provider
      domain = {env.AUTH0_DOMAIN} 
      clientId = {env.AUTH0_CLIENT_ID} 
      redirectUri= {window.location.origin }
      audience= {env.AUTH0_AUDIENCE} 
      scope="read:current_user read:users read:roles admin">
      <Provider store={store}>
        <ApiClientProvider>
          <Router />
        </ApiClientProvider>  
      </Provider>
    </Auth0Provider>    
  </React.StrictMode>
);


