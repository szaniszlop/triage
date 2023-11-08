import React from 'react'
import UserDetails from './userDetailsComponent';

export default function PublicComponent() {
    console.log("PublicComponent");
    return (     
        <div>
          <h2> About this application </h2>
          <UserDetails/>
        </div>   
    );
  }
