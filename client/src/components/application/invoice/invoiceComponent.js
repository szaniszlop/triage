import React from "react";
import { useParams } from "react-router-dom";
import { useSelector } from 'react-redux'
import { selectInvoice } from "../../../state/invoiceSlice"; 
import ErrorMessage from "../error";

export default function Invoice() {
    console.log("Invoice");     
    const params = useParams();
    const invoice = useSelector(selectInvoice(params.invoiceId))   
    
    let content
    if(invoice) {
      content = 
      <main className="container App-item-details-box">
      <div className="flex-row">
        <div className="one-third"><b>Total Due:</b></div>
        <div className="two-thirds">{invoice.amount}</div>
      </div>
      <div className="flex-row">
        <div className="one-third">{invoice.name}:</div>
        <div className="two-thirds">{invoice.number}</div>
      </div>
      <div className="flex-row">
        <div className="one-third"><b>Due Date:</b></div>
        <div className="two-thirds">{invoice.due}</div>
      </div>
    </main>
    } else {
      content = 
      <ErrorMessage text={`Invoice ${params.invoiceId} not found`}/>
    }
    return ( content );
  }
