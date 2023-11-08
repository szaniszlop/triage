import { useParams } from "react-router-dom";
import { useSelector } from 'react-redux'
import { selectTenant } from "../../../state/tenantSlice"; 
import ErrorMessage from "../error";

export default function Tenant() {
    console.log("Tenant");     
    const params = useParams();
    const tenant = useSelector(selectTenant(params.tenantId))   
    
    let content
    if(tenant) {
      content = 
      <main className="container App-item-details-box">
      <div className="flex-row">
        <div className="one-third">{tenant.id}:</div>
        <div className="two-thirds">{tenant.name}</div>
      </div>
      <div className="flex-row">
        <div className="one-third"><b>Business Unit:</b></div>
      </div>
    </main>
    } else {
      content = 
      <ErrorMessage text={`Tenant ${params.tenantId} not found`}/>
    }
    return ( content );
  }
