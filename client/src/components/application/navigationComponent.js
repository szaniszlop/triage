import { useAuth0 } from '@auth0/auth0-react';
import {Link} from 'react-router-dom'
import React from 'react'

export default function Navigation() {
  console.log("Navigation");
    return (
        <nav className="flex-row App-nav">
            <NavigationLink to="/about" isSecured={false}>About</NavigationLink>
            <NavigationLink to="/private" isSecured={true}>Private</NavigationLink>
            <NavigationLink to="/invoices" isSecured={true}>Invoices</NavigationLink>
            <NavigationLink to="/tenants" isSecured={true}>Tenants</NavigationLink>
        </nav>
    );
  }

function NavigationLink(props ){
    const {isAuthenticated} = useAuth0();
    if(!props.isSecured || (props.isSecured && isAuthenticated)){
    return(
        <Link to={props.to} className="App-tabstop round-button button"> {props.children} </Link>
    );
    } 
}  
