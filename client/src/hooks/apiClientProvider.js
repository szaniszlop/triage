import React, { useContext, createContext } from 'react'
import {useAuth0} from '@auth0/auth0-react'
import configureFetchWrapper from '../utils/fetchWrapper'
import {env} from '../env/environment.js'

// declare context
const apiClientContext = createContext()

export default function ApiClientProvider({children}){
    const apiClient = useApiClientProvider()
    return <apiClientContext.Provider value={apiClient}>{children}</apiClientContext.Provider>
}

export const useApiClient = () => useContext(apiClientContext)

function useApiClientProvider(){
    const { getAccessTokenSilently } = useAuth0();  
    const fetchWraper = configureFetchWrapper({refreshToken: getAccessTokenSilently, audience: env.BACKEND_AUDIENCE, scope: env.BACKEND_SCOPE});
    const backendUrl = env.BACKEND_URL;
    const apiVersion = env.BACKEND_API_VERSION;
    const apiUrl = `${backendUrl}/${apiVersion}`

    return ({
        InvoiceApi : invoiceApi(fetchWraper, apiUrl),
        TenantApi : tenantApi(fetchWraper, apiUrl)
    })
}

function invoiceApi(fetchWraper, apiUrl){
    const getInvoices = async () => {
        return fetchWraper.get(`${apiUrl}/invoices`)        
    }

    const addInvoice = async (body) => {
        console.log("useApiClientProvider.addInvoice")
        return fetchWraper.post(`${apiUrl}/invoices`, body)        
    }

    const getInvoice = async (id) => {
        return fetchWraper.post(`${apiUrl}/invoices/${id}`)        
    }

    const deleteInvoice = async (id) => {
        return fetchWraper.delete(`${apiUrl}/invoices/${id}`)        
    }

    return ({
        getInvoices,
        addInvoice,
        getInvoice,
        deleteInvoice
    })    
}

function tenantApi(fetchWraper, apiUrl){
    const getTenants = async () => {
        return fetchWraper.get(`${apiUrl}/tenants/admin`)
    }

    const getTenant = async (id) => {
        return fetchWraper.get(`${apiUrl}/tenants/public/${id}`)
    }    

    const createTenant = async (body) => {
        return fetchWraper.post(`${apiUrl}/tenants/admin`, body)
    }    

    const changeTenant = async (id, body) => {
        return fetchWraper.put(`${apiUrl}/tenants/admin/${id}`, body)
    }  

    const deleteTenant = async (id, body) => {
        return fetchWraper.delete(`${apiUrl}/tenants/admin/${id}`)
    }      

    return ({
        getTenants, 
        getTenant, 
        createTenant,
        changeTenant, 
        deleteTenant
    })
}
