package com.psz.restdemo.security;

import org.springframework.http.HttpEntity;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

@Component("securityLabelAuthz")
public class SecurityLabelAuthorizationLogic {
    public boolean decide(MethodSecurityExpressionOperations operations){
        return operations.hasAuthority(Scopes.ADMIN) || hasObjectAuthority(operations);        
    }

    private boolean hasObjectAuthority(MethodSecurityExpressionOperations operations){
        if(operations.getReturnObject() instanceof HttpEntity ){
            return hasObjectAuthority((HttpEntity<? extends SecurityLabelHolder>)operations.getReturnObject(), 
                operations);
        }
        return false;
    }

    private boolean hasObjectAuthority(HttpEntity<? extends SecurityLabelHolder> entity, 
                                        MethodSecurityExpressionOperations operations){
        SecurityLabel securityLabel = entity.getBody().getSecurityLabel();
        return operations.hasAuthority(securityLabel.getLableType() + ":" + securityLabel.getLabel());
    }    
}
