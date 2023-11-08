import React from 'react'

export default function Spinner(props) {
  console.log("Spinner");
    return (
        <div className="flex-row App-info">
            <h2 className='App-info'>{props.text}</h2>
        </div>
    );
  }