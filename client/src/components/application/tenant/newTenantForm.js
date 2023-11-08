import React, { useState } from "react";
import { useNavigate } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { tenant } from "../../../state/tenantSlice"; 
import { useApiClient } from "../../../hooks/apiClientProvider";

export default function NewTenantForm() {
    console.log("NewTenantForm");     
    const [name, setName ] = useState("");
    const [id, setId ] = useState("");
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const apiClient = useApiClient();

    const onNameChanged = e => setName(e.target.value)
    const onIdChanged = e => setId(e.target.value)

    const formSubmit = () => {
        if(isFormValid()) {
            const body = {
                name, 
                id
            }
            navigate("/tenants")    
        }
    }

    const formCancel = () => {
        navigate("/tenants")
    }

    function isFormValid(){
        return isNameValid() && isIdValid() 
    }    

    function isNameValid(){
        return name && name.length >= 3
    }

    function isIdValid(){
        return id && id.length >= 3
    }

    return (
      <main className="container App-item-details-box">
        <section>
        <h2>New Tenant</h2>
            <label htmlFor="postName">Name:</label>
            <input
                type="text"
                id="postName"
                name="postName"
                placeholder="Customer Name"
                value={name}
                onChange={onNameChanged}
                className={isNameValid() ? "is-success" : "has-error"}
            />
            <label htmlFor="id">Id:</label>
            <input
                type="text"
                id="id"
                name="id"
                placeholder="tenant Id"
                value={id}
                onChange={onIdChanged}
                className={isIdValid() ? "is-success" : "has-error"}
            />
            <label htmlFor="submitForm">{ isFormValid() ? "Entry is valid" : "Enter a valid customer name, amount and due date"}</label>
            <div className="flex-row">
                <button 
                    type="button" 
                    id="submitForm"
                    onClick={formSubmit} 
                    className={isFormValid() ? "button" : "muted-button"}>
                    Save Invoice
                </button>
                <button 
                    type="button" 
                    id="cancelForm"
                    onClick={formCancel} >
                    Cancel
                </button>
            </div>
        </section>
      </main>
    );
  }