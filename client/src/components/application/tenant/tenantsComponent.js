import React, { useEffect } from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useDispatch, useSelector } from 'react-redux'
import { fetchTenants, selectTenants, selectTenantsStatus, selectTenantsError, resetStatus } from "../../../state/tenantSlice";
import { useApiClient } from '../../../hooks/apiClientProvider'
import ErrorMessage from "../error";
import Spinner from "../spinner";


export default function Tenants() {
    console.log("Tenants");    
  const tenantsStatus = useSelector(selectTenantsStatus)  
  const tenants = useSelector(selectTenants)
  const error = useSelector(selectTenantsError)
  const dispatch = useDispatch()
  const apiClient = useApiClient().TenantApi

  useEffect(() => {
    console.log("Tenants.useEffect ", tenantsStatus)
    if(tenantsStatus === "pending") {
      dispatch(fetchTenants(apiClient))
    }
  }, [dispatch, tenantsStatus, apiClient])

  let content

  console.log("Invoices ", tenantsStatus)
  if(tenantsStatus === "loading"){
    content = <Spinner text="Loading..."/>
  } else if(tenantsStatus === "failed") {
    content = <ErrorMessage text={"Tenants load failed: " + error} action={resetStatus}/>
  } else if (tenantsStatus === "loaded"){
    content =     
      <div style={{ display: "flex" }}>
        <nav>
        <NavLink
              to={"/tenants/new"}
              key={"/tenants/new"}
            >     
              Add New tenant
            </NavLink>                 
          {tenants.map((tenant) => (
              <NavLink
              style={({ isActive }) => {
                  return {
                  display: "block",
                  margin: "1rem 0",
                  color: isActive ? "red" : "",
                  };
              }}
              to={`/tenants/${tenant.id}`}
              key={tenant.id}
            >
              {tenant.name}
            </NavLink>
          ))}
        </nav>
        <Outlet />
    </div>
  }

  return content;
}