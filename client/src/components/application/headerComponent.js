import React from 'react'
import { useSelector } from 'react-redux'
import { selectUserDetails } from '../../state/userSlice'
import LoginComponent from './loginComponent';
import Navigation from './navigationComponent';

export default function Header() {
  console.log("Header");
    return (
        <header className="App-header">
            <div className='flex-row'>
                <Logo/>
                <Title><h1>Test app</h1></Title>
                <UserInfo/>
            </div>        
            <Navigation/>
        </header>
    );
  }

function Logo( ){
    return(
        <div className="flex-large one-third App-header-logo">
            <div><img src={process.env.PUBLIC_URL + '/images/logo.svg'} alt="logo" className=".App-logo"/></div>
        </div>
    );
} 

function Title(props ){
    return(
        <div className="flex-large one-third App-header-title">
            {(props.children)}
        </div>
    );
} 

function UserInfo( ){
    const user = useSelector(selectUserDetails);
    if(user && user && user.name){
        return(
            <div className=" flex-row App-header-user-info">
                <LoginComponent/>
                <div><img src={user.picture} alt={user.name} /></div>
                <div>{user.name} {" "}</div>
                <div>{user.email}</div>            
            </div>
        );
        } else {
            return(
                <div className=" flex row App-header-user-info">
                    <LoginComponent/>
                </div>
            );
        }
}  
