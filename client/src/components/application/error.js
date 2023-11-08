import React from 'react'
import { useDispatch } from 'react-redux'

export default function ErrorMessage(props) {
  console.log("Error");
  const dispatch = useDispatch()

  let content
  if (props.action) {
    content = 
    <div className="App-error">
        <h2 className="App-error">{props.text}</h2>
        <button onClick={() => dispatch(props.action())}>Retry</button>
    </div>
  } else {
    content = 
    <div className="App-error">
        <h2 className="App-error">{props.text}</h2>
    </div>
  }
    return ( content  );
  }