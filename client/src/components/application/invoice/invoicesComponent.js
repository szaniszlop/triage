import React, { useEffect } from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useDispatch, useSelector } from 'react-redux'
import { fetchInvoices, selectInvoices, selectInvoicesStatus, selectInvoicesError, resetStatus } from "../../../state/invoiceSlice";
import { useApiClient } from '../../../hooks/apiClientProvider'
import ErrorMessage from "../error";
import Spinner from "../spinner";


export default function Invoices() {
    console.log("Invoices");    
  const invoicesStatus = useSelector(selectInvoicesStatus)  
  const invoices = useSelector(selectInvoices)
  const error = useSelector(selectInvoicesError)
  const dispatch = useDispatch()
  const apiClient = useApiClient().InvoiceApi

  useEffect(() => {
    console.log("Invoices.useEffect ", invoicesStatus)
    if(invoicesStatus === "pending") {
      dispatch(fetchInvoices(apiClient))
    }
  }, [dispatch, invoicesStatus, apiClient])

  let content

  console.log("Invoices ", invoicesStatus)
  if(invoicesStatus === "loading"){
    content = <Spinner text="Loading..."/>
  } else if(invoicesStatus === "failed") {
    content = <ErrorMessage text={"Invoice load failed: " + error} action={resetStatus}/>
  } else if (invoicesStatus === "loaded"){
    content =     
      <div style={{ display: "flex" }}>
        <nav>
        <NavLink
              to={"/invoices/new"}
              key={"/invoices/new"}
            >     
              Add New Invoice
            </NavLink>                 
          {invoices.map((invoice) => (
              <NavLink
              style={({ isActive }) => {
                  return {
                  display: "block",
                  margin: "1rem 0",
                  color: isActive ? "red" : "",
                  };
              }}
              to={`/invoices/${invoice.number}`}
              key={invoice.number}
            >
              {invoice.name}
            </NavLink>
          ))}
        </nav>
        <Outlet />
    </div>
  }

  return content;
}